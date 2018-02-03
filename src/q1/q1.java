package q1;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.*;

public class q1 {

    private static BufferedImage img;
    private static final double PI = 3.1415926535;
    private static int width = 1920;
    private static int height = 1080;
    private static boolean pixelReserved[][] = new boolean[width][height];
    private static AtomicInteger circlesDrawn = new AtomicInteger();
    private static final Object pixelLock = new Object();

    // draws a circle
    // original C code from: http://www.softwareandfinance.com/Turbo_C/DrawCircle.html
    // code was modified to draw full circles
    private static void drawCircle(int radius, int xPos, int yPos, Color color) {
        if (xPos >= width || yPos >= height)
            throw new IllegalArgumentException();

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
//    private static void reservePixels(int radius, int xPos, int yPos) {
//        if (xPos >= width || yPos >= height)
//            throw new IllegalArgumentException();
//
//        double angle;
//        int x, y;
//
//        for (int r = 0; r < radius; r++) {
//            for (double i = 0; i < 360; i = i + 0.01) {
//                angle = i;
//                x = (int)(r * Math.cos(angle * PI / 180));
//                y = (int)(r * Math.sin(angle * PI / 180));
//                pixelReserved[xPos + x][yPos + y] = true;
//            }
//        }
//    }

    // reserves the pixels needed to draw a circle
    private static boolean synchronizedReservePixels(int radius, int xPos, int yPos) {
        synchronized (pixelLock) {
            if (xPos >= width || yPos >= height)
                throw new IllegalArgumentException();

            double angle;
            int x, y;

            for (int r = 0; r < radius; r++) {
                for (double i = 0; i < 360; i = i + 0.01) {
                    angle = i;
                    x = (int)(r * Math.cos(angle * PI / 180));
                    y = (int)(r * Math.sin(angle * PI / 180));
                    if (pixelReserved[xPos + x][yPos + y])
                        return false;
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
            return true;
        }
    }

    // checks if the pixels required are already reserved
//    private static boolean canDraw(int radius, int xPos, int yPos) {
//        if (xPos >= width || yPos >= height)
//            throw new IllegalArgumentException();
//
//        double angle;
//        int x, y;
//
//        for (int r = 0; r < radius; r++) {
//            for (double i = 0; i < 360; i = i + 0.01) {
//                angle = i;
//                x = (int)(r * Math.cos(angle * PI / 180));
//                y = (int)(r * Math.sin(angle * PI / 180));
//                if (pixelReserved[xPos + x][yPos + y])
//                    return false;
//            }
//        }
//        return true;
//    }

    public static void main(String[] args) {

        circlesDrawn.set(0);


        try {
            if (args.length<3)
                throw new Exception("Missing arguments, only "+args.length+" were specified!");
            // arg 0 is the max radius
            int r = Integer.parseInt(args[0]);
            // arg 1 is count
            int c = Integer.parseInt(args[1]);
            // arg 2 is a boolean
            boolean multithreaded = Boolean.parseBoolean(args[2]);

            // initialize img pixels to all 0's
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i< width; i++) {
                for (int j = 0; j< height; j++) {
                    img.setRGB(i,j,0);
                }
            }

            long startTime = System.currentTimeMillis();
            if (multithreaded) {
                Random rng = new Random();

                while(circlesDrawn.get() < c) {
                    int radius = Math.max(10, rng.nextInt(r));
                    int x = Math.max(radius, rng.nextInt(width - radius));
                    int y = Math.max(radius, rng.nextInt(height - radius));
                    if (synchronizedReservePixels(radius, x, y)) {
                        drawCircle(radius, x, y, Color.black);
                        circlesDrawn.incrementAndGet();
                    }
                }
            }
            else {
                Random rng = new Random();

                while(circlesDrawn.get() < c) {
                    int radius = Math.max(10, rng.nextInt(r));
                    int x = Math.max(radius, rng.nextInt(width - radius));
                    int y = Math.max(radius, rng.nextInt(height - radius));
                    if (synchronizedReservePixels(radius, x, y)) {
                        drawCircle(radius, x, y, Color.black);
                        circlesDrawn.incrementAndGet();
                    }
                }

            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("time: " + elapsedTime + " ms");

            // Write out the image
            File outputfile = new File("outputimage.png");
            ImageIO.write(img, "png", outputfile);
        } catch (Exception e) {
            System.out.println("ERROR " +e);
            e.printStackTrace();
        }
    }
}
