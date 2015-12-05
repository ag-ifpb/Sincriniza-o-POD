package io.github.joaomarccos.ads.pod;

import io.github.joaomarccos.ads.pod.project.pod.core.Professor;
import io.github.joaomarccos.ads.pod.project.pod.core.dao.Dao;
import io.github.joaomarccos.ads.pod.project.pod.core.dao.DaoProfessorGoogleD;
import io.github.joaomarccos.ads.pod.project.pod.core.dao.GoogleDataStoreContext;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * @author joaomarcos
 */
public class DataStoreServiceImpl extends UnicastRemoteObject implements StoreService {

    private final Dao<Professor> dao;

    public DataStoreServiceImpl(GoogleDataStoreContext context) throws RemoteException {
        super();
        try {
            this.dao = new DaoProfessorGoogleD(context);
        } catch (NotBoundException ex) {
            throw new RemoteException();
        }
    }

    @Override
    public void salvar(Professor professor) throws RemoteException {
        if (!dao.save(professor)) {
            throw new RemoteException();
        }
    }

    @Override
    public void atualizar(Professor professor) throws RemoteException {
        if (!dao.update(professor)) {
            throw new RemoteException();
        }
    }

    @Override
    public List<Professor> listar() throws RemoteException {
        return dao.list();
    }

}
