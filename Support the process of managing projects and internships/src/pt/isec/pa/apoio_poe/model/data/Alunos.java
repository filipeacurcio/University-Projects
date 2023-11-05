package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;

/** Representa um aluno.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */


public class Alunos implements Serializable {
    static final long serialVersionUID = 100L;
    /** Número do estudante.
     */
    private long numEstudante;
    /** Nome do estudante.
     */
    private String nome;
    /** Email do estudante.
     */
    private String email;
    /** Sigla do curso.
     */
    private String siglaCurso;
    /** Sigla do ramo.
     */
    private String siglaRamo;
    /** Classificação do aluno.
     */
    private double classificacao;
    /** Possilidade de acesso ao estágio.
     */
    private boolean acessoEstagios;

    /** Cria um aluno.
     * @param numEstudante Número do estudante.
     * @param nome Nome do aluno.
     * @param email Email do aluno.
     * @param siglaCurso Sigla do curso.
     * @param siglaRamo Sigla do ramo.
     * @param classificacao Classificação do aluno.
     * @param acessoEstagios Guarda a possibilidade de acesso ao estágio.
     */
    public Alunos(long numEstudante, String nome, String email, String siglaCurso, String siglaRamo, double classificacao, boolean acessoEstagios) {
        this.numEstudante = numEstudante;
        this.nome = nome;
        this.email = email;
        this.siglaCurso = siglaCurso;
        this.siglaRamo = siglaRamo;
        this.classificacao = classificacao;
        this.acessoEstagios = acessoEstagios;
    }

    /** Cria um aluno com um número especifico.
     * @param num Número do aluno.
     */
    private Alunos(long num){
        numEstudante = num;
    }

    /** Reperesenta um aluno auxiliar para as pesquisas.
     * @param numEstudante Número do estudante.
     * @return Um aluno.
     */
    public static Alunos getDummyAluno(long numEstudante){
        return new Alunos(numEstudante);
    }



    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o instanceof Alunos){
            Alunos outro = (Alunos) o;
            return outro.getNumEstudante() == this.getNumEstudante();
        }

        return false;
    }


    @Override
    public int hashCode(){
        int aux = (int) (numEstudante * 0.00001);
        aux += numEstudante * 0.001;
        return aux;
    }


    /** Obtém o número do aluno.
     * @return Um long que contém o número de estudante.
     */
    public long getNumEstudante() {
        return numEstudante;
    }

    /** Obtém o nome do aluno.
     * @return Uma String que contém o nome de estudante.
     */
    public String getNome() {
        return nome;
    }


    /** Altera o nome do aluno.
     * @param nome String com o novo nome.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /** Obtém o email do aluno.
     * @return Uma String que contém o email de estudante.
     */
    public String getEmail() {
        return email;
    }

    /** Altera o nome do email.
     * @param email String com o novo email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /** Obtém a sigla do curso do aluno.
     * @return Uma String que contém a sigla do curso.
     */
    public String getSiglaCurso() {
        return siglaCurso;
    }

    /** Altera a sigla do curso.
     * @param siglaCurso String com a nova sigla.
     */
    public void setSiglaCurso(String siglaCurso) {
        this.siglaCurso = siglaCurso;
    }

    /** Obtém a sigla do ramo do aluno.
     * @return Uma String que contém a sigla do ramo.
     */
    public String getSiglaRamo() {
        return siglaRamo;
    }

    /** Altera a sigla do ramo do aluno.
     * @param siglaRamo String com a nova sigla.
     */
    public void setSiglaRamo(String siglaRamo) {
        this.siglaRamo = siglaRamo;
    }

    /** Obtém a classificação do aluno.
     * @return Um double que contém a classifição.
     */
    public double getClassificacao() {
        return classificacao;
    }

    /** Altera a classificação do aluno.
     * @param classificacao String com a nova sigla.
     */
    public void setClassificacao(double classificacao) {
        this.classificacao = classificacao;
    }

    /** Obtém o acesso a possbilidade de acesso ao estágio do aluno.
     * @return Um boolean que contém o acesso ao estágio.
     */
    public boolean getAcessoEstagios() {
        return acessoEstagios;
    }

    /** Altera o acesso a possbilidade de acesso ao estágio do aluno.
     * @param acessoEstagios boolean com a nova possibilidade.
     */
    public void setAcessoEstagios(boolean acessoEstagios) {
        this.acessoEstagios = acessoEstagios;
    }


    @Override
    public String toString() {
        return "Alunos{" +
                "numEstudante=" + numEstudante +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", siglaCurso='" + siglaCurso + '\'' +
                ", siglaRamo='" + siglaRamo + '\'' +
                ", classificacao=" + classificacao +
                ", acessoEstagios=" + acessoEstagios +
                '}' + "\n";
    }

}
