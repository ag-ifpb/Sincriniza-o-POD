package io.github.joaomarccos.ads.pod.project.pod.core.dao;

import io.github.joaomarccos.ads.pod.project.pod.core.Professor;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author joaomarcos
 */
public class DaoProfessorGoogleD implements Dao<Professor> {

    private final GoogleDataStoreContext context;

    public DaoProfessorGoogleD(GoogleDataStoreContext context) throws RemoteException, NotBoundException {
        this.context = context;
    }

    @Override
    public boolean save(Professor professor) {
        this.context.save(professor);
        return true;
    }

    @Override
    public boolean update(Professor professor) {
        this.context.update(professor);
        return true;
    }

    @Override
    public List<Professor> list() {
        return this.context.list();
    }

}
