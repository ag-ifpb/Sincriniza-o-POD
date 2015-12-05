package io.github.joaomarccos.ads.pod;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author joaomarcos
 */
public class TxCoordImpl extends UnicastRemoteObject implements TxCoord {

    private final TransactionManager tma;
    private final TransactionManager tmb;
    private final TransactionManager tmc;

    public TxCoordImpl(TransactionManager tma, TransactionManager tmb, TransactionManager tmc) throws RemoteException {
        super();
        this.tma = tma;
        this.tmb = tmb;
        this.tmc = tmc;
    }

    @Override
    public void prepareAll() throws RemoteException {
        this.tma.prepare();
        this.tmb.prepare();
        this.tmc.prepare();
    }

    @Override
    public void commitAll() throws RemoteException {
        this.tma.commit();
        this.tmb.commit();
        this.tmc.commit();
    }

    @Override
    public void rollbackAll() throws RemoteException {
        this.tma.rollback();
        this.tmb.rollback();
        this.tmc.rollback();
    }

}
