package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.FaseData;

import java.util.List;
import java.util.Map;

/** Cria uma classe da Fase quatro da máquina de estados.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class FaseQuatro extends FaseStateAdapter{


    /** Cria uma classe da Fase quatro da máquina de estados.
     * @param context Classe que liga a interface com utilizador à lógica.
     * @param data Classe que contém os dados da aplicação.
     */
    protected FaseQuatro(FaseContext context, FaseData data) {
        super(context, data);
    }
    @Override
    public void atribuicaoAutoDocentesProp(){
        data.atribuicaoDocentesProp();
    }

    @Override
    public String listarAlunos(){
        return data.listaAlunosComCandidatura() + data.listaAlunosSemCandidatura() + data.listaAlunosAutoPropostos()+ data.propostasSemAtribuicao();
    }


    @Override
    public String consultaDocente(String docente){
        return data.consultaDocente(docente);
    }

    @Override
    public String consultaProposta(String codigo){
        return data.consultaProposta(codigo);
    }

    @Override
    public void alteraOrientador(String docenteAntigo, String docenteNovo) {
        data.alteraOrientador(docenteAntigo,docenteNovo);
    }

    @Override
    public void adicionaPropDocente(String email, String prop){
        data.adicionaPropDocente(email, prop);
    }


    @Override
    public String consultaOrientadores(String email){
        return data.consultaOrientadores(email);
    }

    @Override
    public void eliminarOrientadorAssociado(String email){
        data.eliminarOrientadorAssociado(email);
    }

    @Override
    public String listarOrientadores(int opcao){
        return data.listarOrientadores(opcao);
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
    public void backState(){
        context.changeState(new FaseTres(context, data));
    }

    @Override
    public void changeState(){
        context.changeState(new FaseCinco(context, data));
    }

    @Override
    public FaseState getState() {
        return FaseState.FASE_QUATRO;
    }
}
