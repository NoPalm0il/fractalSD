package fractalsd.fractal.engine;

import fractalsd.fractal.Fractal;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class FractalPixels extends Thread{
    int ini;
    int fin;
    Point2D center;
    double windowSize;
    int iteration;
    int sizeX, sizeY;

    BufferedImage imgbuffer;
    Fractal fractal;

    public FractalPixels(int ini, int fin, Point2D center, double windowSize, int iteration, int sizeX, int sizeY, BufferedImage imgbuffer, Fractal fractal) {
        this.ini = ini;
        this.fin = fin;
        this.center = center;
        this.windowSize = windowSize;
        this.iteration = iteration;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.imgbuffer = imgbuffer;
        this.fractal = fractal;
    }

    @Override
    public void run() {
        for (int x = ini; x < fin; x++) {
            for (int y = 0; y < sizeY; y++) {
                //converter as coordenadas do pixel para o mundo fratal
                double x0 = center.getX() - windowSize / 2 + windowSize * x / sizeY;
                double y0 = center.getY() - windowSize / 2 + windowSize * y / sizeY;
                //calcular a cor
                float color = fractal.color(x0, y0, iteration) / (float) iteration;
                //pintar o pixel
                imgbuffer.setRGB(x, sizeY - 1 - y, Color.HSBtoRGB(1 - color, 1f, color));
            }
        }
    }
}
