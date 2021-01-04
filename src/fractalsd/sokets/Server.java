package fractalsd.sokets;

import fractalsd.fractal.Fractal;
import fractalsd.fractal.engine.FractalPixelsServer;
import fractalsd.gui.GUIMain;
import fractalsd.sokets.shared.BalancerRMI;
import fractalsd.sokets.shared.ServerRMI;
import fractalsd.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Server extends SwingWorker<Long, Integer> implements ServerRMI{

    private final GUIMain guiMain;
    private Registry balcRegistry;
    private BalancerRMI balStub;
    private Registry serverRegistry;
    private ServerRMI serverStub;
    private String[] fractalParams;

    public Server(GUIMain guiMain) {
        this.guiMain = guiMain;
    }

    @Override
    protected Long doInBackground() {
        try {
            // connects to balancer rmi
            balcRegistry = LocateRegistry.getRegistry("", 13337);
            // TODO: Faz o que o stor fez, assim n dá para chegar a outra app
            balStub = (BalancerRMI) balcRegistry.lookup("BalancerRMI");
            // creates this server rmi
            serverRegistry = LocateRegistry.createRegistry(13338);
            serverStub = (ServerRMI) UnicastRemoteObject.exportObject(this, 13338);
            serverRegistry.bind("ServerRMI", serverStub);

            balStub.serverConnected(serverStub);
            guiMain.onDisplay(Color.GREEN, "Connected to balancer");
        } catch (IOException | NotBoundException | AlreadyBoundException io) {
            guiMain.onException("", io);
            guiMain.onStop();
        }
        return null;
    }

    @Override
    public void setFractalParams(String fractalParams) throws RemoteException {
        this.fractalParams = fractalParams.split(" ");
        guiMain.onDisplay(Color.GREEN, "Received params");
    }

    @Override
    public void generateFractal() {
        try {
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
            ExecutorService exe = Executors.newFixedThreadPool(nCores);

            for (int i = 0; i < nCores; i++) {
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
            exe.awaitTermination(1, TimeUnit.HOURS);

            balStub.setRectFractalImg(0, start, ImageUtils.imageToColorArray(bufferedImage));
            guiMain.onDisplay(Color.GREEN, "Image sent");
        } catch (Exception e) {
            guiMain.onException("", e);
        }
    }
}
