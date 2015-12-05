/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.joaomarccos.ads.pod;

import io.github.joaomarccos.ads.pod.project.pod.core.Professor;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author joaomarcos
 */
public class SincronizadorTest {

    public SincronizadorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * Test of verificarMudancas method, of class Sincronizador. Faz uma
     * atualização no Banco C e tenta sincronizar os dados.
     *
     * @throws java.rmi.NotBoundException
     * @throws java.rmi.RemoteException
     */
    @Test
    public void testVerificarMudancas() throws NotBoundException, RemoteException {
        System.out.println("verificarMudancas");

        StoreService service = (StoreService) Main.getService(3005, "dataStore");
        TransactionManager tmC = (TransactionManager) Main.getService(3006, "transactionManager");

        try {
            List<Professor> listar = service.listar();
            if (!listar.isEmpty()) {
                Professor get = listar.get(0);
                get.setNome("joao Teste" + System.currentTimeMillis());
                tmC.prepare();
                service.atualizar(get);
                tmC.commit();
            }
        } catch (Exception ex) {
            tmC.rollback();
        }
        Main.getSincronizador().verificarMudancas();
        assertTrue(Main.getSincronizador().isSyncronized());
    }

}
