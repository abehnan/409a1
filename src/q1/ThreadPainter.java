package q1;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class ThreadPainter extends Thread {

    private final BufferedImage img;
    private final int maxRadius;
    private final int numCircles;
    private final Color color;
    private final String name;
    private static final double PI = 3.1415926535;
    private static final int width = 1920;
    private static final int height = 1080;
    private static final boolean pixelReserved[][] = new boolean[width][height];
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final Object pixelLock = new Object();


    ThreadPainter(BufferedImage img, int maxRadius, int numCircles, Color color, String name) {
        System.out.println("ThreadPainter " + name + " created.");
        this.img = img;
        this.maxRadius = maxRadius;
        this.numCircles = numCircles;
        this.color = color;
        this.name = name;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    // draws a circle
    // original C code from: http://www.softwareandfinance.com/Turbo_C/DrawCircle.html
    // code was modified to draw full circles
    private void drawCircle(int radius, int xPos, int yPos) {
        if (xPos >= width || yPos >= height)
            throw new IllegalArgumentException();

        System.out.println(name + " drawing circle at " + xPos + "," + yPos);
        double angle;
        int x, y;

        for (int r = 0; r < radius; r++) {
            for (double i = 0; i < 360; i = i + 0.01) {
                angle = i;
                x = (int)(r * Math.cos(angle * PI / 180));
                y = (int)(r * Math.sin(angle * PI / 180));
                img.setRGB(xPos + x, yPos + y, color.getRGB());
            }
        }
    }

    // reserves the pixels needed to draw a circle
    private boolean synchronizedReservePixels(int radius, int xPos, int yPos) {
        synchronized (pixelLock) {

            // corner case: sometimes the counter is incremented while the other thread has already entered the loop
            if (counter.get() >= numCircles)
                return false;

            System.out.print(name + " attempting to reserve circle at " + xPos + "," + yPos + "...");
            if (xPos >= width || yPos >= height)
                throw new IllegalArgumentException();

            double angle;
            int x, y;

            for (int r = 0; r < radius; r++) {
                for (double i = 0; i < 360; i = i + 0.01) {
                    angle = i;
                    x = (int)(r * Math.cos(angle * PI / 180));
                    y = (int)(r * Math.sin(angle * PI / 180));
                    if (pixelReserved[xPos + x][yPos + y]) {
                        System.out.println("failed.");
                        return false;
                    }
                }
            }


            for (int r = 0; r < radius; r++) {
                for (double i = 0; i < 360; i = i + 0.01) {
                    angle = i;
                    x = (int)(r * Math.cos(angle * PI / 180));
                    y = (int)(r * Math.sin(angle * PI / 180));
                    pixelReserved[xPos + x][yPos + y] = true;
                }
            }
            System.out.println("success!");
            System.out.println(name + " incrementing counter to " + counter.incrementAndGet());
            return true;
        }
    }

    public void run() {

            Random rng = new Random();

            try {
                while(counter.get() < numCircles) {
                    int radius = Math.max(20, rng.nextInt(maxRadius));
                    int x = Math.max(radius, rng.nextInt(width - radius));
                    int y = Math.max(radius, rng.nextInt(height - radius));

                    if (synchronizedReservePixels(radius, x, y))
                        drawCircle(radius, x, y);
                    else
                        Thread.sleep(50);

                }
            }
            catch(InterruptedException e) {
                System.out.println("Thread color " + color + " interrupted.");
            }



    }
}
