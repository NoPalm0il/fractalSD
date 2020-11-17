package fractalsd.fractal.engine;

import fractalsd.fractal.Fractal;
import fractalsd.fractal.colors.ColorShifter;
import fractalsd.main.GUIMain;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GenFractal extends SwingWorker<BufferedImage, BufferedImage> {
    private final Point2D center;
    private final Object zoomSize;
    private final int iteration;
    private final int sizeX, sizeY;
    private final float[] sliderHSB;
    private final boolean isBigDecimal;

    private BufferedImage bufferedImage;
    private final Fractal fractal;
    private final GUIMain guiMain;

    public GenFractal(GUIMain guiMain) {
        this.guiMain = guiMain;
        this.center = guiMain.getCenter();
        this.zoomSize = guiMain.getZoomSize();
        this.iteration = guiMain.getIteration();
        this.sizeX = guiMain.getPictureSizeX();
        this.sizeY = guiMain.getPictureSizeY();
        this.fractal = (Fractal) guiMain.getFractalsCombo().getSelectedItem();
        this.sliderHSB = guiMain.getSliderHSB();
        this.isBigDecimal = guiMain.getBigDecCheckBox().isSelected();
    }

    @Override
    protected BufferedImage doInBackground() throws Exception {
        //progressBar.setVisible(true);
        bufferedImage = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);

        int nCores = Runtime.getRuntime().availableProcessors();

        ExecutorService exe = Executors.newFixedThreadPool(nCores);
        AtomicInteger ticket = new AtomicInteger();

        for (int i = 0; i < nCores; i++) {
            exe.execute(new FractalPixels(center, zoomSize, iteration, sizeX, sizeY, bufferedImage, fractal, ticket, sliderHSB, isBigDecimal));
        }

        exe.shutdown();
        exe.awaitTermination(1, TimeUnit.HOURS);

        return bufferedImage;
    }

    public void process(List<BufferedImage> chunks) {
        //progressBar.setValue(chunks.get(chunks.size() - 1));
    }

    public void done() {
        guiMain.getFractalLabel().setIcon(new ImageIcon(bufferedImage));
        guiMain.getFractalScroll().setViewportView(guiMain.getFractalLabel());
        guiMain.setFractalBufferedImage(bufferedImage);
        guiMain.setPl(new ColorShifter(bufferedImage));
        //progressBar.setVisible(false);
    }
}
