package io.github.joaomarccos.ads.pod;

import io.github.joaomarccos.ads.pod.project.pod.core.Professor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joaomarcos
 */
public class Sincronizador {

    private final String PATH = "src/main/resources/espelho.properties";
    private Path arqEspelho;
    private Properties espelho;
    private RepositorioModificacoes modificacoes;
    private StoreService datastoreA;
    private StoreService datastoreB;
    private StoreService datastoreC;

    private List<Professor> repositorioA;
    private List<Professor> repositorioB;
    private List<Professor> repositorioC;

    private TxCoord txCoord;

    private Sincronizador() throws IOException {
        init();
    }

    public Sincronizador(StoreService datastoreA, StoreService datastoreB, StoreService datastoreC, TxCoord txCoord) throws IOException {
        this();
        this.datastoreA = datastoreA;
        this.datastoreB = datastoreB;
        this.datastoreC = datastoreC;
        this.txCoord = txCoord;
    }

    /**
     * Inicia o arquivo checksums e o repositório de modificações
     * @throws IOException 
     */
    private void init() throws IOException {
        this.arqEspelho = Paths.get(PATH);
        if (!Files.exists(arqEspelho)) {
            Files.createFile(arqEspelho);
        }

        this.espelho = new Properties();
        this.espelho.load(Files.newInputStream(arqEspelho));
        this.modificacoes = new RepositorioModificacoes();

    }

    /**
     * Vasculha por alterações em cada banco, caso as encontre as replica para
     * os outros.
     * 
     */
    public void verificarMudancas() {
        carregarDados();
        System.out.println("Verificando mudanças..");
        
        if (!MD5Util.generateHash(repositorioA).equals(this.espelho.getProperty("checksum"))) {
            System.out.println("Replicando A");
            registrarModificacoes(repositorioA);
            replicarA();
            atualizarEspelho(repositorioA);
            carregarDados();
        }
        if (!MD5Util.generateHash(repositorioB).equals(this.espelho.getProperty("checksum"))) {
            System.out.println("Replicando B");
            registrarModificacoes(repositorioB);
            replicarB();
            atualizarEspelho(repositorioB);
            carregarDados();
        }
        if (!MD5Util.generateHash(repositorioC).equals(this.espelho.getProperty("checksum"))) {
            System.out.println("Replicando C");
            registrarModificacoes(repositorioC);
            replicarC();
            atualizarEspelho(repositorioC);
            carregarDados();
        }

        log();

    }

    /**
     * Imprime os checkums de cada banco na saída padrão
     */
    private void log() {
        System.out.println("-----------------------------------------+");
        System.out.println("banco A: " + MD5Util.generateHash(repositorioA));
        System.out.println("banco B: " + MD5Util.generateHash(repositorioB));
        System.out.println("banco C: " + MD5Util.generateHash(repositorioC));
        System.out.println("-----------------------------------------+\n");

    }

    public boolean isSyncronized() {

        return MD5Util.generateHash(repositorioA).
                equals(espelho.getProperty("checksum"))
                && MD5Util.generateHash(repositorioB).
                equals(espelho.getProperty("checksum"))
                && MD5Util.generateHash(repositorioC).
                equals(espelho.getProperty("checksum"));
    }

    /**
     * Obtém a lista de dados de cada banco e as armazena
     */
    private void carregarDados() {
        try {
            this.repositorioA = datastoreA.listar();
            this.repositorioB = datastoreB.listar();
            this.repositorioC = datastoreC.listar();
        } catch (RemoteException ex) {
            Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, "Não foi possível acessas um ou mais bancos", ex);
        }
    }

    /**
     * Registra as modificações encontradas no banco em um repositório de modificações
     * @param repositorio 
     */
    private void registrarModificacoes(List<Professor> repositorio) {
        for (Professor professor : repositorio) {
            if (!espelho.containsKey(String.valueOf(professor.getCodigo()))) {
                modificacoes.addNovoProfessor(professor);
            } else if (!espelho.getProperty(String.valueOf(professor.getCodigo())).equals(MD5Util.generateHash(professor.toString()))) {
                modificacoes.addProfessorAlterado(professor);
            }
        }
    }

    /**
     * Varre o repositório de modificações e as replica para os bancos B e C
     */
    private void replicarA() {
        try {
            for (Professor p : modificacoes.getNovosProfessores()) {
                txCoord.prepareAll();
                if (!repositorioB.contains(p)) {
                    datastoreB.salvar(p);
                }
                if (!repositorioC.contains(p)) {
                    datastoreC.salvar(p);
                }
                txCoord.commitAll();
            }
            for (Professor p : modificacoes.getProfessoresAlterados()) {
                txCoord.prepareAll();
                datastoreB.atualizar(p);
                datastoreC.atualizar(p);
                txCoord.commitAll();
            }
            modificacoes.clear();
        } catch (RemoteException ex) {
            Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
            try {
                txCoord.rollbackAll();
            } catch (RemoteException ex1) {
                Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    /**
     * Varre o repositório de modificações e as replica para os bancos A e C
     */
    private void replicarB() {
        try {
            for (Professor p : modificacoes.getNovosProfessores()) {
                txCoord.prepareAll();
                if (!repositorioA.contains(p)) {
                    datastoreA.salvar(p);
                }
                if (!repositorioC.contains(p)) {
                    datastoreC.salvar(p);
                }
                txCoord.commitAll();
            }
            for (Professor p : modificacoes.getProfessoresAlterados()) {
                txCoord.prepareAll();
                datastoreA.atualizar(p);
                datastoreC.atualizar(p);
                txCoord.commitAll();
            }
            modificacoes.clear();
        } catch (RemoteException ex) {
            Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
            try {
                txCoord.rollbackAll();
            } catch (RemoteException ex1) {
                Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    /**
     * Varre o repositório de modificações e as replica para os bancos A e B
     */
    private void replicarC() {
        try {
            for (Professor p : modificacoes.getNovosProfessores()) {
                txCoord.prepareAll();
                if (!repositorioA.contains(p)) {
                    datastoreA.salvar(p);
                }
                if (!repositorioB.contains(p)) {
                    datastoreB.salvar(p);
                }
                txCoord.commitAll();
            }
            for (Professor p : modificacoes.getProfessoresAlterados()) {
                txCoord.prepareAll();
                datastoreA.atualizar(p);
                datastoreB.atualizar(p);
                txCoord.commitAll();
            }
            modificacoes.clear();
        } catch (RemoteException ex) {
            Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
            try {
                txCoord.rollbackAll();
            } catch (RemoteException ex1) {
                Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    /**
     * Atualiza o arquivo que mantém os checksums do estado consistente do banco
     * @param repositorioA 
     */
    private void atualizarEspelho(List<Professor> repositorioA) {
        try {
            limparEspelho();
            this.espelho.setProperty("checksum", MD5Util.generateHash(repositorioA));
            for (Professor p : repositorioA) {
                this.espelho.setProperty(p.getCodigo() + "", MD5Util.generateHash(p.toString()));
            }
            this.espelho.store(Files.newOutputStream(arqEspelho), "Espelho atualizado às " + LocalDateTime.now().format(DateTimeFormatter.ISO_TIME));
        } catch (IOException ex) {
            Logger.getLogger(Sincronizador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Limpa o arquivo de checksum
     * @throws IOException 
     */
    private void limparEspelho() throws IOException {
        this.espelho.clear();
        this.espelho.store(Files.newOutputStream(arqEspelho), "Espelho atualizado às " + LocalDateTime.now().format(DateTimeFormatter.ISO_TIME));
    }

}
