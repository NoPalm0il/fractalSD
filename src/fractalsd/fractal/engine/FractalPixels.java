package fractalsd.fractal.engine;

import fractalsd.fractal.Fractal;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;
import java.math.BigDecimal;

/**
 * Esta classe serve apenas para gerar os pixeis e preencher a BufferedImage
 * <p>
 * Verifica se a boolean isBigDecimal está ativa, em caso afirmativo realiza os calculos em BigDecimal
 */
public class FractalPixels extends Thread {
    protected final Point2D center;
    protected final Object zoomSize;
    protected final int iteration;
    protected final int sizeX, sizeY;
    protected final boolean isBigDecimal;
    protected final float hueShift, saturationShift, brightnessShift;
    protected final int zoomSizeDecCount;

    BufferedImage imgBuffer;
    Fractal fractal;
    AtomicInteger ticket;

    // construtor da classe
    public FractalPixels(Point2D center, Object zoomSize, int iteration, int sizeX, int sizeY, BufferedImage imgBuffer, Fractal fractal, AtomicInteger ticket, float[] getSliderHSB, boolean isBigDecimal, int zoomSizeDecCount) {
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
        this.zoomSizeDecCount = Math.max(zoomSizeDecCount, 15);
    }

    @Override
    public void run() {
        // executa a condicao para verficar se o calculo vai ser feito com "doubles" ou "BigDecimals"
        if(isBigDecimal){
            // processo de balanceamento com a ajuda dos AtomicInteger "ticket"
            for (int x = ticket.get(); x < sizeX; x = ticket.getAndIncrement()) {
                for (int y = 0; y < sizeY; y++) {
                    // conversao das coordenadas dos pixeis para mundo real
                    BigDecimal x0 = BigDecimal.valueOf(center.getX())
                            .subtract(((BigDecimal) zoomSize).setScale(zoomSizeDecCount, RoundingMode.CEILING)
                                    .divide(new BigDecimal("2"), zoomSizeDecCount, RoundingMode.CEILING))
                            .add(((BigDecimal) zoomSize).multiply(BigDecimal.valueOf(x)).setScale(zoomSizeDecCount, RoundingMode.CEILING)
                                    .divide(BigDecimal.valueOf(sizeY), zoomSizeDecCount, RoundingMode.CEILING));

                    BigDecimal y0 = BigDecimal.valueOf(center.getY())
                            .subtract(((BigDecimal) zoomSize).setScale(zoomSizeDecCount, RoundingMode.CEILING)
                                    .divide(new BigDecimal("2"), zoomSizeDecCount, RoundingMode.CEILING))
                            .add(((BigDecimal) zoomSize).multiply(BigDecimal.valueOf(y)).setScale(zoomSizeDecCount, RoundingMode.CEILING)
                                    .divide(BigDecimal.valueOf(sizeY), zoomSizeDecCount, RoundingMode.CEILING));
                    // get color
                    float color = fractal.color(x0, y0, iteration, zoomSizeDecCount) / (float) iteration;
                    // paint pixel
                    imgBuffer.setRGB(x, sizeY - 1 - y, Color.HSBtoRGB(hueShift - color, saturationShift, brightnessShift + color));
                }
            }
        }
        // caso o processo seja feito com valores double:
        else {
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
