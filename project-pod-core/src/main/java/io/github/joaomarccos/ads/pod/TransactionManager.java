package io.github.joaomarccos.ads.pod;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author joaomarcos
 */
public interface TransactionManager extends Remote {

    public void prepare() throws RemoteException;

    public void commit() throws RemoteException;

    public void rollback() throws RemoteException;

}
