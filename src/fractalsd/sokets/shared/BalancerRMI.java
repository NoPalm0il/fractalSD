package fractalsd.sokets.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BalancerRMI extends Remote {
    void serverConnected(ServerRMI serverRMI) throws RemoteException;
    void setRectFractalImg(int x, int y, int[][] colorBuffer) throws RemoteException;
}
