package io.github.joaomarccos.ads.pod.project.pod.core.dao;

import io.github.joaomarccos.ads.pod.project.pod.core.Professor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author joaomarcos
 */
public class DaoProfessorJPA implements Dao<Professor> {

    private EntityManager em;

    public DaoProfessorJPA(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEm() {
        return em;
    }

    @Override
    public boolean save(Professor professor) {
        try {
            em.persist(professor);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(Professor professor) {
        try {
            Professor find = em.find(Professor.class, professor.getCodigo());
            if (find != null) {
                find.setNome(professor.getNome());
                find.setAbreviacao(professor.getAbreviacao());
                find.setAtivo(professor.isAtivo());
                em.merge(find);
            } else {
                em.merge(professor);
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Professor> list() {
        TypedQuery<Professor> tq = em.createQuery("SELECT p FROM Professor p ORDER BY p.codigo ASC", Professor.class);
        List<Professor> resultList = tq.getResultList();
        return resultList;
    }

}
