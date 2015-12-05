package io.github.joaomarccos.ads.pod;

import io.github.joaomarccos.ads.pod.project.pod.core.dao.GoogleDataStoreContext;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joaomarcos
 */
public class Main {

    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            // Inicializando contexto de persistencia para Google DataStore
            GoogleDataStoreContext context = new GoogleDataStoreContext();

            DataStoreServiceImpl dataStore = new DataStoreServiceImpl(context);
            Registry registryds = LocateRegistry.createRegistry(3005);
            registryds.rebind("dataStore", dataStore);

            System.out.println("dataStore registrado!");

            TransactionManagerImpl transactionManager = new TransactionManagerImpl(context);
            Registry registrytm = LocateRegistry.createRegistry(3006);
            registrytm.rebind("transactionManager", transactionManager);
            
            System.out.println("transactionManager registrado!");

        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
