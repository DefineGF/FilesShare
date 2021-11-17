package util;

public class Log {
    private final String tag;

    public Log(String tag) {
        this.tag = tag;
    }

    public void empty() {
        System.out.println();
    }
    public void i_n(String msg) {
        System.out.println(tag + msg);
    }

    public void i(String msg) {
        System.out.print(tag + msg);
    }

    public void e(String msg) {
        System.err.println(tag + msg);
    }

    public void p(String msg) {
        System.out.print("\r" + tag + msg);
    }
}
