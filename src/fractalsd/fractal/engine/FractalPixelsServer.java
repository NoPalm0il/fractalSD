package fractalsd.fractal.engine;

import fractalsd.fractal.Fractal;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

public class FractalPixelsServer extends FractalPixels {

    private final int max;

    public FractalPixelsServer(Point2D center, Object zoomSize, int iteration, int sizeX, int sizeY, BufferedImage imgBuffer, Fractal fractal, AtomicInteger ticket, int max) {
        super(center, zoomSize, iteration, sizeX, sizeY, imgBuffer, fractal, ticket, new float[]{1.0f, 1.0f, 0.0f}, false, 0);
        this.max = max;
    }

    @Override
    public void run() {
        for (int x = ticket.get(); x < max; x = ticket.getAndIncrement()) {
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
