package fractalsd.fractal.engine;

import fractalsd.fractal.Fractal;
import fractalsd.fractal.colors.ColorShifter;
import fractalsd.main.GUIMain;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class GenFractal extends Thread {
    private final Point2D center;
    private final double zoomSize;
    private final int iteration;
    private final int sizeX, sizeY;
    private final float[] sliderHSB;
    private final boolean isBigDecimal;

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

    public void run() {
        BufferedImage bufferedImage = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);

        int nCores = Runtime.getRuntime().availableProcessors();
        int dim = sizeX / nCores;

        FractalPixels[] tdPool = new FractalPixels[nCores];

        for (int i = 0; i < tdPool.length; i++) {
            tdPool[i] = new FractalPixels(i * dim, (i + 1) * dim, center, zoomSize, iteration, sizeY, bufferedImage, fractal, sliderHSB, isBigDecimal);
        }

        for (FractalPixels pf : tdPool) {
            pf.start();
        }
        try {
            for (FractalPixels pf : tdPool) {
                pf.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        guiMain.getFractalLabel().setIcon(new ImageIcon(bufferedImage));
        guiMain.getFractalScroll().setViewportView(guiMain.getFractalLabel());
        guiMain.setFractalBufferedImage(bufferedImage);
        guiMain.setPl(new ColorShifter(bufferedImage));
    }
}
