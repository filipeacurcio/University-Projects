package pt.isec.pa.apoio_poe.model.fsm;

/** Cria uma interafce para gerir os undos/redos.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */
public interface ICommand {
    /** Excuta o comando.
     * @return Resultado da operação.
     */
    boolean execute();

    /** Faz a operação inversa ao excute.
     * @return Resultado da operação.
     */
    boolean undo();
}
