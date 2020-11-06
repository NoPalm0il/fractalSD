package fractalsd.main;

import fractalsd.fractal.Fractal;
import fractalsd.fractal.GenFractal;
import fractalsd.fractal.models.Index;

import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

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

    Point2D center;
    double windowSize;
    int iteration;
    int pictureSize;

    public GUIMain() {
        genFractalBt.addActionListener(e -> {
            //fractal center
            center = new Point2D.Double(-0.5, 0);
            //fractal window size
            windowSize = Integer.parseInt(zoomTextField.getText());
            //fractal iterations
            iteration = Integer.parseInt(iterTextField.getText());
            //image w and h in pixels
            pictureSize = 700;

            // TODO: Parallelize this with Swing
            BigCalculus bc = new BigCalculus(progressBar);
            bc.execute();
        });
        fractalLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                center = getRealCoordinates(e.getX(), e.getY(), pictureSize);
                windowSize = Double.parseDouble(zoomTextField.getText()) / 4;

                GenFractal fr =
                        new GenFractal(center, windowSize, iteration, pictureSize, (Fractal) fractalsCombo.getSelectedItem());
                fractalLabel.setIcon(fr.getFractalImage().getIcon());

                zoomTextField.setText("" + windowSize);
            }
        });
        DecimalFormat df = new DecimalFormat("#.####");
        fractalLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
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

            fr = new GenFractal(center, windowSize, iteration, pictureSize, (Fractal) fractalsCombo.getSelectedItem());

            return (double)0;
        }

        public void done(){
            progressBar.setVisible(false);
            fractalLabel.setIcon(fr.getFractalImage().getIcon());
        }
    }
}
