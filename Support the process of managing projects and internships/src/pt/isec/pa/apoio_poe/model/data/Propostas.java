package pt.isec.pa.apoio_poe.model.data;


import java.io.Serializable;

/** Representa uma proposta.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class Propostas  implements Serializable {
    static final long serialVersionUID = 100L;
    /** Código da proposta.
     */
    private String codigo;
    /** Titulo da propostas.
     */
    private String titulo;
    /** Número do aluno.
     */
    private long numAluno;


    /** Cria uma proposta.
     * @param codigo Código da proposta.
     * @param titulo Titulo da proposta.
     * @param numAluno Número do aluno associado.
     */
    protected Propostas(String codigo, String titulo, long numAluno) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.numAluno = numAluno;
    }

    /** Cria uma proposta.
     * @param codigo Código da proposta.
     */
    protected Propostas(String codigo) {
        this.codigo = codigo;
        this.titulo = null;
    }

    /** Cria um docente.
     * @param codigo Código da propsota.
     * @return Uma proposta para pesquisa.
     */
    public static Propostas getDummyProposta(String codigo){
        return new Propostas(codigo);
    }

    /** Obtém o número do aluno.
     * @return Um long que contém o número de estudante.
     */
    public long getNumAluno() {
        return numAluno;
    }

    /** Altera o número do aluno.
     * @param numAluno long com o novo número.
     */
    public void setNumAluno(long numAluno) {
        this.numAluno = numAluno;
    }

    /** Altera o ramo da proposta.
     * @param ramo String com o novo ramo.
     */
    public void setRamo(String ramo) {

    }
    /** Altera a entidade da proposta.
     * @param entidade String com a nova entidade.
     */
    public void setEntidade(String entidade) {

    }

    /** Obtém o ramo da proposta.
     * @return Uma String que contém o ramo.
     */
    public String getRamo() {
        return "";
    }

    /** Obtém o codigo da proposta.
     * @return Uma String que contém o codigo.
     */
    public String getCodigo() {
        return codigo;
    }

    /** Altera o codigo da proposta.
     * @param codigo String com o novo codigo.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /** Obtém o titulo da proposta.
     * @return Uma String que contém o titulo.
     */
    public String getTitulo() {
        return titulo;
    }


    /** Altera o titulo da proposta.
     * @param titulo String com o novo titulo.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if(o instanceof Propostas){
            Propostas outro = (Propostas) o;
            return outro.codigo.equals(this.codigo);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return "Propostas{" +
                "codigo='" + codigo + '\'' +
                ", titulo='" + titulo + '\'' +
                ", numALuno=" + numAluno +

                "}";
    }

}
