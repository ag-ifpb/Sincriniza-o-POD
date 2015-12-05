package io.github.joaomarccos.ads.pod;

import io.github.joaomarccos.ads.pod.project.pod.core.Professor;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author joaomarcos
 */
public interface StoreService extends Remote {

    public void salvar(Professor professor) throws RemoteException;

    public void atualizar(Professor professor) throws RemoteException;

    public List<Professor> listar() throws RemoteException;
}
