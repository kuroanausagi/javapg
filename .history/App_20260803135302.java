// この行は、HttpServer（Webサーバーの本体）を使うための準備です。
import com.sun.net.httpserver.HttpServer;
// この行は、InetSocketAddress（通信先の住所）を使うための準備です。
import java.net.InetSocketAddress;
// この行は、StandardCharsets（UTF-8の文字の作法）を使うための準備です。
import java.nio.charset.StandardCharsets;

// この行は、App クラスを作ります。これは Web サーバーを起動するための本体です。
public class App {
    // この行は、main メソッドを作ります。これはプログラムを始める入口です。
    public static void main(String[] args) throws Exception {
        // この行は、8080 番ポート（通信の入口番号）で待ち受けるサーバーを作ります。
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0); // この行は、サーバーの本体を作ります。
        // この行は、"/" へ来たときの処理を、その場で直接書いて設定します。
        server.createContext("/", exchange -> { // この行は、パス「/」に来たときの処理を登録します。
            // この行は、ブラウザへ返す文字を message という名前の変数に入れます。
            String message = "Hello, Server!"; // この行は、返す文字を message に入れます。
            // この行は、message を UTF-8 の文字データに変えます。
            byte[] response = message.getBytes(StandardCharsets.UTF_8); // この行は、文字を送るためのデータにします。
            // この行は、ブラウザに UTF-8 で返すことを伝えます。
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8"); // この行は、文字の形式を指定します。
            // この行は、HTTP の成功番号 200 と、データの長さを送ります。
            exchange.sendResponseHeaders(200, response.length); // この行は、返す内容の始まりを送ります。
            // この行は、データをブラウザへ書き出します。
            exchange.getResponseBody().write(response); // この行は、実際の文字を送ります。
            // この行は、通信を終わらせます。
            exchange.close(); // この行は、通信の出口を閉じます。
        });
        // この行は、同時にたくさんのアクセスを処理できるようにします。
        server.setExecutor(null); // この行は、標準の実行役を使います。
        // この行は、サーバーの待ち受けを始めます。
        server.start(); // この行は、サーバーを起動します。
        // この行は、起動したことをターミナルに表示します。
        System.out.println("サーバー起動: http://localhost:8080 （止めるときは Ctrl+C）"); // この行は、指定の文を画面に出します。
    }
}
