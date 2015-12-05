package io.github.joaomarccos.ads.pod.project.pod.core;

import ag.ifpb.pod.rmi.core.TeacherTO;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author joaomarcos
 */
@Entity
@Table(name = "professor")
public class Professor implements Serializable {

    @Id
    private int codigo;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String abreviacao;

    @Column(nullable = false)
    private boolean ativo;

    public Professor() {
    }

    public Professor(int codigo, String nome, String abreviacao, boolean ativo) {
        this.codigo = codigo;
        this.nome = nome;
        this.abreviacao = abreviacao;
        this.ativo = ativo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAbreviacao() {
        return abreviacao;
    }

    public void setAbreviacao(String abreviacao) {
        this.abreviacao = abreviacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.codigo;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Professor other = (Professor) obj;
        return this.codigo == other.codigo;
    }

    /**
     * Converte um professor para o objeto de transferência TeacherTO
     *
     * @return
     */
    public TeacherTO toTeacherTO() {
        TeacherTO teacherTO = new TeacherTO();
        teacherTO.setAbbrev(abreviacao);
        teacherTO.setCode(codigo);
        teacherTO.setName(nome);
        teacherTO.setActive(ativo);

        return teacherTO;
    }

    /**
     * Constrói o professor a partir de um TeacherTO
     *
     * @param TO
     */
    public void fromTeacherTO(TeacherTO TO) {
        abreviacao = TO.getAbbrev();
        nome = TO.getName();
        codigo = TO.getCode();
        ativo = TO.isActive();
    }

    @Override
    public String toString() {
        return codigo + nome + abreviacao + ativo;
    }

}
