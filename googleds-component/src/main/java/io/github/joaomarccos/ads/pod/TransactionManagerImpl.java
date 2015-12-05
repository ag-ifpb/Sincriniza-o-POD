package io.github.joaomarccos.ads.pod;

import io.github.joaomarccos.ads.pod.project.pod.core.dao.GoogleDataStoreContext;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author joaomarcos
 */
public class TransactionManagerImpl extends UnicastRemoteObject implements TransactionManager {

    private final GoogleDataStoreContext context;

    public TransactionManagerImpl(GoogleDataStoreContext context) throws RemoteException {
        super();
        this.context = context;
    }

    @Override
    public void prepare() throws RemoteException {
        this.context.begin();
    }

    @Override
    public void commit() throws RemoteException {
        this.context.commit();
    }

    @Override
    public void rollback() throws RemoteException {
        this.context.roolback();
    }

}
