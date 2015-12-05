package io.github.joaomarccos.ads.pod;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.persistence.EntityManager;

/**
 *
 * @author joaomarcos
 */
public class TransactionManagerImpl extends UnicastRemoteObject implements TransactionManager {

    private final EntityManager em;

    public TransactionManagerImpl(EntityManager em) throws RemoteException {
        super();
        this.em = em;
    }

    @Override
    public void prepare() throws RemoteException {
        em.getTransaction().begin();
    }

    @Override
    public void commit() throws RemoteException {
        em.getTransaction().commit();
    }

    @Override
    public void rollback() throws RemoteException {
        em.getTransaction().rollback();
    }

}
