package q1;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class q1 {
    public static void main(String[] args) {
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
            BufferedImage img = new BufferedImage(ThreadPainter.getWidth(), ThreadPainter.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i< ThreadPainter.getWidth(); i++) {
                for (int j = 0; j< ThreadPainter.getHeight(); j++) {
                    img.setRGB(i,j,Color.white.getRGB());
                }
            }

            long startTime = System.currentTimeMillis();
            if (multithreaded) {
                ThreadPainter t1 = new ThreadPainter(img, r, c, Color.red, "Red");
                ThreadPainter t2 = new ThreadPainter(img, r, c, Color.blue, "Blue");

                t1.start();
                t2.start();

                t1.join();
                t2.join();
            }
            else {
                ThreadPainter t1 = new ThreadPainter(img, r, c, Color.black, "Black");
                t1.start();
                t1.join();
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("\ntime: " + elapsedTime + " ms");

            // Write out the image
            File outputfile = new File("outputimage.png");
            ImageIO.write(img, "png", outputfile);
        } catch (Exception e) {
            System.out.println("ERROR " +e);
            e.printStackTrace();
        }
    }
}
