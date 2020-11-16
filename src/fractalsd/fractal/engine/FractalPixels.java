package fractalsd.fractal.engine;

import fractalsd.fractal.Fractal;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

public class FractalPixels extends Thread {
    Point2D center;
    double windowSize;
    int iteration;
    int sizeX, sizeY;

    BufferedImage imgBuffer;
    Fractal fractal;
    AtomicInteger ticket;

    public FractalPixels(Point2D center, double windowSize, int iteration, int sizeX, int sizeY, BufferedImage imgBuffer, Fractal fractal, AtomicInteger ticket) {
        this.center = center;
        this.windowSize = windowSize;
        this.iteration = iteration;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.imgBuffer = imgBuffer;
        this.fractal = fractal;
        this.ticket = ticket;
    }

    @Override
    public void run() {
        for (int x = ticket.get(); x < sizeX; x = ticket.getAndIncrement()) {
            for (int y = 0; y < sizeY; y++) {
                // convert pixel cords to real world
                double x0 = center.getX() - windowSize / 2 + windowSize * x / sizeY;
                double y0 = center.getY() - windowSize / 2 + windowSize * y / sizeY;
                // get color
                float color = fractal.color(x0, y0, iteration) / (float) iteration;
                // paint pixel
                imgBuffer.setRGB(x, sizeY - 1 - y, Color.HSBtoRGB(1 - color, 1f, color));
            }
        }
    }
}
