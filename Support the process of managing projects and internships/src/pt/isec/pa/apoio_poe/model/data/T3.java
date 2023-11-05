package pt.isec.pa.apoio_poe.model.data;

/** Representa um tipo de propostas(T3).
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */
public class T3 extends Propostas{


    /** Cria uma proposta (T2).
     * @param codigo Código da proposta.
     * @param titulo Titulo da proposta.
     * @param numEstudante Número do estudante.
     */
    public T3(String codigo, String titulo, long numEstudante) {
        super(codigo, titulo, numEstudante);
    }

    @Override
    public String toString() {
        return "T3{" + super.toString() +
                "}\n";
    }
}
