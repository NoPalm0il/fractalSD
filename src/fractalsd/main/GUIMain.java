package fractalsd.main;

import fractalsd.fractal.Fractal;
import fractalsd.fractal.colors.ColorShifter;
import fractalsd.fractal.engine.GenFractal;
import fractalsd.fractal.models.Index;

import javax.swing.*;
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
    private JLabel coordsLabel;
    private JTextField zoomTextField;
    private JTextField iterTextField;
    private JProgressBar progressBar;
    private JSlider brightnessSlider;
    private JSlider saturationSlider;
    private JSlider hueSlider;

    private Point2D center;
    private double windowSize;
    private int iteration;
    private int pictureSizeX;
    private int pictureSizeY;
    private BufferedImage fractalBufferedImage;
    ColorShifter pl;
    GenFractal fr;

    public GUIMain() {
        genFractalBt.addActionListener(e -> {
            //fractal window size
            windowSize = Double.parseDouble(zoomTextField.getText());
            //fractal iterations
            iteration = Integer.parseInt(iterTextField.getText());
            //image w and h in pixels
            pictureSizeX = 600;
            pictureSizeY = 600;

            center = new Point2D.Double(-0.5, 0);

            // dynamic fractal center
            //center = getRealCoordinates(pictureSizeX / 2.0 / pictureSizeY, pictureSizeY / 2.0, pictureSizeY);

            // TODO: Parallelize with SwingWorker

            fr = new GenFractal(center, windowSize, iteration, pictureSizeX, pictureSizeY, (Fractal) fractalsCombo.getSelectedItem(), this, getSliderHSB());
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

                fr = new GenFractal(center, windowSize, iteration, pictureSizeX, pictureSizeY, (Fractal) fractalsCombo.getSelectedItem(), GUIMain.this, getSliderHSB());
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
        });

        hueSlider.addChangeListener(evt -> {
            if(fractalBufferedImage != null && pl != null) {
                pl.setHueShift(hueSlider.getValue() / 100f);
                fractalLabel.setIcon(new ImageIcon(pl.genNewColorMap()));
            }
        });
        saturationSlider.addChangeListener(evt -> {
            if(fractalBufferedImage != null && pl != null) {
                pl.setSaturationShift(saturationSlider.getValue() / 100f);
                fractalLabel.setIcon(new ImageIcon(pl.genNewColorMap()));
            }
        });
        brightnessSlider.addChangeListener(evt -> {
            if(fractalBufferedImage != null && pl != null) {
                pl.setBrightnessShift(brightnessSlider.getValue() / 100f);
                fractalLabel.setIcon(new ImageIcon(pl.genNewColorMap()));
            }
        });
    }

    private void createUIComponents() {
        fractalLabel = new JLabel();
        coordsLabel = new JLabel();
        zoomTextField = new JTextField();
        iterTextField = new JTextField();
        progressBar = new JProgressBar();

        fractalsCombo = new JComboBox();
        Index idx = new Index();
        for (Fractal f : idx.fractals)
            fractalsCombo.addItem(f);

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

    private float[] getSliderHSB(){
        return new float[]{(float) hueSlider.getValue()/100, (float) saturationSlider.getValue()/100, (float) brightnessSlider.getValue()/100};
    }

    public JLabel getFractalLabel() {
        return fractalLabel;
    }

    public void setFractalBufferedImage(BufferedImage fractalBufferedImage) {
        this.fractalBufferedImage = fractalBufferedImage;
    }

    public void setPl(ColorShifter pl) {
        this.pl = pl;
    }
}
