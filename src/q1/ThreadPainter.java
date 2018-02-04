package q1;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class ThreadPainter extends Thread {

    private final BufferedImage img;
    private final int maxRadius;
    private final int numCircles;
    private Color color;
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

    ThreadPainter(BufferedImage img, int maxRadius, int numCircles, String name) {
        System.out.println("ThreadPainter " + name + " created.");
        this.img = img;
        this.maxRadius = maxRadius;
        this.numCircles = numCircles;
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
    // code was modified to draw filled in circles and to add wrap-around
    private void drawCircle(int radius, int xPos, int yPos) {

        // corner case: sometimes the counter is incremented while the other thread has already entered the loop
        if (counter.get() > numCircles)
            return;

        System.out.println(name + " drawing circle at " + xPos + "," + yPos);
        double angle;
        int x, y;
        int rgb = new Color((int)(Math.random() * 0x1000000)).getRGB();

        for (int r = 0; r < radius; r++) {
            for (double i = 0; i < 360; i = i + 0.01) {
                angle = i;
                x = (int)(r * Math.cos(angle * PI / 180));
                x += xPos;
                x %= width;
                if (x < 0) x+= width;
                y = (int)(r * Math.sin(angle * PI / 180));
                y += yPos;
                y %= height;
                if (y < 0) y+= height;
                img.setRGB(x, y, rgb);
            }
        }
        System.out.println(name + " finished drawing circle at " + xPos + "," + yPos);
    }

    // reserves the pixels needed to draw a circle
    private boolean testAndLockPixels(int radius, int xPos, int yPos) {



        double angle;
        int x, y;

        synchronized (pixelLock) {
            // corner case: sometimes the counter is incremented while the other thread has already entered the loop
            if (counter.get() >= numCircles)
                return false;

            for (int r = 0; r < radius; r++) {
                for (double i = 0; i < 360; i = i + 0.01) {
                    angle = i;
                    x = (int) (r * Math.cos(angle * PI / 180));
                    x += xPos;
                    x %= width;
                    if (x < 0) x += width;
                    y = (int) (r * Math.sin(angle * PI / 180));
                    y += yPos;
                    y %= height;
                    if (y < 0) y += height;
                    if (pixelReserved[x][y]) {
                        System.out.println(name + " could not reserve circle at " + xPos + "," + yPos);
                        return false;
                    }
                }
            }
            for (int r = 0; r < radius; r++) {
                for (double i = 0; i < 360; i = i + 0.01) {
                    angle = i;
                    x = (int) (r * Math.cos(angle * PI / 180));
                    x += xPos;
                    x %= width;
                    if (x < 0) x += width;
                    y = (int) (r * Math.sin(angle * PI / 180));
                    y += yPos;
                    y %= height;
                    if (y < 0) y += height;
                    pixelReserved[x][y] = true;
                }
            }
            counter.incrementAndGet();
        }

        System.out.println(name + " reserved circle at " + xPos + "," + yPos);
        System.out.println(name + " incrementing counter to " + counter.get());
        return true;
    }

    private void unlockPixels(int radius, int xPos, int yPos) {

        double angle;
        int x, y;
        synchronized (pixelLock) {
            for (int r = 0; r < radius; r++) {
                for (double i = 0; i < 360; i = i + 0.01) {
                    angle = i;
                    x = (int)(r * Math.cos(angle * PI / 180));
                    x += xPos;
                    x %= width;
                    if (x < 0) x+= width;
                    y = (int)(r * Math.sin(angle * PI / 180));
                    y += yPos;
                    y %= height;
                    if (y < 0) y+= height;
                    pixelReserved[x][y] = false;
                }
            }
        }
    }

    public void run() {

            Random rng = new Random();

            try {
                while(counter.get() < numCircles) {
                    int radius = rng.nextInt(maxRadius);
                    int x = rng.nextInt(width);
                    int y = rng.nextInt(height);

                    if (testAndLockPixels(radius, x, y)) {
                        drawCircle(radius, x, y);
                        unlockPixels(radius, x, y);
                    }
                    Thread.sleep(2);
                }
            }
            catch(InterruptedException e) {
                System.out.println("Thread color " + color + " interrupted.");
            }



    }
}
