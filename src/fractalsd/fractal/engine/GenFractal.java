package fractalsd.fractal.engine;

import fractalsd.fractal.Fractal;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class GenFractal extends Thread {
    Point2D center;
    double windowSize;
    int iteration;
    int sizeX, sizeY;
    float color;

    BufferedImage picture;
    Fractal fractal;
    JLabel fractalIcon;
    JScrollPane fractalScroll;

    public GenFractal(Point2D center, double windowSize, int iteration, int sizeX, int sizeY, Fractal fractal, JLabel fractalIcon, float color, JScrollPane fractalScroll) {
        this.center = center;
        this.windowSize = windowSize;
        this.iteration = iteration;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.fractal = fractal;
        this.fractalIcon = fractalIcon;
        this.color = color;
        this.fractalScroll = fractalScroll;
    }

    public void run() {
        picture = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);

        int nCores = Runtime.getRuntime().availableProcessors();
        int dim = sizeX / nCores;

        FractalPixels[] tdPool = new FractalPixels[nCores];

        for (int i = 0; i < tdPool.length; i++) {
            tdPool[i] = new FractalPixels(i * dim, (i + 1) * dim, center, windowSize, iteration, sizeX, sizeY, picture, fractal, color);
        }

        for (FractalPixels pf : tdPool) {
            pf.start();
        }
        try {
            for (FractalPixels pf : tdPool) {
                pf.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fractalIcon.setIcon(new ImageIcon(picture));
        fractalScroll.setViewportView(fractalIcon);
    }
}
