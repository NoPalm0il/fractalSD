package fractalsd.fractal;

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
    int size;

    BufferedImage imgbuffer;
    Fractal fractal;

    private GenFractal(int ini, int fin, Point2D center, double windowSize, int iteration, int size, BufferedImage imgbuffer, Fractal fractal) {
        this.ini = ini;
        this.fin = fin;
        this.center = center;
        this.windowSize = windowSize;
        this.iteration = iteration;
        this.size = size;
        this.imgbuffer = imgbuffer;
        this.fractal = fractal;
    }

    public GenFractal(Point2D center, double windowSize, int iteration, int size, Fractal fractal) {
        this.center = center;
        this.windowSize = windowSize;
        this.iteration = iteration;
        this.size = size;
        this.fractal = fractal;
    }

    public void run(){
        for (int x = ini; x < fin; x++) {
            for (int y = 0; y < size; y++) {
                //converter as coordenadas do pixel para o mundo fratal
                double x0 = center.getX() - windowSize / 2 + windowSize * x / size;
                double y0 = center.getY() - windowSize / 2 + windowSize * y / size;
                //calcular a cor
                float color = fractal.color(x0, y0, iteration) / (float) iteration;
                //pintar o pixel
                imgbuffer.setRGB(x, size - 1 - y, Color.HSBtoRGB(1 - color, 1f, color));
            }
        }
    }

    public JLabel getFractalImage() {
        BufferedImage picture = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

        int nCores = Runtime.getRuntime().availableProcessors();
        int dim = size / nCores;

        GenFractal[] tdPool = new GenFractal[nCores];

        for (int i = 0; i < tdPool.length; i++){
            tdPool[i] = new GenFractal(i * dim, (i + 1) * dim, center, windowSize, iteration, size, picture, fractal);
        }

        for (GenFractal pf : tdPool){
            pf.start();
        }
        try {
            for (GenFractal pf : tdPool) {
                pf.join();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return new JLabel(new ImageIcon(picture));
    }
}
