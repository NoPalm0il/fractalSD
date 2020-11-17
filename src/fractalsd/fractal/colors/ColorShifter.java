package fractalsd.fractal.colors;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Classe que recebe como parametro uma BufferedImage, define as shifts de cada valor HSB e altera os pixeis
 */
public class ColorShifter {

    private final int imgX, imgY;
    private final int[][] colorMap;
    private final BufferedImage image;
    private float hueShift, saturationShift, brightnessShift;

    public ColorShifter(BufferedImage image) {
        this.image = image;
        this.imgX = image.getWidth();
        this.imgY = image.getHeight();
        this.colorMap = new int[imgX][imgY];

        genColorMap();
    }

    private void genColorMap() {
        for (int x = 0; x < imgX; x++) {
            for (int y = 0; y < imgY; y++) {
                colorMap[x][y] = image.getRGB(x, y);
            }
        }
    }

    public BufferedImage genNewColorMap(){
        int red, green, blue;
        for (int x = 0; x < imgX; x++) {
            for (int y = 0; y < imgY; y++) {
                red = new Color(colorMap[x][y]).getRed();
                green = new Color(colorMap[x][y]).getGreen();
                blue = new Color(colorMap[x][y]).getBlue();

                float[] clHsb = Color.RGBtoHSB(red, green, blue, null);

                image.setRGB(x, y, Color.HSBtoRGB(clHsb[0] + hueShift, clHsb[1] + saturationShift, clHsb[2] + brightnessShift));
            }
        }

        return image;
    }

    public void setHueShift(float hueShift) {
        this.hueShift = hueShift;
    }

    public void setSaturationShift(float saturationShift) {
        this.saturationShift = saturationShift;
    }

    public void setBrightnessShift(float brightnessShift) {
        this.brightnessShift = brightnessShift;
    }
}
