package io.github.joaomarccos.ads.pod;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author joaomarcos
 */
public interface TxCoord extends Remote {

    public void prepareAll() throws RemoteException;

    public void commitAll() throws RemoteException;

    public void rollbackAll() throws RemoteException;
}
