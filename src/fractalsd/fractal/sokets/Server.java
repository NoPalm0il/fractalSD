package fractalsd.fractal.sokets;

import fractalsd.fractal.Fractal;
import fractalsd.fractal.engine.FractalPixels;
import fractalsd.fractal.engine.GenFractal;
import fractalsd.gui.GUIMain;
import fractalsd.utils.*;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends SwingWorker<Long, Integer> {
    Socket socket;
    final String ip;
    final int port;
    final GUIMain guiMain;

    public Server(String ip, int port, GUIMain guiMain) {
        this.ip = ip;
        this.port = port;
        this.guiMain = guiMain;
    }

    @Override
    protected Long doInBackground() {
        try {
            // TODO: RECEBER ISTO POR PARAMETRO DO BALANCEADOR
            BufferedImage bufferedImage = new BufferedImage(guiMain.getPictureSizeX(), guiMain.getPictureSizeY(), BufferedImage.TYPE_INT_RGB);

            AtomicInteger ticket = new AtomicInteger();
            int nCores = Runtime.getRuntime().availableProcessors();

            // e criada uma thread pool com "nCores" threads
            ExecutorService exe = Executors.newFixedThreadPool(nCores);

            for (int i = 0; i < nCores; i++) {
                // TODO: RECEBER ISTO POR PARAMETRO A PARTIR DO BALANCEADOR
                exe.execute(new FractalPixels(
                        guiMain.getCenter(),
                        guiMain.getZoomSize(),
                        guiMain.getIteration(),
                        guiMain.getPictureSizeX(),
                        guiMain.getPictureSizeY(),
                        bufferedImage,
                        (Fractal) guiMain.getFractalsCombo().getSelectedItem(),
                        ticket,
                        guiMain.getSliderHSB(),
                        guiMain.getBigDecCheckBox().isSelected(),
                        guiMain.getZoomSizeDecCount()));
            }

            // obriga o ExecutorService a nao aceitar mais tasks novas e espera que as threads acabem o processo para poder terminar
            exe.shutdown();
            exe.awaitTermination(5, TimeUnit.MINUTES);

            socket = new Socket(ip, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject('s');
            oos.writeObject(ColorUtils.imageToColorArray(bufferedImage));
        } catch (IOException | InterruptedException io) {
            io.printStackTrace();
        }
        return null;
    }

}
