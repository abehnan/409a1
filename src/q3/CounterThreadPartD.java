package q3;

public class CounterThreadPartD extends Thread {
    private static final Object lock = new Object();
    public static volatile int count = 0;
    private static int x = 2;
    private static int limit = Integer.MAX_VALUE/x;

    public void run() {
        while (true) {
            synchronized (lock) {
                if (count >= limit)
                    break;
                else
                    count++;
            }
        }
    }
}
