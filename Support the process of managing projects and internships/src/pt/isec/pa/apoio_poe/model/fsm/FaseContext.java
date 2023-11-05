package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.*;

import java.io.Serializable;
import java.util.*;

/** Cria uma classe que comunica a interface com a lógica.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */
public class FaseContext implements Serializable {
    static final long serialVersionUID = 100L;

    /** Máquina de estados.
     */
    private IFaseState state;
    /** Informação dos dados da aplicacção.
     */
    private FaseData info;

    /** Cria uma classe que comunica a interface com a lógica.
     */
    public FaseContext(){
        info = new FaseData();
    }
    /** Inicia o primeiro estado.
     */
    public void start(){
        changeState(new FaseUm(this, info));
    }

    /** Devolve o estado atual da máquina de estados.
     * @return Estado da máquina de estado,
     */
    public FaseState getState(){
        if (state == null)
            return null;
        return state.getState();
    }

    /** Adiciona um aluno.
     * @param aluno Aluno a adicionar.
     * @return Resultado da operação.
     */
    public boolean adiciona(Alunos aluno){
        return state.addAluno(aluno);
    }
    /** Remove um aluno.
     * @param numeroALuno Número do aluno a remover.
     * @return Resultado da operação.
     */
    public boolean remove(long numeroALuno){
        return state.removeAluno(numeroALuno);
    }
    /** Consulta um aluno.
     * @param numeroAluno Número do aluno a consultar.
     * @return String com os dados do aluno.
     */
    public String consulta(long numeroAluno){
        return state.consultaAluno(numeroAluno);
    }
    /** Edita um aluno.
     * @param numeroAluno Número do aluno a editar.
     * @param  novoNome Novo nome do aluno.
     */
    public void editNome(long numeroAluno, String novoNome){
        state.editNomeAluno(numeroAluno,novoNome);
    }

    /** Edita um aluno.
     * @param numeroAluno Número do aluno a editar.
     * @param  novoEmail Novo email do aluno.
     */
    public void editEmail(long numeroAluno, String novoEmail){
        state.editNomeEmail(numeroAluno,novoEmail);
    }
    /** Edita um aluno.
     * @param numeroAluno Número do aluno a editar.
     * @param  novoSCurso Nova sigla do curso do aluno.
     */
    public void editSiglaCurso(long numeroAluno, String novoSCurso){
        state.editSiglaCurso(numeroAluno,novoSCurso);
    }

    /** Edita um aluno.
     * @param numeroAluno Número do aluno a editar.
     * @param  novoSRamo Nova sigla do ramo do aluno.
     */
    public void editSiglaRamo(long numeroAluno, String novoSRamo){
        state.editSiglaRamo(numeroAluno,novoSRamo);
    }
    /** Edita um aluno.
     * @param numeroAluno Número do aluno a editar.
     * @param classficacao Nova classificação do aluno.
     */
    public void editClassificacao(long numeroAluno, double classficacao){
        state.editClassificacao(numeroAluno, classficacao);
    }
    /** Edita um aluno.
     * @param numeroAluno Número do aluno a editar.
     */
    public void editPossibilidade(long numeroAluno){
        state.editPossibilidade(numeroAluno);
    }

    /** Obtem a lista de alunos.
     * @return Devolve a lista de alunos.
     */
    public String listarAlunos(){
        return state.listarAlunos();
    }
    /** Obtem a lista de propostas.
     * @param crit Opção de restrinção.
     * @param opcao Opção de restrinção.
     * @return Devolve a lista de propostas.
     */
    public String consultaPropostas(int opcao, int crit){
        return state.consultaPropostas(opcao, crit);
    }
    /** Obtém o estado de uma fase.
     * @param fase Número da fase.
     * @return Estado da fase.
     */
    public boolean getFechado(int fase){
        return state.getFechado(fase);
    }
    /** Obtém o estado de uma fase anterior.
     * @param fase Número da fase.
     * @return Estado da fase.
     */
    public boolean getFaseAnteriorFechado(int fase){
        return state.getFaseAnteriorFechado(fase);
    }
    /** Altera o estado de uma fase.
     * @param estado Novo estado.
     * @param fase Número da fase.
     */
    public void setFechado(boolean estado, int fase){
        state.setFechado(estado, fase);
    }

    /** Consulta um docente.
     * @param nome Email do docente.
     * @return dados do docente.
     */
    public String consultaDocente(String nome){
       return state.consultaDocente(nome);
    }
    /** Adiciona um docente.
     * @param docente Docente a adiconar.
     * @return Resultado da operação.
     */
    public boolean adicionaDocente(Docentes docente){
        return state.addDocente(docente);
    }
    /** Remove um docente.
     * @param nome Email do docente.
     * @return Resultado da operação.
     */
    public boolean removeDocente(String nome){
        return state.removeDocente(nome);
    }
    /** Edita um docente.
     * @param nome Email do docente.
     * @param nomeNovo Novo Nome do docente.
     */
    public void editNomeDocente(String nome, String nomeNovo) {
        state.editNomeDocente(nome, nomeNovo);
    }

    /** Avança um estado na máquina de estados.
     */
    public void mudaEstado(){
        state.changeState();
    }

    /** Obtém o número de docentes.
     * @return Número de docentes.
     */
    public int getNDocentes(){
        return info.getNDocentes();
    }
    /** Obtém o número de alunos.
     * @return Número de aluno.
     */
    public int getNAlunos(){
        return info.getNAlunos();
    }
    /** Obtém o número de candidaturas.
     * @return Número de candidaturas.
     */
    public int getNCandidaturas(){
        return info.getNCandidaturas();
    }
    /** Obtém o número de alunos com proposta associada.
     * @return Número de alunos com proposta associada.
     */
    public int getNAlunosPropAssociados(){
        return info.getNAlunosPropAssociados();
    }
    /** Obtém o número de docentes com proposta associada.
     * @return Número de docentes com proposta associada.
     */
    public int getNDocentesPropAssociados(){
        return info.getNDocentesPropAssociados();
    }

    /** Obtém o número de propostas do ramo de DA.
     * @return Número de propostas do ramo de DA.
     */
    public int getNDAProps(){
     return info.getNDAProps();
    }
    /** Obtém o número de propostas do ramo de SI.
     * @return Número de propostas do ramo de SI.
     */
    public int getNSIProps(){
        return info.getNSIProps();
    }
    /** Obtém o número de propostas do ramo de RAS.
     * @return Número de propostas do ramo de RAS.
     */
    public int getNRASProps(){
        return  info.getNRASProps();
    }

    /** Obtém o número de propostas.
     * @return Número de propostas.
     */
    public int getNPropostas(){
        return info.getNPropostas();
    }
    /** Adiciona uma proposta.
     * @param prop Proposta a adiconar.
     * @return Resultado da operação.
     */
    public boolean adicionaProposta(Propostas prop){
        return state.adicionaProposta(prop);
    }
    /** Obtém o tipo de proposta.
     * @param codigo Codigo da proposta.
     * @return Tipo da proposta.
     */
    public String getTipoPropostas(String codigo){
        return info.getTipoPropostas(codigo);
    }
    /** Consulta uma proposta.
     * @param codigo Codigo da proposta.
     * @return Dados da proposta.
     */
    public String consultaProposta(String codigo){
        return state.consultaProposta(codigo);
    }
    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param titulo Novo titulo.
     */
    public void editTituloPropostas(String codigo, String titulo){
         state.editTituloProp(codigo, titulo);
    }
    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param novo Novo numero do estudante.
     */
    public void editNumEstudante(String codigo, long novo){
        state.editNumEstudantePropostas(codigo, novo);
    }
    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param entidade Nova entidade de acolhimento.
     */
    public void editEntidadePropostas(String codigo, String entidade){
        state.editEntidadeAcolhimento(codigo, entidade);
    }
    /** Verifica alunos com candidaturas.
     * @return Resultado da operação.
     */
    public boolean verificaAlunosCandidaturas(){
        return info.verificaAlunosCandidaturas();
    }

    /** Volta um estado atrás na máquina de estados.
     */
    public void backState(){
        state.backState();
    }
    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param email Novo email do docente da proposta.
     */
    public void editProfEmailPropostas(String codigo, String email){
        state.editProfEmailPropostas(codigo, email);

    }
    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param ramo Novo ramo da proposta.
     */
    public void editRamoPropostas(String codigo, String ramo){
        state.editRamoPropostas(codigo, ramo);
    }
    /** Remove uma proposta.
     * @param codigo Codigo da proposta.
     * @return Resultado da operação.
     */
    public boolean removeProposta(String codigo){
        return state.removeProposta(codigo);
    }
    /** Obtém o aluno dassociado a uma proposta.
     * @param numAluno Número do aluno.
     * @return Dados do aluno.
     */
    public String getAlunoProp(long numAluno){
        return info.getAlunoProp(numAluno);
    }
    /** Obtém uma lista dos alunos não atribuidos.
     * @return Set com os alunos não atribuidos.
     */
    public Set<Alunos> getAlunoNaoAtribuidos() {
       return info.getAlunoNaoAtribuidos();
    }
    /** Obtém uma lista das propostas não atribuidas.
     * @return Set com as propostas não atribuidas.
     */
    public Set<Propostas> getPropNaoAtribuidos() {
        return info.getPropNaoAtribuidos();
    }
    /** Obtém uma lista das propostas não atribuidas.
     * @return Set com as propostas não atribuidas.
     */
    public Set<Propostas> getPropOrientadorNaoAtribuidos() {
        return info.getPropOrientadorNaoAtribuidos();
    }
    /** Obtém uma lista dos alunos atribuidos.
     * @return Set com os alunos atribuidos.
     */
    public Set<Alunos> getAlunoAtribuidos() {
        return info.getAlunoAtribuidos();
    }
    /** Adiciona uma candidatura.
     * @param candidatura Nova candidatura.
     * @return Resultado da operação.
     */
    public boolean adicionaCandidatura(Candidaturas candidatura){
        return state.adicionaCandidatura(candidatura);
    }
    /** Consulta uma candidatura.
     * @param num Número do aluno
     * @return Dados da candidatura.
     */
    public String consultaCandidatura(long num){
        return state.consultaCandidatura(num);
    }
    /** Remove uma candidatura.
     * @param num Número do aluno.
     * @return Resultado da operação.
     */
    public boolean removeCandidatura(long num){
        return state.removeCandidatura(num);
    }
    /** Edita uma candidatura.
     * @param num Número do aluno.
     * @param lista Nova lista de propostas.
     */
    public void editCandidatura(long num, List<String> lista){
        state.editCandidatura(num, lista);
    }
    /** Verifica se uma proposta está atribuida.
     * @param codigo Código da proposta.
     * @return Resultado da operação.
     */
    public boolean propostaAtribuida(String codigo){
        return info.propostaAtribuida(codigo);
    }
    /** Atribui automaticamente as proposts aos docentes.
     */
    public void atribuicaoAutoProp(){
        state.atribuicaoAutoProp();
    }
    /** Lista associação dos alunos.
     * @param opcao Opção de critério.
     * @return Lista da associação.
     */
    public String listaAssociacao(int opcao){
        return state.listaAssociacao(opcao);
    }
    /** Lista associação dos alunos.
     * @param opcao Opção de critério.
     * @param ordem Opção de critério.
     * @return Lista da associação.
     */
    public String listaAssociacao(int opcao, int ordem){
        return state.listaAssociacao(opcao, ordem);
    }
    /** Ordena os docentes por número de propostas.
     * @return Mapa com os docentes ordenados.
     */
    public Map<Docentes, List<Propostas>> getDocentesOrdProp(){
        return info.getDocentesOrdProp();
    }
    /** Obtém uma lista dos alunos.
     * @return Set com os alunos.
     */
    public Set<Alunos> getAlunos() {
        return info.getAlunos();
    }
    /** Obtém uma lista dos docentes.
     * @return Set com os docentes.
     */
    public Set<Docentes> getDocentes() {
        return info.getDocentes();
    }
    /** Obtém uma lista dos propostas.
     * @return Set com os propostas.
     */
    public Set<Propostas> getPropostas() {
        return info.getPropostas();
    }
    /** Obtém uma lista dos candidaturas.
     * @return Set com os candidaturas.
     */
    public Set<Candidaturas> getCandidaturas() {
        return info.getCandidaturas();
    }


    /** Obtém lista docentes sem propostas.
     * @return String lista de docentes propostas.
     */
    public String listaPropsSemDocente(){
        return info.listaPropsSemDocente();
    }
    /** Obtém lista alunos sem atribuição.
     * @return String lista de alunos sem atribuição.
     */
    public String alunosSemAtribuicao(){
        return info.alunosSemAtribuicao();
    }
    /** Obtém lista propostas sem atribuição.
     * @return String lista de propostas sem atribuição.
     */
    public String propostasSemAtribuicao(){
        return info.propostasSemAtribuicao();
    }
    /** Verifica se um aluno tem atribuição.
     * @param num número do aluno.
     * @return boolean resultado da operação.
     */
    public boolean isSemAtribuicao(long num){
        return info.isSemAtribuicao(num);
    }
    /** Verifica se uma proposta tem atribuição.
     * @param prop código da proposta.
     * @return boolean resultado da operação.
     */
    public boolean isPropSemAtribuicao(String prop){
        return info.isPropSemAtribuicao(prop);
    }
    /** Remove uma proposta atribuida ao aluno.
     * @param num número do aluno.
     * @return boolean resultado da operação.
     */
    public boolean removePropostaDoAluno(long num){
        return info.removePropostaDoAluno(num);
    }
    /** Consulta propostas de acordo com várias restrições da fase três.
     * @param opcao opção de restrições.
     * @param crit opção de restrições.
     * @return String listas de propostas de acordo com as opções.
     */
    public String listaPropostasF3(int opcao, int crit){
        return info.listaPropostasF3(opcao, crit);
    }
    /** Obtém uma lista das entidades ordenadas.
     * @return Mapa das entidades ordenados por número de propostas.
     */
    public  LinkedHashMap<String, Integer> getEntidadeTop(){
        return info.getEntidadeTop();
    }
    /** Atribui as propostas aos alunos que estejam disponiveis.
     * @return  Mapa dos alunos empatados.
     */
    public Map<String, List<Long>> atribuicaoPropDisponiveis(){
            return state.atribuicaoPropDisponiveis();
    }
    /** Obtem o numero de alunos sem atruibução.
     * @return int numero de alunos sem atribuição.
     */
    public int getAlunosSemAtribuicao(){
        return info.getAlunosSemAtribuicao();
    }
    /** Atribui o aluno à propostas.
     * @param numALuno Número do aluno.
     * @param propCodigo Código da proposta.
     * @return Resultado da operação.
     */
    public boolean atribuicaoManual(long numALuno, String propCodigo){
        return state.atribuicaoManual(numALuno, propCodigo);
    }

    /** Verifica se o ramo existe.
     * @param ramo String com o ramo.
     * @return boolean se o ramo existe ou não.
     */
    public boolean checkRamo(String ramo){
        return info.checkRamo(ramo);
    }
    /** Verifica se o curso existe.
     * @param curso String com o curso.
     * @return boolean se o curso existe ou não.
     */
    public boolean checkCurso(String curso){
        return info.checkCurso(curso);
    }
    /** Atribui a proposta ao aluno.
     * @param numALuno numero do aluno a associar.
     * @param  proposta proposta a associar.
     */
    public void atribuiAlunoEmpatado(long numALuno, String proposta){
        info.atribuiAlunoEmpatado(numALuno, proposta);
    }

    /** Atribui automaticamente as proposts aos docentes.
     */
    public void atribuicaoAutoDocentesProp(){
        state.atribuicaoAutoDocentesProp();
    }

    /** Verifica se uma proposta tem uma proposta associada.
     * @param prop codigo da proposta.
     * @return boolean resultado da operação.
     */
    public boolean isPropAlunoAtribuida(String prop){
        return info.isPropAlunoAtribuida(prop);
    }
    /** Adicona uma proposta a um docente.
     * @param email Email do docentes.
     * @param prop Codigo da proposta..
     */
    public void adicionaPropDocente(String email, String prop){
        state.adicionaPropDocente(email,prop);
    }

    /** Verifica se um docente tem uma proposta associada.
     * @param docente email do docente.
     * @param prop codigo da proposta.
     * @return boolean resultado da operação.
     */
    public boolean isPropDocenteAssociado(String docente, String prop){
        return info.isPropDocenteAssociado(docente, prop);
    }
    /** Verifica se um orientador tem uma proposta associada.
     * @param email email do docente.
     * @return boolean resultado da operação.
     */
    public boolean isOrientadorAtribuido(String email){
        return info.isOrientadorAtribuido(email);
    }
    /** Altera o orientador de uma proposta.
     * @param docenteAntigo docente anteriormente associado.
     * @param docenteNovo novo docente para associar.
     */
    public void alteraOrientador(String docenteAntigo, String docenteNovo) {
        state.alteraOrientador(docenteAntigo,docenteNovo);
    }
    /** Verifica se o ramo da proposta de um aluno.
     * @param num número do aluno.
     * @param nome código da proposta.
     * @return boolean resultado da operação.
     */
    public boolean verificaRamoPropAluno(long num, String nome){
        return info.verificaRamoPropAluno(num, nome);
    }
    /** Consulta orientadores.
     * @param email Email do orientador.
     * @return Dados do orientador.
     */
        public String consultaOrientadores(String email){
        return state.consultaOrientadores(email);
    }
    /** Obtém uma lista dos docentes atribuidos.
     * @return Set com os docentes atribuidos.
     */
    public Set<Docentes> getDocentesAtribuidos() {
        return info.getDocentesAtribuidos();
    }
    /** Elimina orientadores.
     * @param email Email do orientador.
     */
    public void eliminarOrientadorAssociado(String email){
        state.eliminarOrientadorAssociado(email);
    }
    /** Elimina proposta orientadores.
     * @param email Email do orientador.
     * @param prop propostas a eliminar
     */
    public void eliminarOrientadorPropAssociado(String email, String prop){
       info.eliminarOrientadorPropAssociado(email,prop);
    }
    /** Obtém a lista de propostas de um orientador.
     * @param email Email do docente.
     * @return  Lista das propostas.
     */
    public List<Propostas> getCodigoDocenteOrientador(String email){
        return info.getCodigoDocenteOrientador(email);
    }
    /** Listagem final.
     * @param opcao de critério.
     * @return Dados da fase cinco.
     */
    public String listaFinalF5(int opcao){
        return state.listaFinalF5(opcao);
    }
    /** Lista dos orientadores.
     * @param opcao Opção de critério.
     * @return Dados dos orientadores.
     */
    public String listarOrientadores(int opcao){
        return state.listarOrientadores(opcao);
    }

    /** Avança um estado na máquina de estados.
     */
    void changeState(IFaseState newState) {
        state = newState;
    }

    /** Exporta para um ficheiro a informção da primeira fase.
     * @param nome Nome do ficheiro.
     */
    public void exportFase1(String nome){
        info.exportFase1(nome);
    }
    /** Exporta para um ficheiro a informção da segunda fase.
     * @param nome Nome do ficheiro.
     */
    public void exportFase2(String nome){
        info.exportFase2(nome);
    }
    /** Exporta para um ficheiro a informção da terceira fase.
     * @param nome Nome do ficheiro.
     */
    public void exportarFase3(String nome){
        info.exportarFase3(nome);
    }

    /** Exporta para um ficheiro a informção da quarta e quinta fase.
     * @param nome Nome do ficheiro.
     */
    public void exportarFase4E5(String nome){
        info.exportarFase4E5(nome);
    }




}
