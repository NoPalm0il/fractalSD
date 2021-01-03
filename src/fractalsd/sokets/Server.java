package fractalsd.sokets;

import fractalsd.fractal.Fractal;
import fractalsd.fractal.engine.FractalPixelsServer;
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
                guiMain.onDisplay(Color.GREEN, "Connected to balancer");

                DataOutputStream balcDos = new DataOutputStream(balcSocket.getOutputStream());
                DataInputStream balcDis = new DataInputStream(balcSocket.getInputStream());

                balcDos.writeChar('s');

                String[] fractalParams = balcDis.readUTF().split(" ");
                guiMain.onDisplay(Color.GREEN, "Received params");

                Point2D center = new Point2D.Double(Double.parseDouble(fractalParams[0]), Double.parseDouble(fractalParams[1]));
                double zoom = Double.parseDouble(fractalParams[2]);
                int iterations = Integer.parseInt(fractalParams[3]);
                int dimX = Integer.parseInt(fractalParams[4]);
                int dimY = Integer.parseInt(fractalParams[5]);
                int start = Integer.parseInt(fractalParams[6]);
                int end = Integer.parseInt(fractalParams[7]);

                BufferedImage bufferedImage = new BufferedImage(end - start, dimY, BufferedImage.TYPE_INT_RGB);

                AtomicInteger ticket = new AtomicInteger(start);
                // e criada uma thread pool com "nCores" threads
                int nCores = Runtime.getRuntime().availableProcessors();
                ExecutorService exe = Executors.newFixedThreadPool(guiMain.getSequentialCheckBox().isSelected() ? 1 : nCores);
                // ExecutorService exe = Executors.newFixedThreadPool(1);

                for (int i = 0; i < 1; i++) {
                    exe.execute(new FractalPixelsServer(
                            center,
                            zoom,
                            iterations,
                            dimX,
                            dimY,
                            bufferedImage,
                            (Fractal) guiMain.getFractalsCombo().getSelectedItem(),
                            ticket,
                            end));
                }

                // obriga o ExecutorService a nao aceitar mais tasks novas e espera que as threads acabem o processo para poder terminar
                exe.shutdown();
                exe.awaitTermination(5, TimeUnit.MINUTES);

                //balcDos.writeObject(ImageUtils.imageToColorArray(bufferedImage));
                byte[] buffer = ImageUtils.imageToByteArray(bufferedImage);
                balcDos.write(buffer);
                balcSocket.close();
                guiMain.onDisplay(Color.GREEN, "Sent " + buffer.length + " bytes");
            }
            balcSocket.close();
        } catch (IOException | InterruptedException io) {
            guiMain.onException("", io);
            guiMain.onStop();
        }
        guiMain.onStop();
        guiMain.onDisplay(Color.RED, "Disconnected");
        return null;
    }
}
