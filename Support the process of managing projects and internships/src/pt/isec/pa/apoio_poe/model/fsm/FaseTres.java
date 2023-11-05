package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.FaseData;

import java.util.List;
import java.util.Map;

/** Cria uma classe da Fase três da máquina de estados.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */
public class FaseTres extends FaseStateAdapter{

    /** Cria uma classe da Fase três da máquina de estados.
     * @param context Classe que liga a interface com utilizador à lógica.
     * @param data Classe que contém os dados da aplicação.
     */
    protected FaseTres(FaseContext context, FaseData data) {
        super(context, data);
    }


    @Override
    public String consultaAluno(long numEstudante){
        return data.consultaAluno(numEstudante);
    }

    @Override
    public String consultaProposta(String codigo){
        return data.consultaProposta(codigo);
    }

    @Override
    public Map<String, List<Long>> atribuicaoPropDisponiveis(){
        return data.atribuicaoPropDisponiveis();
    }

    @Override
    public boolean atribuicaoManual(long numALuno, String propCodigo){
        return data.atribuicaoManual(numALuno, propCodigo);
    }
    @Override
    public void backState(){
        context.changeState(new FaseDois(context, data));
    }

    @Override
    public boolean getFaseAnteriorFechado(int fase){
        return data.getFaseAnterior(fase);
    }

    @Override
    public boolean getFechado(int fase) {
        return data.getFaseFechada(fase);
    }

    @Override
    public void setFechado(boolean fechada, int fase) {
        data.setFechada(fechada, fase);
    }
    @Override
    public void atribuicaoAutoProp(){
        data.atribuicaoAutoProp();
    }
    @Override
    public String listarAlunos(){
        return data.listaAlunosComCandidatura() + data.listaAlunosSemCandidatura() + data.listaAlunosAutoPropostos() + data.propostasSemAtribuicao();
    }

    @Override
    public String listaAssociacao(int opcao){
        return data.listaAssociacao(opcao);
    }

    @Override
    public String listaAssociacao(int opcao, int ordem){
        return data.listaAssociacao(opcao, ordem);
    }

    @Override
    public String consultaCandidatura(long num){
        return data.consultaCandidatura(num);
    }

    @Override
    public void changeState(){
        context.changeState(new FaseQuatro(context, data));
    }

    @Override
    public FaseState getState() {
        return FaseState.FASE_TRES;
    }
}
