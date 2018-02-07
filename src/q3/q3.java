package q3;

import java.util.Scanner;

@SuppressWarnings({"FieldCanBeLocal", "NonAtomicOperationOnVolatileField"})
public class q3 {

    private static final int x = 2;
    private static final int limit = Integer.MAX_VALUE/x;
    private static final Object lock = new Object();
    private static volatile int volatileStaticCount;
    private static int staticCount;

    public static void main(String[] args) {
        long results[] = new long[8];
        long average;
        Scanner scan = new Scanner(System.in);
        System.out.println("x: " + x);
        System.out.println("limit: " + limit);
        System.out.println("1. part A");
        System.out.println("2. part B");
        System.out.println("3. part C");
        System.out.println("4. part D");
        System.out.println("5. part E");
        System.out.println("please enter an option: ");
        int input = scan.nextInt();
        switch(input) {
            // part A
            case 1:
                for (int i = 0; i < 8; i++) {
                    staticCount = 0;
                    System.out.println("i: " + i);
                    long currentTime = System.currentTimeMillis();
                    while (staticCount < limit) {
                        staticCount++;
                    }
                    long timeTaken = System.currentTimeMillis() - currentTime;
                    results[i] = timeTaken;
                }
                break;
            // part B
            case 2:
                for (int i = 0; i < 8; i++) {
                    volatileStaticCount = 0;
                    System.out.println("i: " + i);
                    long currentTime = System.currentTimeMillis();
                    while (volatileStaticCount < limit) {
//                        System.out.println("staticCount: " + staticCount);
                        volatileStaticCount++;
                    }
                    long timeTaken = System.currentTimeMillis() - currentTime;
                    results[i] = timeTaken;
                }
                break;
            // part C
            case 3:
                for (int i = 0; i < 8; i++) {
                    synchronized (lock) {
                        volatileStaticCount = 0;
                        System.out.println("i: " + i);
                        long currentTime = System.currentTimeMillis();
                        while (volatileStaticCount < limit) {
//                        System.out.println("staticCount: " + staticCount);
                            volatileStaticCount++;
                        }
                        long timeTaken = System.currentTimeMillis() - currentTime;
                        results[i] = timeTaken;
                    }
                }
                break;
            // part D
            case 4:
                for (int i = 0; i < 8; i++) {
                    System.out.println("i: " + i);
                    CounterThreadPartD.count = 0;
                    CounterThreadPartD t1 = new CounterThreadPartD();
                    CounterThreadPartD t2 = new CounterThreadPartD();
                    long currentTime = System.currentTimeMillis();
                    t1.start();
                    t2.start();
                    try {
                        t1.join();
                        t2.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long timeTaken = System.currentTimeMillis() - currentTime;
                    results[i] = timeTaken;
                }
                break;
            // part E
            case 5:
                for (int i = 0; i < 8; i++) {
                    System.out.println("i: " + i);
                    CounterThreadPartE.count = 0;
                    CounterThreadPartE t1 = new CounterThreadPartE();
                    CounterThreadPartE t2 = new CounterThreadPartE();
                    long currentTime = System.currentTimeMillis();
                    t1.start();
                    t2.start();
                    try {
                        t1.join();
                        t2.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long timeTaken = System.currentTimeMillis() - currentTime;
                    results[i] = timeTaken;

                }
                break;
            default:
                System.out.println("invalid option");
                break;
        }
        System.out.println("results of 8 runs (ms):");
        average = 0;
        for (int i = 0; i < results.length; i++) {
            System.out.println("time: " + results[i]);
            if (i >= 1)
                average += results[i];
        }
        average /= results.length - 1;
        System.out.println("avg of last 7 runs: "  + average);
    }
}
