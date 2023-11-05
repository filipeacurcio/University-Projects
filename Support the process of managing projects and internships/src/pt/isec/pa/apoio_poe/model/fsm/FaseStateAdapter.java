package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/** Cria um classe intermadiária entre a interface e as fases.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public abstract class FaseStateAdapter implements IFaseState, Serializable {
    static final long serialVersionUID = 100L;
    /** Máquina de estados.
     */
    protected FaseContext context;
    /** Dados da aplicação.
     */
    protected FaseData data;

    /** Cria um classe intermadiária entre a interface e as fases.
     * @param context  máquina de estados.
     * @param data Dados da aplicação.
     */
    protected FaseStateAdapter(FaseContext context, FaseData data){
        this.context = context;
        this.data = data;
    }

    @Override
    public String consultaPropostas(int opcao ,int crit){
        return null;
    }

    @Override
    public String listarAlunos(){
        return null;
    }
    @Override
    public void changeState(){}

    @Override
    public boolean getFechado(int fase){
        return false;
    }

    @Override
    public void setFechado(boolean state, int fase){
    }

    @Override
   public boolean getFaseAnteriorFechado(int fase){
        return false;
    }

    @Override
    public boolean addAluno(Alunos aluno){
        return false;
    }

    @Override
    public String consultaAluno(long numEstudante){
        return null;
    }

    @Override
    public void editNomeAluno(long numero, String novoNome){}

    @Override
    public void editNomeEmail(long numero, String novoEmail){}

    @Override
    public void editSiglaCurso(long numero, String novaSigla){}

    @Override
    public void editSiglaRamo(long numero, String novaSigla){}

    @Override
    public void editClassificacao(long numero, double classificacao){}

    @Override
    public void editPossibilidade(long numero){}

    @Override
    public void adicionaPropDocente(String email, String prop){

    }


    @Override
    public Map<String, List<Long>> atribuicaoPropDisponiveis(){
        return null;
    }
    @Override
    public boolean removeAluno(long numEstudante){return false;}

    @Override
    public String consultaDocente(String nome){
        return null;
    }

    @Override
    public boolean addDocente(Docentes docente){
        return false;
    }
    @Override
    public boolean removeDocente(String nome){
        return false;
    }

    @Override
    public void editNomeDocente(String nome, String novoNome){
    }
    @Override
    public String consultaProposta(String codigo){
        return null;
    }
    @Override
    public boolean removeProposta(String codigo){
        return false;
    }
    @Override
    public void editEmailDocente(String nome, String novoEmail){}

    @Override
    public boolean adicionaProposta(Propostas prop){return false;}

    @Override
    public void editNumEstudantePropostas(String codigo, long novoNumero){

    }

    @Override
    public boolean adicionaCandidatura(Candidaturas candidatura){
        return false;
    }

    @Override
    public String consultaCandidatura(long num){
        return null;
    }

    @Override
    public boolean removeCandidatura(long num){
        return false;
    }

    @Override
    public void editCandidatura(long num, List<String> list){}

    @Override
    public void backState(){

    }


    @Override
    public void editTituloProp(String codigo, String titulo){

    }
    @Override
    public void editProfEmailPropostas(String codigo, String email){

    }

    @Override
    public void editRamoPropostas(String codigo, String ramo){

    }
    @Override
    public void editEntidadeAcolhimento(String codigo, String titulo){
    }

    @Override
    public boolean atribuicaoManual(long numALuno, String propCodigo){
        return false;
    }

    @Override
    public void atribuicaoAutoProp(){

    }

    @Override
    public String listaAssociacao(int opcao){
     return null;
    }

    @Override
   public String listaAssociacao(int opcao, int ordem){
        return null;
    }


    @Override
    public void atribuicaoAutoDocentesProp(){

    }

    @Override
    public void alteraOrientador(String docenteAntigo, String docenteNovo) {
    }

    @Override
    public String consultaOrientadores(String email){
        return null;
    }

    @Override
    public void eliminarOrientadorAssociado(String email){
    }

    @Override
    public String listaFinalF5(int opcao){
        return null;
    }

    @Override
    public String listarOrientadores(int opcao){
        return null;
    }

}
