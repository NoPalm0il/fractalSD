package fractalsd.fractal.sokets;

import fractalsd.main.GUIMain;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable{
    private Socket socket;
    private final String serverIp;
    private final int serverPort;
    private final GUIMain guiMain;

    public Client(String serverIp, int serverPort, GUIMain guiMain) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.guiMain = guiMain;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(serverIp, serverPort);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            // tells the balancer server that this socket comes from a client (c = client)
            oos.writeChar('c');

            //ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            oos.writeObject(guiMain.getIteration());
        } catch(IOException io){
            System.out.println(io.getLocalizedMessage());
        }

    }
}
