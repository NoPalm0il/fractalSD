package fractalsd.fractal;

import fractalsd.fractal.models.Mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class GenFractal implements Runnable {

    private Fractal fractal;
    public JLabel label;
    private Point2D center;
    private double windowSize;
    private int iteration;
    private int size;

    public GenFractal(Fractal fractal, JLabel label, Point2D center, double windowSize, int iteration, int size) {
        this.fractal = fractal;
        this.label = label;
        this.center = center;
        this.windowSize = windowSize;
        this.iteration = iteration;
        this.size = size;
    }

    @Override
    public void run() {
        label.setIcon(getFractalImage().getIcon());
    }

    private JLabel getFractalImage() {
        //criar a imagem com dimensão size x size do tipo RGB
        BufferedImage picture = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        //calcular os pixeis da imagem
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                //converter as coordenadas do pixel para o mundo fratal
                double x0 = center.getX() - windowSize / 2 + windowSize * x / size;
                double y0 = center.getY() - windowSize / 2 + windowSize * y / size;
                //calcular a cor
                float color = fractal.color(x0, y0, iteration) / (float) iteration;
                //pintar o pixel
                picture.setRGB(x, size - 1 - y, Color.HSBtoRGB(1 - color, 1f, color));
            }
        }
        return new JLabel(new ImageIcon(picture));
    }
}
