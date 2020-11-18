package fractalsd.fractal.engine;

import fractalsd.fractal.Fractal;
import fractalsd.fractal.colors.ColorShifter;
import fractalsd.main.GUIMain;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe que gera o fratal balanceado
 */
public class GenFractal extends SwingWorker<Long, Integer> {
    private final Point2D center;
    private final Object zoomSize;
    private final int iteration;
    private final int sizeX, sizeY;
    private final float[] sliderHSB;
    private final boolean isBigDecimal;
    private final int zoomSizeDecCount;

    private BufferedImage bufferedImage;
    private final Fractal fractal;
    private final GUIMain guiMain;

    /**
     * Tem como parametro um objeto da classe GUIMain
     *
     * @param guiMain - ponteiro para a classe GUIMain para ser possivel acerder a todos os valores
     */
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
        this.zoomSizeDecCount = guiMain.getZoomSizeDecCount();
    }

    // metodo doInBackground() que e executado numa "background thread" e retorna o resultado requerido (neste caso a "bufferedImage")
    @Override
    protected Long doInBackground() throws Exception {
        guiMain.getProgressBar().setMaximum(sizeX);
        // obter o output pela progressBar
        guiMain.getProgressBar().setVisible(true);
        // nova BufferedImage com os parametros:
        //                      width = sizeX
        //                      height = sizeY
        //                      imageType = BufferedImage.TYPE_INT_RGB
        bufferedImage = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);

        AtomicInteger ticket = new AtomicInteger();

        long end, start;
        if (guiMain.getSequentialCheckBox().isSelected()) {
            start = System.currentTimeMillis();
            Thread td = new Thread(new FractalPixels(center, zoomSize, iteration, sizeX, sizeY, bufferedImage, fractal, ticket, sliderHSB, isBigDecimal, zoomSizeDecCount));
            td.start();
            int t;
            while ((t = ticket.get()) < sizeX) {
                publish(t);
                //Thread.sleep(100);
            }
            td.join();
        } else {
            // variavel com o numero de cores
            int nCores = Runtime.getRuntime().availableProcessors();

            // e criada uma thread pool com "nCores" threads
            ExecutorService exe = Executors.newFixedThreadPool(nCores);

            for (int i = 0; i < nCores; i++) {
                exe.execute(new FractalPixels(center, zoomSize, iteration, sizeX, sizeY, bufferedImage, fractal, ticket, sliderHSB, isBigDecimal, zoomSizeDecCount));
            }

            // obriga o ExecutorService a nao aceitar mais tasks novas e espera que as threads acabem o processo para poder terminar
            exe.shutdown();

            start = System.currentTimeMillis();

            int t;
            while ((t = ticket.get()) < sizeX) {
                publish(t);
                Thread.sleep(100);
            }

            exe.awaitTermination(1, TimeUnit.HOURS);
        }
        end = System.currentTimeMillis();

        return end - start;
    }

    public void process(List<Integer> chunks) {
        guiMain.getFractalLabel().setIcon(new ImageIcon(bufferedImage));
        guiMain.getFractalScroll().setViewportView(guiMain.getFractalLabel());
        guiMain.getProgressBar().setValue(chunks.get(chunks.size() - 1));
    }

    public void done() {
        try {
            guiMain.getProgressBar().setVisible(false);
            guiMain.getFractalLabel().setIcon(new ImageIcon(bufferedImage));
            guiMain.getFractalScroll().setViewportView(guiMain.getFractalLabel());
            guiMain.setFractalBufferedImage(bufferedImage);
            guiMain.setPl(new ColorShifter(bufferedImage));
            showInfo(get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // metodo para colocar os outputs das informacoes do Fratal
    private void showInfo(long execTimeMils) {
        guiMain.getInfoTextArea().setText("");
        // cada vez que o metodo e executado, limpamos a string "info" para colocar novas informacoes
        String info = "";

        info += "Fractal Name: " + fractal;
        info += "\n\nMax Iterations: " + iteration;
        info += "\nZoom: " + zoomSize;
        info += "\n\nImage Width: " + sizeX;
        info += "\nImage Height: " + sizeY;
        info += "\n\nHue Value: " + sliderHSB[0];
        info += "\nSaturation Value: " + sliderHSB[1];
        info += "\nBrightness Value: " + sliderHSB[2];
        info += "\n\nExecution Time: " + execTimeMils;

        if (guiMain.getSequentialCheckBox().isSelected())
            info += "\n\n!!!    SEQUENTIAL  ON      !!!";

        // inserimos o valor da variavel "info" na area de "info"
        guiMain.getInfoTextArea().insert(info, 0);
    }
}
