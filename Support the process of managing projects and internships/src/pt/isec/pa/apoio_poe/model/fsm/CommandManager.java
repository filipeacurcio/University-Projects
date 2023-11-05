package pt.isec.pa.apoio_poe.model.fsm;

import java.util.ArrayDeque;
import java.util.Deque;
/** Cria uma classe para gerir os comandos e guardá-los.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */


public class CommandManager {
    private Deque<ICommand> history;
    private Deque<ICommand> redoCmds;

    /** Cria uma classe para gerir os comandos e guardá-los.
     */

    public CommandManager() {
        history = new ArrayDeque<>();
        redoCmds = new ArrayDeque<>();
    }

    /** Guarda o comando invocado.
     * @param cmd Comando invocado.
     * @return resultado da aplicação.
     */

    public boolean invokeCommand(ICommand cmd) {
        redoCmds.clear();
        if (cmd.execute()) {
            history.push(cmd);
            return true;
        }
        history.clear();
        return false;
    }

    /** Faz a operação inversa à original.
     * @return resultado da aplicação.
     */
    public boolean undo() {
        if (history.isEmpty())
            return false;
        ICommand cmd = history.pop();
        cmd.undo();
        redoCmds.push(cmd);
        return true;
    }

    /** Volta ao estado depois do undo.
     * @return resultado da aplicação.
     */
    public boolean redo() {
        if (redoCmds.isEmpty())
            return false;
        ICommand cmd = redoCmds.pop();
        cmd.execute();
        history.push(cmd);
        return true;
    }
    /** Verifica se existe undos Possiveis.
     * @return resultado da aplicação.
     */
    public boolean hasUndo() {
        return history.size()>0;
    }

    /** Verifica se existe redos Possiveis.
     * @return resultado da aplicação.
     */
    public boolean hasRedo() {
        return redoCmds.size()>0;
    }

}
