package fractalsd.main;

import fractalsd.fractal.Fractal;
import fractalsd.fractal.GenFractal;
import fractalsd.fractal.models.Index;
import fractalsd.fractal.models.Mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class GUIMain {
    private JPanel mainPanel;
    private JPanel fractalPanel;
    private JLabel fractalLabel;
    private JButton genFractalBt;
    private JComboBox fractalsCombo;
    private JLabel zoomLabel;
    private JLabel coordsLabel;

    Point2D center;
    double windowSize;
    int iteration;
    int pictureSize;

    public GUIMain() {
        genFractalBt.addActionListener(e -> {
            //fractal center
            center = new Point2D.Double(-0.5, 0);
            //fractal window size
            windowSize = 5;
            //fractal iterations
            iteration = 1024;
            //image w and h in pixels
            pictureSize = 600;

            //generate fractal icon on a separated thread
            Thread td = new Thread(new GenFractal((Fractal) fractalsCombo.getSelectedItem(), fractalLabel,
                    center, Double.parseDouble(zoomLabel.getText()), iteration, pictureSize));
            td.start();
        });
        fractalLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                center = getRealCoordinates(e.getX(), e.getY(), pictureSize);
                windowSize = Double.parseDouble(zoomLabel.getText()) / 4;
                Thread td = new Thread(new GenFractal((Fractal) fractalsCombo.getSelectedItem(), fractalLabel,
                        center, windowSize, iteration, pictureSize));
                td.start();
                zoomLabel.setText("" + windowSize);
            }
        });
        fractalLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                DecimalFormat df = new DecimalFormat("#.####");
                df.setRoundingMode(RoundingMode.CEILING);
                Point2D mouseRealCoord = getRealCoordinates(e.getX(), e.getY(), pictureSize);
                coordsLabel.setText("x: " + df.format(mouseRealCoord.getX()) + " y: " + df.format(mouseRealCoord.getY()));
                try {
                    Thread.sleep(30);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        fractalLabel = new JLabel();
        zoomLabel = new JLabel();
        coordsLabel = new JLabel();

        fractalsCombo = new JComboBox();
        Index idx = new Index();
        for (Fractal f : idx.fractals)
            fractalsCombo.addItem(f);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    //metodo para obter as coordenadas reais a partir da jlabel fratal
    public Point2D getRealCoordinates(double xx, double yy, int size) {
        double ws = Double.parseDouble(zoomLabel.getText());
        double pixelSize = ws / size;
        double minX = center.getX() - ws / 2;
        double miny = center.getY() + ws / 2;
        double x = minX + pixelSize * xx;
        double y = miny - pixelSize * yy;
        return new Point2D.Double(x, y);
    }
}
