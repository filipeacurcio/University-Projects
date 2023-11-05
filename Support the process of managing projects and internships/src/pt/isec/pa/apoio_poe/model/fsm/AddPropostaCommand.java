package pt.isec.pa.apoio_poe.model.fsm;

/** Cria uma classe para gerir o undo/redo do comando adicionarProposta.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */


public class AddPropostaCommand extends CommandAdapter{
    long numAluno;
    String codigo;

    /** Cria uma para gerir o undo/redo do comando adicionarProposta.
     * @param ls Classe que liga à lógica.
     * @param numALuno Numero do aluno a associar a proposta.
     * @param codigo Codigo da proposta a associar.
     */
    protected AddPropostaCommand(FaseContext ls, long numALuno, String codigo) {
        super(ls);
        this.codigo = codigo;
        this.numAluno = numALuno;
    }

    @Override
    public boolean execute() {
        return data.atribuicaoManual(numAluno, codigo);
    }

    @Override
    public boolean undo() {
       return data.removePropostaDoAluno(numAluno);
    }
}
