package io.github.joaomarccos.ads.pod;

import io.github.joaomarccos.ads.pod.project.pod.core.Professor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joaomarcos
 */
public class RepositorioModificacoes {

    private List<Professor> novosProfessores;
    private List<Professor> professoresAlterados;

    public RepositorioModificacoes() {
        initLists();
    }

    private void initLists() {
        this.novosProfessores = new ArrayList<>();
        this.professoresAlterados = new ArrayList<>();
    }

    public List<Professor> getNovosProfessores() {
        return novosProfessores;
    }

    public List<Professor> getProfessoresAlterados() {
        return professoresAlterados;
    }


    public void addNovoProfessor(Professor professor) {
        this.novosProfessores.add(professor);
    }

    public void addProfessorAlterado(Professor professor) {
        this.professoresAlterados.add(professor);
    }

    public void clear() {
        this.professoresAlterados.clear();
        this.novosProfessores.clear();
    }

}
