package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Propostas;

import java.util.List;
/** Cria uma classe para gerir o undo/redo do comando RemoverOrientador.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */
public class RemoveOrientadorCommand extends CommandAdapter{
    String email;
    List<Propostas> codigo;

    /** Cria uma para gerir o undo/redo do comando RemoverOrientador.
     * @param ls Classe que liga à lógica.
     * @param email Email do docente a associar a proposta.
     * @param codigo Lista das propostas a associar.
     */
    protected RemoveOrientadorCommand(FaseContext ls, String email, List<Propostas> codigo) {
        super(ls);
        this.codigo = codigo;
        this.email = email;
    }

    @Override
    public boolean execute() {
        data.eliminarOrientadorAssociado(email);
        return true;
    }

    @Override
    public boolean undo() {
         for (int i = 0; i < codigo.size(); i++) {
            data.adicionaPropDocente(email, codigo.get(i).getCodigo());
        }
        return true;
    }

}
