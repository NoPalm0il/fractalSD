package fractalsd.fractal.engine;

import fractalsd.fractal.Fractal;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GenFractal extends SwingWorker<BufferedImage, BufferedImage> {
    Point2D center;
    double windowZoom;
    int iteration;
    int sizeX, sizeY;

    BufferedImage picture;
    Fractal fractal;
    JLabel fractalIcon;
    JProgressBar progressBar;

    /**
     * Executes all threads to generate the fractal, works with the class SwingWorker
     *
     * @param center      fractal center
     * @param windowZoom  fractal zoom
     * @param iteration   number of iterations per pixel
     * @param sizeX       width
     * @param sizeY       height
     * @param fractal     type of Fractal
     * @param fractalIcon receive JLabel to set the icon
     * @param progressBar shows progress
     */
    public GenFractal(Point2D center, double windowZoom, int iteration, int sizeX, int sizeY, Fractal fractal, JLabel fractalIcon, JProgressBar progressBar) {
        this.center = center;
        this.windowZoom = windowZoom;
        this.iteration = iteration;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.fractal = fractal;
        this.fractalIcon = fractalIcon;
        this.progressBar = progressBar;
    }

    @Override
    protected BufferedImage doInBackground() throws Exception {
        progressBar.setVisible(true);
        picture = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);

        int nCores = Runtime.getRuntime().availableProcessors();

        ExecutorService exe = Executors.newFixedThreadPool(nCores);
        AtomicInteger ticket = new AtomicInteger();

        for (int i = 0; i < nCores; i++) {
            exe.execute(new FractalPixels(center, windowZoom, iteration, sizeX, sizeY, picture, fractal, ticket));
        }

        exe.shutdown();
        exe.awaitTermination(1, TimeUnit.HOURS);

        return picture;
    }

    public void process(List<BufferedImage> chunks) {
        //progressBar.setValue(chunks.get(chunks.size() - 1));
    }

    public void done() {
        try {
            fractalIcon.setIcon(new ImageIcon(get()));
            progressBar.setVisible(false);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
