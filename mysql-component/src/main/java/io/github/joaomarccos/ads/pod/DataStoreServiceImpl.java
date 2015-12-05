package io.github.joaomarccos.ads.pod;

import io.github.joaomarccos.ads.pod.project.pod.core.Professor;
import io.github.joaomarccos.ads.pod.project.pod.core.dao.Dao;
import io.github.joaomarccos.ads.pod.project.pod.core.dao.DaoProfessorJPA;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author joaomarcos
 */
public class DataStoreServiceImpl extends UnicastRemoteObject implements StoreService {

    private final Dao<Professor> dao;

    public DataStoreServiceImpl(EntityManager em) throws RemoteException {
        super();
        this.dao = new DaoProfessorJPA(em);
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
