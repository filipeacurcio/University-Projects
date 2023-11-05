package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;

/** Representa um docente.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class Docentes implements Serializable {
    static final long serialVersionUID = 100L;

    /** Email do docente.
     */
    private String email;
    /** Nome do docente.
     */
    private String nome;

    /** Cria uma candidatura.
     * @param nome Nome do docente.
     * @param email Email do docente.
     */
    public Docentes(String nome, String email){
        this.email = email;
        this.nome = nome;
    }

    /** Cria uma candidatura.
     * @param email Email do docente.
     */
    private Docentes(String email){
        this.nome = null;
        this.email = email;
    }

    /** Cria um docente.
     * @param email Email do docente.
     * @return Um docente para pesquisa.
     */
    public static Docentes getDummyDocente(String email){
       return new Docentes(email);
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o instanceof Docentes){
            Docentes outro = (Docentes) o;
            return outro.email.equals(this.email);
        }
        return false;
    }


    @Override
    public int hashCode(){
        return email.hashCode();
    }

    /** Obtém o email do docente.
     * @return Uma String que contém o email do docente.
     */
    public String getEmail() {
        return email;
    }

    /** Obtém o nome do docente.
     * @return Uma String que contém o nome de docente.
     */
    public String getNome() {
        return nome;
    }

    /** Altera o nome do docente.
     * @param nome String com novo nome do docente.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }


    @Override
    public String toString(){
        return "Nome: " + nome + " Email: " + email;
    }

}
