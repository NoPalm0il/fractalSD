package fractalsd.fractal.engine;

import fractalsd.fractal.Fractal;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;
import java.math.BigDecimal;

public class FractalPixels extends Thread {
    private final Point2D center;
    private final Object zoomSize;
    private final int iteration;
    private final int sizeX, sizeY;
    private final boolean isBigDecimal;
    private final float hueShift, saturationShift, brightnessShift;

    BufferedImage imgBuffer;
    Fractal fractal;
    AtomicInteger ticket;

    public FractalPixels(Point2D center, Object zoomSize, int iteration, int sizeX, int sizeY, BufferedImage imgBuffer, Fractal fractal, AtomicInteger ticket, float[] getSliderHSB, boolean isBigDecimal) {
        this.isBigDecimal = isBigDecimal;
        this.center = center;
        this.zoomSize = zoomSize;
        this.iteration = iteration;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.imgBuffer = imgBuffer;
        this.fractal = fractal;
        this.ticket = ticket;
        this.hueShift = getSliderHSB[0];
        this.saturationShift = getSliderHSB[1];
        this.brightnessShift = getSliderHSB[2];
    }

    @Override
    public void run() {
        if(isBigDecimal){
            for (int x = ticket.get(); x < sizeX; x = ticket.getAndIncrement()) {
                for (int y = 0; y < sizeY; y++) {
                    // convert pixel cords to real world
                    BigDecimal x0 = BigDecimal.valueOf(center.getX())
                            .subtract(((BigDecimal) zoomSize).setScale(20, RoundingMode.CEILING)
                                    .divide(new BigDecimal("2"), 20, RoundingMode.CEILING))
                            .add(((BigDecimal) zoomSize).multiply(BigDecimal.valueOf(x)).setScale(20, RoundingMode.CEILING)
                                    .divide(BigDecimal.valueOf(sizeY), 20, RoundingMode.CEILING));

                    BigDecimal y0 = BigDecimal.valueOf(center.getY())
                            .subtract(((BigDecimal) zoomSize).setScale(20, RoundingMode.CEILING)
                                    .divide(new BigDecimal("2"), 20, RoundingMode.CEILING))
                            .add(((BigDecimal) zoomSize).multiply(BigDecimal.valueOf(y)).setScale(20, RoundingMode.CEILING)
                                    .divide(BigDecimal.valueOf(sizeY), 20, RoundingMode.CEILING));
                    // get color
                    float color = fractal.color(x0, y0, iteration) / (float) iteration;
                    // paint pixel
                    imgBuffer.setRGB(x, sizeY - 1 - y, Color.HSBtoRGB(hueShift - color, saturationShift, brightnessShift + color));
                }
            }
        } else {
            for (int x = ticket.get(); x < sizeX; x = ticket.getAndIncrement()) {
                for (int y = 0; y < sizeY; y++) {
                    // convert pixel cords to real world
                    double x0 = center.getX() - (double) zoomSize / 2 + (double) zoomSize * x / sizeY;
                    double y0 = center.getY() - (double) zoomSize / 2 + (double) zoomSize * y / sizeY;
                    // get color
                    float color = fractal.color(x0, y0, iteration) / (float) iteration;
                    // paint pixel
                    imgBuffer.setRGB(x, sizeY - 1 - y, Color.HSBtoRGB(hueShift - color, saturationShift, brightnessShift + color));
                }
            }
        }
    }
}
