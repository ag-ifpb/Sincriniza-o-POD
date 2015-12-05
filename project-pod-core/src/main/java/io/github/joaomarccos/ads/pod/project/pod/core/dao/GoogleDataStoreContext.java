package io.github.joaomarccos.ads.pod.project.pod.core.dao;

import ag.ifpb.pod.rmi.core.DatastoreService;
import ag.ifpb.pod.rmi.core.TeacherTO;
import io.github.joaomarccos.ads.pod.project.pod.core.Professor;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joaomarcos
 */
public class GoogleDataStoreContext {

    private final DatastoreService datastoreService;

    private Professor novo;
    private Professor existente;

    public GoogleDataStoreContext() throws RemoteException, NotBoundException {
        System.setProperty("java.rmi.server.hostname", "192.168.2.101");
        Registry registry = LocateRegistry.getRegistry("200.129.71.228", 9090);
        this.datastoreService = (DatastoreService) registry.lookup("DatastoreService");
    }

    public void commit() throws RemoteException {
        if (novo != null) {
            realSave();
        } else if (existente != null) {
            realUpdate();
        }
    }

    public void begin() {
        this.novo = null;
        this.existente = null;
    }

    private void realUpdate() throws RemoteException {
        if (existente != null) {
            datastoreService.updateTeacher(existente.toTeacherTO());
        }
    }

    private void realSave() throws RemoteException {
        if (novo != null) {
            datastoreService.createTeacher(novo.toTeacherTO());
        }
    }

    public void roolback() {
        this.novo = null;
        this.existente = null;
    }

    public void save(Professor professor) {
        this.novo = new Professor(professor.getCodigo(), professor.getNome(), professor.getAbreviacao(), professor.isAtivo());
    }

    public void update(Professor professor) {
        this.existente = new Professor(professor.getCodigo(), professor.getNome(), professor.getAbreviacao(), professor.isAtivo());
    }

    public List<Professor> list() {
        List<Professor> professores = new ArrayList<>();
        try {
            List<TeacherTO> listTeachers = datastoreService.listTeachers();
            convert(listTeachers, professores);

        } catch (RemoteException ex) {
            Logger.getLogger(DaoProfessorGoogleD.class.getName()).log(Level.SEVERE, null, ex);

        }
        return professores;
    }

    /**
     * Converte uma lista de TeacherTO para uma lista de Professores
     *
     * @param listTeachers
     * @param professores
     */
    private void convert(List<TeacherTO> listTeachers, List<Professor> professores) {
        for (TeacherTO teacherTO : listTeachers) {
            Professor p = new Professor();
            p.fromTeacherTO(teacherTO);
            professores.add(p);
        }
    }

}
