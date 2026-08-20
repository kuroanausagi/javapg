
// 小さなWebサーバー（ブラウザからのアクセスに応答するプログラム）の機能を読み込みます。
import com.sun.net.httpserver.HttpServer;
// 接続を待ち受ける場所と番号を指定する機能を読み込みます。
import java.net.InetSocketAddress;
// URL用に変換された文字を元へ戻すURLDecoderを使えるようにします。
import java.net.URLDecoder;
// ArrayList（あとから値を追加できるList）を使えるようにします。
import java.util.ArrayList;
// List（複数の値を順番に入れる入れ物）を使えるようにします。
import java.util.List;

// Appという名前のクラス（処理をまとめる入れ物）を作ります。
public class App {

  // プログラムを起動したとき、最初に実行されるmainメソッドです。
  public static void main(String[] args) throws Exception {

    // 8080番ポート（接続を受け付ける番号）でWebサーバーを用意します。
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

    // 「/」から始まるアクセスが来たときの処理を、ここへ登録します。
    server.createContext("/", exchange -> {

      // アクセスされたパスを取り出してpathに入れます。
      String path = exchange.getRequestURI().getPath();

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

        // pathが「/todos」と同じ場合の処理です。
      } else if (path.equals("/todos")) {

        // Todoを入れる、あとから追加可能なListを作ります。
        List<String> todos = new ArrayList<>();

        // Section 12では、Todoをコードに直接書いて追加します。
        todos.add("牛乳を買う");
        todos.add("卵を買う");
        todos.add("パンを買う");

        // 一覧全体のHTMLを組み立て始めます。
        String html = "<ul>";

        // todosからTodoを1件ずつ取り出します。
        for (String todo : todos) {

          // 取り出したTodoを<li>で囲み、htmlの後ろへ足します。
          html += "<li>" + todo + "</li>";
        }

        // 一覧を閉じてHTMLを完成させます。
        html += "</ul>";

        // 完成したHTMLを、ブラウザへ返すmessageに入れます。
        message = html;

        // どのパスにも当てはまらない場合の処理です。
      } else {

        message = "ページが見つかりません";
      }

      // /todosはHTML、それ以外は普通の文字としてブラウザへ伝えます。
      exchange.getResponseHeaders().set(
          "Content-Type",
          path.equals("/todos")
              ? "text/html; charset=UTF-8"
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