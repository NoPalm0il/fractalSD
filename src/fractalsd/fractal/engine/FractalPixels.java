package fractalsd.fractal.engine;

import fractalsd.fractal.Fractal;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

public class FractalPixels extends Thread {
    private final int ini;
    private final int fin;
    private final Point2D center;
    private final double zoomSize;
    private final int iteration;
    private final int sizeY;
    private final float hueShift, saturationShift, brightnessShift;
    private final boolean isBigDecimal;

    private final BufferedImage imgBuffer;
    private final Fractal fractal;

    public FractalPixels(int ini, int fin, Point2D center, double zoomSize, int iteration, int sizeY, BufferedImage imgBuffer, Fractal fractal, float[] getSliderHSB, boolean isBigDecimal) {
        this.isBigDecimal = isBigDecimal;
        /* TODO: this shit right here, if isBigDecimal is ticked than we need to make double to BigDec
        if(isBigDecimal)
            this.zoomSize = new BigDec();

         */
        this.ini = ini;
        this.fin = fin;
        this.center = center;
        this.zoomSize = zoomSize;
        this.iteration = iteration;
        this.sizeY = sizeY;
        this.imgBuffer = imgBuffer;
        this.fractal = fractal;
        this.hueShift = getSliderHSB[0];
        this.saturationShift = getSliderHSB[1];
        this.brightnessShift = getSliderHSB[2];

    }

    @Override
    public void run() {

        if(isBigDecimal){
            for (int x = ini; x < fin; x++) {
                for (int y = 0; y < sizeY; y++) {
                    // convert pixel coords to real world
                    BigDecimal x0 = BigDecimal.valueOf(center.getX() - zoomSize / 2 + zoomSize * x / sizeY);
                    BigDecimal y0 = BigDecimal.valueOf(center.getY() - zoomSize / 2 + zoomSize * y / sizeY);
                    // get color
                    float color = fractal.color(x0, y0, iteration) / (float) iteration;
                    // paint pixel
                    imgBuffer.setRGB(x, sizeY - 1 - y, Color.HSBtoRGB(hueShift - color, saturationShift, brightnessShift + color));
                }
            }
        } else {
            for (int x = ini; x < fin; x++) {
                for (int y = 0; y < sizeY; y++) {
                    // convert pixel coords to real world
                    double x0 = center.getX() - zoomSize / 2 + zoomSize * x / sizeY;
                    double y0 = center.getY() - zoomSize / 2 + zoomSize * y / sizeY;
                    // get color
                    float color = fractal.color(x0, y0, iteration) / (float) iteration;
                    // paint pixel
                    imgBuffer.setRGB(x, sizeY - 1 - y, Color.HSBtoRGB(1f - color, 1f, 0f + color));
                }
            }
        }
    }
}
