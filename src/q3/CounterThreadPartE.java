package q3;

public class CounterThreadPartE extends Thread{

    public static int count = 0;
    private static int x = 2;
    private static int limit = Integer.MAX_VALUE/x;

    public void run() {
        while (count < limit) {
            count++;
        }
    }
}
