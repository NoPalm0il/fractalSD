package fractalsd.utils;

import java.awt.image.BufferedImage;

public class ColorUtils {

    public static int[][] imageToColorArray(BufferedImage image) {
        int[][] buffer = new int[image.getHeight()][image.getWidth()];
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                buffer[y][x] = image.getRGB(x, y);
            }
        }
        return buffer;
    }

    public static BufferedImage colorArrayToImage(int[][] buffer) {
        BufferedImage image = new BufferedImage(buffer[0].length, buffer.length, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                image.setRGB(x, y, buffer[y][x]);
            }
        }
        return image;
    }
}
