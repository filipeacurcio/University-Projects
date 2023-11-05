package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Candidaturas;
import pt.isec.pa.apoio_poe.model.data.FaseData;

import java.util.ArrayList;
import java.util.List;
/** Cria uma classe da Fase dois da máquina de estados.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class FaseDois extends FaseStateAdapter{

    /** Cria uma classe da Fase dois da máquina de estados.
     * @param context Classe que liga a interface com utilizador à lógica.
     * @param data Classe que contém os dados da aplicação.
     */
    protected FaseDois(FaseContext context, FaseData data) {
        super(context, data);
    }

    @Override
    public void setFechado(boolean fechada, int fase) {
        data.setFechada(fechada, fase);
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
    public String consultaPropostas(int opcao ,int crit){
       return data.consultaPropostas(opcao, crit);
    }

    @Override
    public String listarAlunos(){
        return data.listaAlunosComCandidatura() + data.listaAlunosSemCandidatura() + data.listaAlunosAutoPropostos() + data.propostasSemAtribuicao();
    }

    @Override
    public boolean adicionaCandidatura(Candidaturas candidatura){
        return data.addCandidatura(candidatura);
    }
    @Override
    public String consultaProposta(String codigo){
        return data.consultaProposta(codigo);
    }
    @Override
    public String consultaCandidatura(long num){
        return data.consultaCandidatura(num);
    }
    @Override
    public boolean removeCandidatura(long num){
        return data.removeCandidatura(num);
    }

    @Override
    public String consultaDocente(String docente){
        return data.consultaDocente(docente);
    }

    @Override
    public void editCandidatura(long num, List<String> list){
        data.editaCandidatura(num, (ArrayList<String>) list);
    }


    @Override
    public void changeState(){
        context.changeState(new FaseTres(context, data));
    }

    @Override
   public void backState(){
        context.changeState(new FaseUm(context, data));
    }


    @Override
    public FaseState getState() {
        return FaseState.FASE_DOIS;
    }
}
