package pt.isec.pa.apoio_poe.model.data;

import javax.print.Doc;
/** Representa um tipo de propostas(T2).
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */
public class T2 extends Propostas{
    /** Ramo da proposta.
     */
    private String ramo;
    /** Docente associado à proposta.
     */
    private Docentes docenteProponente;

    /** Cria uma proposta (T2).
     * @param codigo Código da proposta.
     * @param ramo Ramo da proposta.
     * @param titulo Titulo da proposta.
     * @param email Email do docente.
     * @param numAluno Numero do aluno.
     */
    public T2(String codigo, String ramo, String titulo, String email, long numAluno) {
        super(codigo, titulo, numAluno);
        this.ramo = ramo;
        docenteProponente = Docentes.getDummyDocente(email);
    }

    /** Cria uma proposta (T2).
     * @param codigo Código da proposta.
     * @param ramo Ramo da proposta.
     * @param titulo Titulo da proposta.
     * @param email Email do docente.
     */
    public T2(String codigo, String ramo, String titulo, String email) {
        super(codigo, titulo,-1);
        this.ramo = ramo;
        docenteProponente = Docentes.getDummyDocente(email);
    }

    @Override
    public String getRamo() {
        return ramo;
    }

    @Override
    public void setRamo(String ramo) {
        this.ramo = ramo;
    }

    /** Obtém o docente associado.
     * @return Um Docente que contém o docente associado à prosposta.
     */
    public Docentes getDocenteProponente() {
        return docenteProponente;
    }

    /** Altera o número do aluno.
     * @param docenteProponente Docente a adicionar à proposta.
     */
    public void setDocenteProponente(Docentes docenteProponente) {
        this.docenteProponente = docenteProponente;

    }


    @Override
    public String toString() {
        return "T2{" + super.toString() +
                "ramo='" + ramo + '\'' +
                ", docenteProponente=" + docenteProponente +
                "}\n";
    }
}
