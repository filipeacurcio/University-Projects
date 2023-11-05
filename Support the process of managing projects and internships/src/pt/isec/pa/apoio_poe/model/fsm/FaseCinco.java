package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.FaseData;

/** Cria uma classe da Fase cinco da máquina de estados.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class FaseCinco extends FaseStateAdapter{

    /** Cria uma classe da Fase cinco da máquina de estados.
     * @param context Classe que liga a interface com utilizador à lógica.
     * @param data Classe que contém os dados da aplicação.
     */
    protected FaseCinco(FaseContext context, FaseData data) {
        super(context, data);
    }


    @Override
    public String listaFinalF5(int opcao){
        return data.listaFinalF5(opcao);
    }

    @Override
    public String listarAlunos(){
        return data.listaAlunosComCandidatura() + data.listaAlunosSemCandidatura() + data.listaAlunosAutoPropostos() + data.propostasSemAtribuicao();
    }
    @Override
    public FaseState getState() {
        return FaseState.FASE_CINCO;
    }
}
