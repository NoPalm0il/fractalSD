package fractalsd.main;

import fractalsd.fractal.Fractal;
import fractalsd.fractal.engine.GenFractal;
import fractalsd.fractal.models.Index;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GUIMain {
    private JPanel mainPanel;
    private JPanel fractalPanel;
    private JLabel fractalLabel;
    private JButton genFractalBt;
    private JComboBox fractalsCombo;
    private JLabel coordsLabel;
    private JTextField zoomTextField;
    private JTextField iterTextField;
    private JProgressBar progressBar;
    private JTabbedPane tabbedPane1;
    private JPanel fractalTabbedPanel;
    private JPanel colorTabbedPanel;
    private JComboBox colorComboBox;
    private JPanel mainJPanel;
    private JPanel colorJPanel;
    private JScrollPane fractalScroll;
    private JTextField xTextField;
    private JTextField yTextField;
    private JTextField centerXtextField;
    private JTextField centerYtextField;

    private Point2D center;
    private double windowSize;
    private int iteration;
    private int pictureSizeX;
    private int pictureSizeY;

    public GUIMain() {
        genFractalBt.addActionListener(e -> {
            //fractal window size
            windowSize = Double.parseDouble(zoomTextField.getText());
            //fractal iterations
            iteration = Integer.parseInt(iterTextField.getText());
            //image w and h in pixels
            //resolution
            pictureSizeX = Integer.parseInt(xTextField.getText());
            pictureSizeY = Integer.parseInt(yTextField.getText());
            //pictureSizeX = 400;
            //pictureSizeY = 400;

            center = new Point2D.Double(Double.parseDouble(centerXtextField.getText()), Double.parseDouble(centerYtextField.getText()));

            //center = new Point2D.Double(0, 0);

            // dynamic fractal center
            center = getRealCoordinates(pictureSizeX / 1.0 / pictureSizeY / 2.0, pictureSizeY / 2.0, pictureSizeY);

            // TODO: Parallelize with SwingWorker

            GenFractal fr = new GenFractal(center, windowSize, iteration, pictureSizeX, pictureSizeY, (Fractal) fractalsCombo.getSelectedItem(), fractalLabel, (float) colorComboBox.getSelectedItem(), fractalScroll);
            fr.start();
        });

        fractalLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getButton() == MouseEvent.BUTTON1) {
                    center = getRealCoordinates(e.getX(), e.getY(), pictureSizeY);
                    windowSize = Double.parseDouble(zoomTextField.getText()) / 4;
                }
                else if(e.getButton() == MouseEvent.BUTTON3) {
                    center = getRealCoordinates(e.getX(), e.getY(), pictureSizeY);
                    windowSize = Double.parseDouble(zoomTextField.getText()) * 4;
                }

                GenFractal fr = new GenFractal(center, windowSize, iteration, pictureSizeX, pictureSizeY, (Fractal) fractalsCombo.getSelectedItem(), fractalLabel, (float) colorComboBox.getSelectedItem(), fractalScroll);
                fr.start();

                zoomTextField.setText("" + windowSize);
            }
        });

        DecimalFormat df = new DecimalFormat("#.####");
        fractalLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                df.setRoundingMode(RoundingMode.CEILING);
                Point2D mouseRealCoord = getRealCoordinates(e.getX(), e.getY(), pictureSizeY);
                coordsLabel.setText("x: " + df.format(mouseRealCoord.getX()) + " y: " + df.format(mouseRealCoord.getY()));
                try {
                    Thread.sleep(30);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });

        fractalsCombo.addItemListener(e -> {
            zoomTextField.setText("5");
            iterTextField.setText("1024");
            xTextField.setText("400");
            yTextField.setText("400");
            centerXtextField.setText("0");
            centerYtextField.setText("0");
        });
    }

    private void createUIComponents() {
        fractalLabel = new JLabel();
        coordsLabel = new JLabel();
        zoomTextField = new JTextField();
        iterTextField = new JTextField();
        progressBar = new JProgressBar();
        fractalScroll = new JScrollPane();

        fractalsCombo = new JComboBox();
        Index idx = new Index();
        for (Fractal f : idx.fractals)
            fractalsCombo.addItem(f);

        colorComboBox = new JComboBox();
        ArrayList<Float> colors = new ArrayList();
        colors.add((float) 0.9);
        colors.add((float) 0.7);
        colors.add((float) 0.5);
        colors.add((float) 0.3);
        colors.add((float) 0.1);
        for (Float c : colors)
            colorComboBox.addItem(c);



        //progressBar.setVisible(false);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    // method to retrieve real coords from label
    public Point2D getRealCoordinates(double xx, double yy, int size) {
        double ws = Double.parseDouble(zoomTextField.getText());
        double pixelSize = ws / size;
        double minX = center.getX() - ws / 2;
        double miny = center.getY() + ws / 2;
        double x = minX + pixelSize * xx;
        double y = miny - pixelSize * yy;
        return new Point2D.Double(x, y);
    }
}
