package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.data.Docentes;
import pt.isec.pa.apoio_poe.model.data.FaseData;
import pt.isec.pa.apoio_poe.model.data.Propostas;

/** Cria uma classe da Fase um da máquina de estados.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class FaseUm extends FaseStateAdapter{

    /** Cria uma classe da Fase um da máquina de estados.
     * @param context Classe que liga a interface com utilizador à lógica.
     * @param data Classe que contém os dados da aplicação.
     */
    protected FaseUm(FaseContext context, FaseData data) {
        super(context, data);
    }


    @Override
    public boolean addAluno(Alunos aluno){
        return data.addAluno(aluno);
    }

    @Override
    public String consultaAluno(long numEstudante){
        return data.consultaAluno(numEstudante);
    }

    @Override
    public void editNomeAluno(long numero, String novoNome){
        data.editNomeAluno(numero, novoNome);
    }

    @Override
    public void editNomeEmail(long numero, String novoEmail){
        data.editNomeEmail(numero, novoEmail);
    }

    @Override
    public void editSiglaCurso(long numero, String novaSigla){
        data.editSiglaCurso(numero, novaSigla);
    }

    @Override
    public void editSiglaRamo(long numero, String novaSigla){
        data.editSiglaRamo(numero, novaSigla);
    }

    @Override
    public void editClassificacao(long numero, double classificacao){
        data.editClassificacao(numero, classificacao);
    }

    @Override
    public void editPossibilidade(long numero){
        data.editPossibilidade(numero);
    }

    @Override
    public boolean removeAluno(long numEstudante){
        return data.removeAluno(numEstudante);
    }

    @Override
    public String consultaDocente(String docente){
        return data.consultaDocente(docente);
    }

    @Override
    public boolean addDocente(Docentes docente){
        return data.addDocente(docente);
    }

    @Override
    public boolean removeDocente(String nome){
        return data.removeDocente(nome);
    }

    @Override
    public void editNomeDocente(String nome, String novoNome){
        data.editNomeDocente(nome, novoNome);
    }

    @Override
    public boolean adicionaProposta(Propostas prop){
        return data.adicionaProposta(prop);
    }

    @Override
    public String consultaProposta(String codigo){
        return data.consultaProposta(codigo);
    }

    @Override
    public boolean removeProposta(String codigo){
        return data.removeProposta(codigo);
    }

    @Override
    public void editNumEstudantePropostas(String codigo, long novoNumero){
        data.editNumEstudantePropostas(codigo, novoNumero);
    }

    @Override
    public void editEntidadeAcolhimento(String codigo, String novaEntidade){
        data.editEntidadeProposta(codigo, novaEntidade);
    }

    @Override
    public void editRamoPropostas(String codigo, String ramo){
        data.editRamoProposta(codigo, ramo);
    }

    @Override
    public void editTituloProp(String codigo, String titulo){
        data.editTituloProposta(codigo, titulo);
    }

    @Override
    public String listarAlunos(){
        return data.listaAlunosComCandidatura() + data.listaAlunosSemCandidatura() + data.listaAlunosAutoPropostos()+ data.propostasSemAtribuicao();
    }

    @Override
    public void changeState(){
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
    public FaseState getState() {
        return FaseState.FASE_UM;
    }
}
