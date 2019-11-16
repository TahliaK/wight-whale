package com.tk.wightwhale.utils;

import com.tk.wightwhale.actors.GameObject;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.image.DataBufferByte;

public class ImageUtils {

    /**
     * Scales an image to the specified size
     * @param image Image to scale
     * @param width target width
     * @param height target height
     * @return Image object at appropriate scale
     */
    public static Image scale(Image image, int width, int height){
        BufferedImage src = toBufferedImage(image);
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gTransform = out.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(
                (double)width/image.getWidth(null),
                (double)height/image.getHeight(null)
        );

        gTransform.drawRenderedImage(src, at);
        return out;
    }

    /**
     * Turns an Image object to a BufferedImage object
     * @param img Image object as input
     * @return BufferedImage version of img
     */
    public static BufferedImage toBufferedImage(Image img){
        BufferedImage bImage;
        if(!(img instanceof BufferedImage)) {
            bImage = new BufferedImage(
                    img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        } else {
            bImage = (BufferedImage) img;
        }

        return bImage;
    }

    /* Credit: https://stackoverflow.com/a/17175454/3252344 */
    private static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    private static final int threshold = 255;

    public static boolean imageCollision(GameObject object1, GameObject object2) {
        boolean collision = false;



        return collision;
    }
}
