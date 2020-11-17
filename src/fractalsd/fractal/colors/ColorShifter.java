package fractalsd.fractal.colors;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Classe que recebe como parametro uma BufferedImage, define as shifts de cada valor HSB e altera os pixeis do Fratal
 * Hue sendo a tonalidade de todas as cores do espetro;
 * Saturation sendo a intensidade de uma Hue desde um tom acinzentado (sem saturacao) até uma cor vivida e "pura" (com saturacao)
 * Brightness sendo a relatividade entre a presença (ou não) de luz mais intensa
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

    /**
     * constroi se um array bidimensional com os pixeis que compoem a BufferedImage
     */
    private void genColorMap() {
        for (int x = 0; x < imgX; x++) {
            for (int y = 0; y < imgY; y++) {
                colorMap[x][y] = image.getRGB(x, y);
            }
        }
    }

    /**
     * percorre se a imagem para obter os componentes RED, GREEN e BLUE e estes são guardados em variaveis apropriadas
     * ex: getRed() - retorna o componente vermelho, num intervalo de 0 - 255, no espaco RGB
     * cria se um array de floats com as conversoes RGB para HSB das cores RED, GREEN e BLUE
     * cada pixel da imagem sera pintado conforme os parametros do metodo setRGB()
     * @return a BufferedImage
     */
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
