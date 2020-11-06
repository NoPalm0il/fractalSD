package fractalsd.main;

import fractalsd.fractal.Fractal;
import fractalsd.fractal.engine.GenFractal;
import fractalsd.fractal.models.Index;

import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
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
            pictureSizeX = 1280;
            pictureSizeY = 720;
            //fractal center
            center = new Point2D.Double(-2.3, 0);

            // TODO: Parallelize this with Swing
            //BigCalculus bc = new BigCalculus(progressBar);
            //bc.execute();

            GenFractal fr = new GenFractal(center, windowSize, iteration, pictureSizeX, pictureSizeY, (Fractal) fractalsCombo.getSelectedItem(), fractalLabel);
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
                    windowSize = Double.parseDouble(zoomTextField.getText()) / 4;
                }
                GenFractal fr = new GenFractal(center, windowSize, iteration, pictureSizeX, pictureSizeY, (Fractal) fractalsCombo.getSelectedItem(), fractalLabel);
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

    private class BigCalculus extends SwingWorker<Double, Integer> {
        JProgressBar progressBar;
        GenFractal fr;

        public BigCalculus (JProgressBar progressBar){
            this.progressBar = progressBar;
        }

        // TODO: doInBackground doesn't return a value, progress bar does not show progress
        @Override
        protected Double doInBackground() {
            progressBar.setVisible(true);

            //fr = new GenFractal(center, windowSize, iteration, pictureSizeX, pictureSizeY, (Fractal) fractalsCombo.getSelectedItem());

            return (double)0;
        }

        public void done(){
            progressBar.setVisible(false);
            //fractalLabel.setIcon(fr.getFractalImage().getIcon());
        }
    }
}
