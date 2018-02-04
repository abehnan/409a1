package q1;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class ThreadPainter extends Thread {
    private final BufferedImage img;
    private static final double PI = 3.1415926535;
    private static final int width = 1920;
    private static final int height = 1080;
    private static final boolean pixelReserved[][] = new boolean[width][height];
    private final int maxRadius;
    private final int numCircles;
    private final  AtomicInteger counter;
    private static final Object pixelLock = new Object();
    private final Color color;

    ThreadPainter(BufferedImage img, int maxRadius, int numCircles, AtomicInteger counter, Color color) {
        System.out.println("ThreadPainter created color " + color);
        this.img = img;
        this.counter = counter;
        this.maxRadius = maxRadius;
        this.numCircles = numCircles;
        this.color = color;
    }

    // draws a circle
    // original C code from: http://www.softwareandfinance.com/Turbo_C/DrawCircle.html
    // code was modified to draw full circles
    private void drawCircle(int radius, int xPos, int yPos) {
        if (xPos >= width || yPos >= height)
            throw new IllegalArgumentException();

        System.out.println("drawing circle at " + xPos + "," + yPos);
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

        counter.incrementAndGet();
    }

    // reserves the pixels needed to draw a circle
    private boolean synchronizedReservePixels(int radius, int xPos, int yPos) {
        synchronized (pixelLock) {
            System.out.print("attempting to reserve circle at " + xPos + "," + yPos + "...");
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
            System.out.println("sucess!");
            return true;
        }
    }

    public void run() {

            Random rng = new Random();

            while(counter.get() < numCircles) {
                int radius = Math.max(10, rng.nextInt(maxRadius));
                int x = Math.max(radius, rng.nextInt(width - radius));
                int y = Math.max(radius, rng.nextInt(height - radius));
                if (synchronizedReservePixels(radius, x, y)) {
                    drawCircle(radius, x, y);
                    counter.incrementAndGet();
                }
            }


    }
}
