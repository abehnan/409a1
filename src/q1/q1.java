package q1;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class q1 {

    private static BufferedImage img;
    private static int width = 1920;
    private static int height = 1080;

    private static void drawCircle(int radius, int xPos, int yPos, Color color) {
        if (xPos >= width || yPos >= height)
            throw new IllegalArgumentException();
        final double PI = 3.1415926535;
        double angle;
        int x, y;

        for (double i = -90; i < 90; i = i + 0.01) {
            angle = i;
            x = (int)(radius * Math.cos(angle * PI / 180));
            y = (int)(radius * Math.sin(angle * PI / 180));
            for (int j = xPos - x; j < xPos + x; j++) {
                for (int k = yPos - y; k < yPos + y; k++) {
                    img.setRGB(j, k, color.getRGB());
                }
            }

            // only draws circle perimeter.
//            img.setRGB(xPos + x, yPos + y, color.getRGB());
        }
    }
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

            // create an image and initialize it to all 0's


            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i< width; i++) {
                for (int j = 0; j< height; j++) {
                    img.setRGB(i,j,0);
                }
            }

            // YOU NEED TO ADD CODE HERE AT LEAST!
            drawCircle(300, 500, 500, Color.black);




            // Write out the image
            File outputfile = new File("outputimage.png");
            ImageIO.write(img, "png", outputfile);

        } catch (Exception e) {
            System.out.println("ERROR " +e);
            e.printStackTrace();
        }
    }
}
