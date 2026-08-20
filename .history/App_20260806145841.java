
// 小さなWebサーバー（ブラウザからのアクセスに応答するプログラム）の機能を読み込みます。
import com.sun.net.httpserver.HttpServer;
// 接続を待ち受ける場所と番号を指定する機能を読み込みます。
import java.net.InetSocketAddress;
// URL用に変換された文字を元へ戻すURLDecoderを使えるようにします。
import java.net.URLDecoder;
// POSTの本文を読み取るInputStreamを使えるようにします。
import java.io.InputStream;
// ArrayList（あとから値を追加できるList）を使えるようにします。
import java.util.ArrayList;
// List（複数の値を順番に入れる入れ物）を使えるようにします。
import java.util.List;

// Appという名前のクラス（処理をまとめる入れ物）を作ります。
public class App {

  // Todoを貯めるListをハンドラの外で一度だけ作ります。
  private static final List<String> todos = new ArrayList<>(); // 追加した行: Todo一覧を保持します

  // プログラムを起動したとき、最初に実行されるmainメソッドです。
  public static void main(String[] args) throws Exception {

    // 8080番ポート（接続を受け付ける番号）でWebサーバーを用意します。
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

    // 「/」から始まるアクセスが来たときの処理を、ここへ登録します。
    server.createContext("/", exchange -> {

      // アクセスされたパスとHTTPメソッドを取り出します。
      String path = exchange.getRequestURI().getPath();
      String method = exchange.getRequestMethod();

      // ブラウザへ返す文字列を入れるmessageを用意します。
      String message;

      // pathが「/hello」と同じ場合の処理です。
      if (path.equals("/hello")) {

        // URLの「?」より後ろにあるクエリを取り出します。
        String query = exchange.getRequestURI().getRawQuery();

        // あいさつに使う名前を入れる変数です。
        String name;

        // クエリがない場合は「ゲスト」にします。
        if (query == null) {
          name = "ゲスト";

        } else {

          // 「name=」より後ろを切り出します。
          name = query.substring("name=".length());

          // URL用に変換された日本語などを元へ戻します。
          name = URLDecoder.decode(name, "UTF-8");
        }

        // 切り出した名前を使って、返す文字列を作ります。
        message = "こんにちは、" + name + "さん！";

        // pathが「/bye」と同じ場合の処理です。
      } else if (path.equals("/bye")) {

        message = "さようなら！";

        // pathが「/menu」と同じ場合の処理です。
      } else if (path.equals("/menu")) {

        message = "今日の定食はカレー";

        // POSTで「/add」に来た場合の処理です。
      } else if (path.equals("/add") && method.equals("POST")) {

        // POSTされた本文をUTF-8で読み取ります。
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), "UTF-8");

        // bodyの中からtodo=より後ろを取り出します。
        String todo = "";
        if (body.startsWith("todo=")) {
          todo = URLDecoder.decode(body.substring("todo=".length()), "UTF-8");
        }

        // 空の入力は追加しません。
        if (!todo.isBlank()) {
          todos.add(todo); // 追加した行: POSTで送信されたTodoをリストに追加します
        }

        // / へ戻すためのリダイレクトを送ります。
        exchange.getResponseHeaders().set("Location", "/");
        exchange.sendResponseHeaders(303, -1);
        exchange.close();
        return;

        // pathが「/」で、GETのときは入力フォームとTodo一覧を表示します。
      } else if (path.equals("/") && method.equals("GET")) {

        // 入力フォームとTodo一覧をHTMLで作ります。
        String html = "<html><body>";
        html += "<form method='post' action='/add'>";
        html += "<input type='text' name='todo' />";
        html += "<button type='submit'>追加</button>";
        html += "</form>";
        html += "<h2>Todo一覧</h2>";
        html += "<ul>";

        for (String todo : todos) {
          html += "<li>" + todo + "</li>"; // 追加した行: Todoを一覧に表示します
        }

        html += "</ul>";
        html += "</body></html>";

        message = html;

        // どのパスにも当てはまらない場合の処理です。
      } else {

        message = "ページが見つかりません";
      }

      // / はHTML、それ以外は普通の文字としてブラウザへ伝えます。
      exchange.getResponseHeaders().set(
          "Content-Type",
          path.equals("/")
              ? "text/html; charset=UTF-8" // 変更した行: 「/」でHTMLを返します
              : "text/plain; charset=UTF-8");

      // 返す文字列を、ネットワークで送れるバイト配列へ変換します。
      byte[] body = message.getBytes("UTF-8");

      // 正常を表す200と、返すデータの大きさをブラウザへ伝えます。
      exchange.sendResponseHeaders(200, body.length);

      // データをブラウザへ送ります。
      exchange.getResponseBody().write(body);

      // 応答を閉じ、送信が終わったことを伝えます。
      exchange.getResponseBody().close();
    });

    // Webサーバーを起動します。
    server.start();

    // 起動したことと止め方をターミナルに表示します。
    System.out.println(
        "サーバー起動: http://localhost:8080 （止めるときは Ctrl+C）");
  }
}