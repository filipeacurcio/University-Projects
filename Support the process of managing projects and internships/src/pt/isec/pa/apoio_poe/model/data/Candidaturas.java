package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Representa uma candidatura.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class Candidaturas implements Serializable {
    static final long serialVersionUID = 100L;
    /** Número do aluno.
     */
    private long numero;
    /** Propostas a que se candidata.
     */
    List<String> propostas;

    /** Cria uma candidatura.
     * @param numero Número do estudante.
     * @param propostas Lista de propostas que o aluno se candidata.
     */
    public Candidaturas(long numero, ArrayList<String> propostas) {
        this.numero = numero;
        this.propostas = propostas;
    }

    /** Cria uma candidatura.
     * @param numero Número do estudante.
     */
    private Candidaturas(long numero){
        this.numero = numero;
        this.propostas = new ArrayList<>();
    }
    /** Cria uma candidatura.
     * @param numero Número do estudante.
     * @return Um aluno para pesquisa.
     */
    public static Candidaturas getDummyCandidatura(long numero){
        return new Candidaturas(numero);
    }

    /** Obtém o nome do aluno.
     * @return Uma String que contém o nome de estudante.
     */
    public long getNumero() {
        return numero;
    }

    /** Altera o número do aluno.
     * @param numero String com o número nome.
     */
    public void setNumero(long numero) {
        this.numero = numero;
    }

    /** Obtém a lista de propostas.
     * @return Uma lista que contém as propostas.
     */
    public List<String> getPropostas() {
        return propostas;
    }

    /** Altera a lista de propostas.
     * @param propostas Lista com as novas propostas.
     */
    public void setPropostas(List<String> propostas) {
        this.propostas = propostas;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidaturas that = (Candidaturas) o;
        return numero == that.numero;
    }


    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public String toString() {
        return "Candidaturas{" +
                "numero=" + numero +
                ", propostas=" + propostas +
                '}';
    }
}

