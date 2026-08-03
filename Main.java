// この行は、文字を並べた一覧を使うための準備です。
import java.util.ArrayList;
// この行は、List（複数の値を順番に入れる箱）を使うための準備です。
import java.util.List;

// このクラスは、プログラムを開始するための本体です。
public class Main {
    // このメソッドは、プログラムを動かしたときに最初に実行される入口です。
    public static void main(String[] args) {
        // この行は、Todoを入れるためのList（複数の値を順番に入れる箱）を作ります。
        List<Todo> todos = new ArrayList<>();

        // この行は、「牛乳を買う」という未完了のTodoを追加します。
        todos.add(new Todo("牛乳を買う", false));
        // この行は、「ゴミを出す」という完了済みのTodoを追加します。
        todos.add(new Todo("ゴミを出す", true));

        // このfor文は、Listの中身を1つずつ順番に取り出して処理します。
        for (Todo todo : todos) {
            // この行は、Todoを<li>...</li>の形の文字列に変えて、ターミナルに1行ずつ表示します。
            System.out.println(todo.toItem());
        }
    }
}

// このクラスは、やること1件分を表します。
class Todo {
    // この変数は、Todoのタイトル（文字）を覚えておきます。
    private String title;
    // この変数は、done（済んだかどうか）を true / false で覚えておきます。
    private boolean done;

    // このコンストラクタは、Todoを作るときに title と done を受け取ります。
    public Todo(String title, boolean done) {
        // この行は、受け取った title をこのTodoの title に入れます。
        this.title = title;
        // この行は、受け取った done をこのTodoの done に入れます。
        this.done = done;
    }

    // このメソッドは、Todo 1件を <li>...</li> の1行の文字列に変えます。
    public String toItem() {
        // このif文は、done が true（済み）かどうかを確認します。
        if (done) {
            // この行は、済みの印を付けた <li>...</li> を返します。
            return "<li>[済] " + title + "</li>";
        }

        // この行は、まだ済んでいないときの <li>...</li> を返します。
        return "<li>" + title + "</li>";
    }
}
