package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.FaseData;

/** Cria uma classe para gerir o undo/redo do comando adicionarOrientador.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class AddOrientadorCommand extends CommandAdapter{
        String email;
        String codigo;

    /** Cria uma para gerir o undo/redo do comando adicionarOrientador.
     * @param ls Classe que liga à lógica.
     * @param email Email do docente a associar a proposta.
     * @param codigo Codigo da proposta a associar.
     */
    protected AddOrientadorCommand(FaseContext ls, String email, String codigo) {
        super(ls);
        this.codigo = codigo;
        this.email = email;
    }

    @Override
    public boolean execute() {
        data.adicionaPropDocente(email, codigo);
        return true;
    }

    @Override
    public boolean undo() {
        data.eliminarOrientadorPropAssociado(email, codigo);
        return true;
    }
}
