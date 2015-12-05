package io.github.joaomarccos.ads.pod;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author joaomarcos
 */
public class Main {
    public static void main(String[] args) {
        EntityManager em = Persistence.createEntityManagerFactory("bdpod-mysql").createEntityManager();
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        try {
            DataStoreServiceImpl dataStore = new DataStoreServiceImpl(em);
            Registry registryds = LocateRegistry.createRegistry(3003);
            registryds.rebind("dataStore", dataStore);
            
            System.out.println("dataStore registrado!");
            
            TransactionManagerImpl transactionManager = new TransactionManagerImpl(em);
            Registry registrytm = LocateRegistry.createRegistry(3004);
            registrytm.rebind("transactionManager", transactionManager);
            
            System.out.println("transactionManager registrado!");
            
        } catch (RemoteException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
