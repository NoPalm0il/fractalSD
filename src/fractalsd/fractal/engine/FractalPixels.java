package fractalsd.fractal.engine;

import fractalsd.fractal.Fractal;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class FractalPixels extends Thread {
    int ini;
    int fin;
    Point2D center;
    double windowSize;
    int iteration;
    int sizeX, sizeY;

    BufferedImage imgBuffer;
    Fractal fractal;

    public FractalPixels(int ini, int fin, Point2D center, double windowSize, int iteration, int sizeX, int sizeY, BufferedImage imgBuffer, Fractal fractal) {
        this.ini = ini;
        this.fin = fin;
        this.center = center;
        this.windowSize = windowSize;
        this.iteration = iteration;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.imgBuffer = imgBuffer;
        this.fractal = fractal;
    }

    @Override
    public void run() {
        for (int x = ini; x < fin; x++) {
            for (int y = 0; y < sizeY; y++) {
                // convert pixel coords to real world
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
