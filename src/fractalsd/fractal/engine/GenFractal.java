package fractalsd.fractal.engine;

import fractalsd.fractal.Fractal;
import fractalsd.fractal.colors.ColorShifter;
import fractalsd.main.GUIMain;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class GenFractal extends Thread {
    Point2D center;
    double windowSize;
    int iteration;
    int sizeX, sizeY;
    float[] sliderHSB;

    BufferedImage bufferedImage;
    Fractal fractal;
    GUIMain guiMain;

    public GenFractal(Point2D center, double windowSize, int iteration, int sizeX, int sizeY, Fractal fractal, GUIMain guiMain, float[] sliderHSB) {
        this.center = center;
        this.windowSize = windowSize;
        this.iteration = iteration;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.fractal = fractal;
        this.guiMain = guiMain;
        this.sliderHSB = sliderHSB;
    }

    public void run() {
        bufferedImage = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);

        int nCores = Runtime.getRuntime().availableProcessors();
        int dim = sizeX / nCores;

        FractalPixels[] tdPool = new FractalPixels[nCores];

        for (int i = 0; i < tdPool.length; i++) {
            tdPool[i] = new FractalPixels(i * dim, (i + 1) * dim, center, windowSize, iteration, sizeX, sizeY, bufferedImage, fractal, sliderHSB);
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

        guiMain.getFractalLabel().setIcon(new ImageIcon(bufferedImage));
        guiMain.setFractalBufferedImage(bufferedImage);
        guiMain.setPl(new ColorShifter(bufferedImage));
    }
}
