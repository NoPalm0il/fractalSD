package fractalsd.sokets;

import fractalsd.fractal.engine.FractalPixels;
import fractalsd.fractal.models.Mandelbrot;
import fractalsd.gui.GUIMain;
import fractalsd.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends SwingWorker<Long, Integer> {
    private Socket balcSocket;
    private final String ip;
    private final int port;
    private final GUIMain guiMain;

    public Server(String ip, int port, GUIMain guiMain) {
        this.ip = ip;
        this.port = port;
        this.guiMain = guiMain;
    }

    @Override
    protected Long doInBackground() {
        try {
            while (guiMain.getServerRunning().get()) {
                balcSocket = new Socket(ip, port);

                DataOutputStream balcDos = new DataOutputStream(balcSocket.getOutputStream());
                DataInputStream balcDis = new DataInputStream(balcSocket.getInputStream());

                balcDos.writeChar('s');

                String[] fractalParams = balcDis.readUTF().split(" ");

                Point2D center = new Point2D.Double(Double.parseDouble(fractalParams[0]), Double.parseDouble(fractalParams[1]));
                double zoom = Double.parseDouble(fractalParams[2]);
                int iterations = Integer.parseInt(fractalParams[3]);
                int dimX = Integer.parseInt(fractalParams[4]);
                int dimY = Integer.parseInt(fractalParams[5]);

                BufferedImage bufferedImage = new BufferedImage(dimX, dimY, BufferedImage.TYPE_INT_RGB);

                AtomicInteger ticket = new AtomicInteger();
                // e criada uma thread pool com "nCores" threads
                // int nCores = Runtime.getRuntime().availableProcessors();
                // ExecutorService exe = Executors.newFixedThreadPool(guiMain.getSequentialCheckBox().isSelected() ? 1 : nCores);
                ExecutorService exe = Executors.newFixedThreadPool(1);

                for (int i = 0; i < 1; i++) {
                    exe.execute(new FractalPixels(
                            center,
                            zoom,
                            iterations,
                            dimX,
                            dimY,
                            bufferedImage,
                            new Mandelbrot(),
                            ticket,
                            guiMain.getSliderHSB(),
                            /*guiMain.getBigDecCheckBox().isSelected()*/ false,
                            guiMain.getZoomSizeDecCount()));
                }

                // obriga o ExecutorService a nao aceitar mais tasks novas e espera que as threads acabem o processo para poder terminar
                exe.shutdown();
                exe.awaitTermination(5, TimeUnit.MINUTES);

                //balcDos.writeObject(ImageUtils.imageToColorArray(bufferedImage));
                byte[] buffer = ImageUtils.imageToByteArray(bufferedImage);
                balcDos.write(buffer);
                balcSocket.close();
            }
            balcSocket.close();
        } catch (IOException | InterruptedException io) {
            io.printStackTrace();
            guiMain.onStop();
        }
        guiMain.onStop();
        return null;
    }
}