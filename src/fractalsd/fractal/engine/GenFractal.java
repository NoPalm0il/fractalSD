package fractalsd.fractal.engine;

import fractalsd.fractal.Fractal;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class GenFractal extends Thread{
    int ini;
    int fin;
    Point2D center;
    double windowSize;
    int iteration;
    int sizeX, sizeY;

    BufferedImage picture;
    Fractal fractal;
    JLabel fractalIcon;

    public GenFractal(Point2D center, double windowSize, int iteration, int sizeX, int sizeY, Fractal fractal, JLabel fractalIcon) {
        this.center = center;
        this.windowSize = windowSize;
        this.iteration = iteration;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.fractal = fractal;
        this.fractalIcon = fractalIcon;
    }

    public void run(){
        picture = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);

        int nCores = Runtime.getRuntime().availableProcessors();
        int dim = sizeX / nCores;

        FractalPixels[] tdPool = new FractalPixels[nCores];

        for (int i = 0; i < tdPool.length; i++){
            tdPool[i] = new FractalPixels(i * dim, (i + 1) * dim, center, windowSize, iteration, sizeX, sizeY, picture, fractal);
        }

        for (FractalPixels pf : tdPool){
            pf.start();
        }
        try {
            for (FractalPixels pf : tdPool) {
                pf.join();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        fractalIcon.setIcon(new ImageIcon(picture));
    }
}
