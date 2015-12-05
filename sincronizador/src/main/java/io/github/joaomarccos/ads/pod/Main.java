package io.github.joaomarccos.ads.pod;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joaomarcos
 */
public class Main {

    public static final long TEMPO = (1000 * 60); // Sincroniza o banco a cada 5 minutos
    private static Sincronizador sincronizador;

    public static Remote getService(int port, String serviceName) throws NotBoundException, RemoteException {
        Registry registry = LocateRegistry.getRegistry(port);
        Remote lookup = registry.lookup(serviceName);
        return lookup;
    }

    public static void setupSincronizador() {
        try {
            StoreService dataStoreA = (StoreService) getService(3001, "dataStore");
            StoreService dataStoreB = (StoreService) getService(3003, "dataStore");
            StoreService dataStoreC = (StoreService) getService(3005, "dataStore");

            TxCoord txCoord = (TxCoord) getService(3009, "txCoord");
            sincronizador = new Sincronizador(dataStoreA, dataStoreB, dataStoreC, txCoord);

        } catch (NotBoundException | RemoteException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Sincronizador getSincronizador() {
        if (sincronizador == null) {
            setupSincronizador();
        }
        return sincronizador;
    }

    public static void main(String[] args) {
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                getSincronizador().verificarMudancas();
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, TEMPO, TEMPO);

    }
}
