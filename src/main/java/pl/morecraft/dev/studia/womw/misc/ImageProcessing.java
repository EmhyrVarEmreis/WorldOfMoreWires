package pl.morecraft.dev.studia.womw.misc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ImageProcessing {

    public static double getColorDistance(Color e1, Color e2) {
        long rmean = ((long) e1.getRed() + (long) e2.getRed()) / 2;
        long r = (long) e1.getRed() - (long) e2.getRed();
        long g = (long) e1.getGreen() - (long) e2.getGreen();
        long b = (long) e1.getBlue() - (long) e2.getBlue();
        return Math.sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8));
    }

    public static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height * width][3];

        System.out.println(result.length);

        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, i = 0; pixel < pixels.length; pixel += pixelLength) {
                // int argb = 0;
                // argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                // argb += ((int) pixels[pixel + 1] & 0xff); // blue
                // argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                // argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[i][0] = (((int) pixels[pixel + 3] & 0xff) << 16);
                result[i][1] = (((int) pixels[pixel + 2] & 0xff) << 8);
                result[i][2] = ((int) pixels[pixel + 1] & 0xff);
                i++;
            }
        } else {
            //int pixel = 0;
            int i = 0;
            for (int x = 0; x < image.getWidth(); x++)
                for (int y = 0; y < image.getHeight(); y++) {
                    //pixel = image.getRGB( x, y );
                    // argb += -16777216; // 255 alpha
                    // argb += ((int) pixels[pixel] & 0xff); // blue
                    // argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                    // argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                    Color c = new Color(image.getRGB(x, y));
                    result[i][0] = c.getRed();
                    result[i][1] = c.getGreen();
                    result[i][2] = c.getBlue();
                    System.out.println("C: " + result[i][0] + " " + result[i][1] + " " + result[i][2]);
                    i++;
                }
        }
        return result;
    }

}
