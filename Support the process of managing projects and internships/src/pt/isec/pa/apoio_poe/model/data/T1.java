package pt.isec.pa.apoio_poe.model.data;
/** Representa um tipo de propostas(T1).
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */
public class T1 extends Propostas{
    /** Ramo da proposta.
     */
    private String ramo;
    /** Entidade de acolhimento.
     */
    private String entidade;

    /** Cria uma proposta (T1).
     * @param codigo Código da proposta.
     * @param ramo Ramo da proposta.
     * @param titulo Titulo da proposta.
     * @param entidade Entidade de acolhimento.
     */
    public T1(String codigo, String ramo, String titulo, String entidade) {
        super(codigo, titulo, -1);
        this.ramo = ramo;
        this.entidade = entidade;
    }

    @Override
    public String getRamo() {
        return ramo;
    }

    @Override
    public void setRamo(String ramo) {
        this.ramo = ramo;
    }


    /** Obtém a entidade da proposta.
     * @return Uma String que contém a entidade da proposta.
     */
    public String getEntidade() {
        return entidade;
    }

    @Override
    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }



    @Override
    public String toString() {
        return "T1{" + super.toString() +
                ", ramo='" + ramo + '\'' +
                ", entidade='" + entidade + '\'' +
                "}\n";
    }
}
