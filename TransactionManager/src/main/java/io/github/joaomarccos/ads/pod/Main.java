package io.github.joaomarccos.ads.pod;

import java.rmi.NotBoundException;
import java.rmi.Remote;
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

    private static Remote getService(int port, String serviceName) throws NotBoundException, RemoteException {
        Registry registry = LocateRegistry.getRegistry(port);
        Remote lookup = registry.lookup(serviceName);
        return lookup;
    }

    public static void main(String[] args) {
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        try {
            TransactionManager tmA = (TransactionManager) getService(3002, "transactionManager");
            TransactionManager tmB = (TransactionManager) getService(3004, "transactionManager");
            TransactionManager tmC = (TransactionManager) getService(3006, "transactionManager");

            TxCoordImpl txCoordImpl = new TxCoordImpl(tmA, tmB, tmC);
            Registry registry = LocateRegistry.createRegistry(3009);
            registry.rebind("txCoord", txCoordImpl);

            System.out.println("TxCoord Regitrado!");

        } catch (NotBoundException | RemoteException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
