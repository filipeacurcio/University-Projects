package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.data.Candidaturas;
import pt.isec.pa.apoio_poe.model.data.Docentes;
import pt.isec.pa.apoio_poe.model.data.Propostas;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/** Cria uma classe que comunica a interface com a lógica.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class FaseContextManager {
    /** Nome do para o listener dos estados.
     */
    public static final String PROP_STATE = "state";
    /** Nome do para o listener dos dados.
     */
    public static final String PROP_DATA  = "data";
    static final String FILENAME = "gestaoEP.dat";
    private  PropertyChangeSupport pcs;
    private FaseContext faseContext;
    CommandManager cmOrientadores, cmPropostas;

    /** Cria uma classe que comunica a interface com a lógica.
     * @param faseContext Classe interior da lógica.
     */
    public FaseContextManager(FaseContext faseContext){
        this.faseContext = faseContext;
        pcs = new PropertyChangeSupport(this);
        cmOrientadores = new CommandManager();
        cmPropostas = new CommandManager();
    }
    /** Inicia o primeiro estado.
     */
    public void start(){
        faseContext.start();
    }

    /** Adiciona listeners para os UI.
     * @param property Proporiadade.
     * @param listener Tipo de listener.
     */
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }
    /** Devolve o estado atual da máquina de estados.
     * @return Estado da máquina de estados.
     */
    public FaseState getState(){
        return faseContext.getState();
    }

    /** Adiciona um aluno.
     * @param aluno Aluno a adicionar.
     * @return Resultado da operação.
     */
    public boolean adiciona(Alunos aluno){
        boolean aux =  faseContext.adiciona(aluno);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return aux;
    }
    /** Verifica se existe redos possiveis.
     * @return resultado da aplicação.
     */
    public boolean hasRedoOrientadores(){
       return cmOrientadores.hasRedo();
    }
    /** Verifica se existe undos Possiveis.
     * @return resultado da aplicação.
     */
    public boolean hasUndoOrientadores(){
        return cmOrientadores.hasUndo();
    }
    /** Verifica se existe redos possiveis.
     * @return resultado da aplicação.
     */
    public boolean hasRedoProps(){
        return cmPropostas.hasRedo();
    }
    /** Verifica se existe undos Possiveis.
     * @return resultado da aplicação.
     */
    public boolean hasUndoProps(){
        return cmPropostas.hasUndo();
    }
    /** Remove um aluno.
     * @param numeroALuno Número do aluno a remover.
     * @return Resultado da operação.
     */
    public boolean remove(long numeroALuno){
        boolean aux = faseContext.remove(numeroALuno);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return aux;
    }
    /** Consulta um aluno.
     * @param numeroAluno Número do aluno a consultar.
     * @return String com os dados do aluno.
     */
    public String consulta(long numeroAluno){
        return faseContext.consulta(numeroAluno);
    }

    /** Edita um aluno.
     * @param numeroAluno Número do aluno a editar.
     * @param  novoNome Novo nome do aluno.
     */
    public void editNome(long numeroAluno, String novoNome){
        faseContext.editNome(numeroAluno,novoNome);
        pcs.firePropertyChange(PROP_DATA,null,null);
    }
    /** Edita um aluno.
     * @param numeroAluno Número do aluno a editar.
     * @param  novoEmail Novo email do aluno.
     */
    public void editEmail(long numeroAluno, String novoEmail){
        faseContext.editEmail(numeroAluno,novoEmail);
        pcs.firePropertyChange(PROP_DATA,null,null);
    }
    /** Edita um aluno.
     * @param numeroAluno Número do aluno a editar.
     * @param  novoSCurso Nova sigla do curso do aluno.
     */
    public void editSiglaCurso(long numeroAluno, String novoSCurso){
        faseContext.editSiglaCurso(numeroAluno,novoSCurso);
        pcs.firePropertyChange(PROP_DATA,null,null);
    }
    /** Edita um aluno.
     * @param numeroAluno Número do aluno a editar.
     * @param  novoSRamo Nova sigla do ramo do aluno.
     */
    public void editSiglaRamo(long numeroAluno, String novoSRamo){
        faseContext.editSiglaRamo(numeroAluno,novoSRamo);
        pcs.firePropertyChange(PROP_DATA,null,null);
    }

    /** Edita um aluno.
     * @param numeroAluno Número do aluno a editar.
     * @param classficacao Nova classificação do aluno.
     */
    public void editClassificacao(long numeroAluno, double classficacao){
        faseContext.editClassificacao(numeroAluno, classficacao);
        pcs.firePropertyChange(PROP_DATA,null,null);
    }
    /** Edita um aluno.
     * @param numeroAluno Número do aluno a editar.
     */
    public void editPossibilidade(long numeroAluno){
        faseContext.editPossibilidade(numeroAluno);
        pcs.firePropertyChange(PROP_DATA,null,null);

    }
    /** Obtem a lista de alunos.
     * @return Devolve a lista de alunos.
     */
    public String listarAlunos(){
        return faseContext.listarAlunos();
    }
    /** Obtem a lista de propostas.
     * @param crit Opção de restrinção.
     * @param opcao Opção de restrinção.
     * @return Devolve a lista de propostas.
     */
    public String consultaPropostas(int opcao, int crit){
        return faseContext.consultaPropostas(opcao, crit);
    }

    /** Obtém o estado de uma fase.
     * @param fase Número da fase.
     * @return Estado da fase.
     */
    public boolean getFechado(int fase){
        return faseContext.getFechado(fase);
    }
    /** Obtém o estado de uma fase anterior.
     * @param fase Número da fase.
     * @return Estado da fase.
     */
    public boolean getFaseAnteriorFechado(int fase){
        return faseContext.getFaseAnteriorFechado(fase);
    }
    /** Altera o estado de uma fase.
     * @param estado Novo estado.
     * @param fase Número da fase.
     */
    public void setFechado(boolean estado, int fase){
        faseContext.setFechado(estado, fase);
        pcs.firePropertyChange(PROP_DATA,null,null);
    }
    /** Obtém o número de candidaturas.
     * @return Número de candidaturas.
     */
    public int getNCandidaturas(){
        return faseContext.getNCandidaturas();
    }
    /** Obtém o número de alunos com proposta associada.
     * @return Número de alunos com proposta associada.
     */
    public int getNAlunosPropAssociados(){
        return faseContext.getNAlunosPropAssociados();
    }
    /** Obtém o número de docentes com proposta associada.
     * @return Número de docentes com proposta associada.
     */
    public int getNDocentesPropAssociados(){
        return faseContext.getNDocentesPropAssociados();
    }

    /** Consulta um docente.
     * @param nome Email do docente.
     * @return dados do docente.
     */
    public String consultaDocente(String nome){
        return faseContext.consultaDocente(nome);
    }
    /** Adiciona um docente.
     * @param docente Docente a adiconar.
     * @return Resultado da operação.
     */
    public boolean adicionaDocente(Docentes docente){
        boolean aux =  faseContext.adicionaDocente(docente);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return aux;
    }
    /** Obtém lista docentes sem propostas.
     * @return String lista de docentes propostas.
     */
    public String listaPropsSemDocente(){
        return faseContext.listaPropsSemDocente();
    }
      /** Remove um docente.
     * @param nome Email do docente.
     * @return Resultado da operação.
     */
    public boolean removeDocente(String nome){
        boolean aux = faseContext.removeDocente(nome);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return aux;
    }
    /** Edita um docente.
     * @param nome Email do docente.
     * @param nomeNovo Novo Nome do docente.
     */
    public void editNomeDocente(String nome, String nomeNovo) {
        pcs.firePropertyChange(PROP_DATA,null,null);
        faseContext.editNomeDocente(nome, nomeNovo);
    }
    /** Avança um estado na máquina de estados.
     */
    public void mudaEstado(){
        faseContext.mudaEstado();
        pcs.firePropertyChange(PROP_STATE,null, faseContext.getState());
    }
    /** Obtém o número de alunos.
     * @return Número de aluno.
     */
    public int getNAlunos(){
        return faseContext.getNAlunos();
    }
    /** Obtém o número de propostas.
     * @return Número de propostas.
     */
    public int getNPropostas(){
        return faseContext.getNPropostas();
    }
    /** Adiciona uma proposta.
     * @param prop Proposta a adiconar.
     * @return Resultado da operação.
     */
    public boolean adicionaProposta(Propostas prop){
        boolean aux =  faseContext.adicionaProposta(prop);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return aux;
    }
    /** Obtém o tipo de proposta.
     * @param codigo Codigo da proposta.
     * @return Tipo da proposta.
     */
    public String getTipoPropostas(String codigo){
        return faseContext.getTipoPropostas(codigo);
    }
    /** Consulta uma proposta.
     * @param codigo Codigo da proposta.
     * @return Dados da proposta.
     */
    public String consultaProposta(String codigo){
        return faseContext.consultaProposta(codigo);
    }
    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param titulo Novo titulo.
     */
    public void editTituloPropostas(String codigo, String titulo){
        faseContext.editTituloPropostas(codigo, titulo);
        pcs.firePropertyChange(PROP_DATA,null,null);

    }

    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param novo Novo numero do estudante.
     */
    public void editNumEstudante(String codigo, long novo){
        faseContext.editNumEstudante(codigo, novo);
        pcs.firePropertyChange(PROP_DATA,null,null);

    }
    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param entidade Nova entidade de acolhimento.
     */
    public void editEntidadePropostas(String codigo, String entidade){
        faseContext.editEntidadePropostas(codigo, entidade);
        pcs.firePropertyChange(PROP_DATA,null,null);

    }
    /** Verifica alunos com candidaturas.
     * @return Resultado da operação.
     */
    public boolean verificaAlunosCandidaturas(){
        return faseContext.verificaAlunosCandidaturas();
    }
    /** Volta um estado atrás na máquina de estados.
     */
    public void backState(){
        faseContext.backState();
        pcs.firePropertyChange(PROP_STATE,null,faseContext.getState());

    }
    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param email Novo email do docente da proposta.
     */
    public void editProfEmailPropostas(String codigo, String email){
        faseContext.editProfEmailPropostas(codigo, email);
        pcs.firePropertyChange(PROP_DATA,null,null);
    }
    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param ramo Novo ramo da proposta.
     */
    public void editRamoPropostas(String codigo, String ramo){
        faseContext.editRamoPropostas(codigo, ramo);
        pcs.firePropertyChange(PROP_DATA,null,null);

    }
    /** Remove uma proposta.
     * @param codigo Codigo da proposta.
     * @return Resultado da operação.
     */
    public boolean removeProposta(String codigo){
        boolean aux = faseContext.removeProposta(codigo);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return aux;
    }
    /** Adiciona uma candidatura.
     * @param candidatura Nova candidatura.
     * @return Resultado da operação.
     */
    public boolean adicionaCandidatura(Candidaturas candidatura){
        boolean aux = faseContext.adicionaCandidatura(candidatura);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return aux;
    }
    /** Consulta uma candidatura.
     * @param num Número do aluno
     * @return Dados da candidatura.
     */
    public String consultaCandidatura(long num){
        return faseContext.consultaCandidatura(num);
    }
    /** Remove uma candidatura.
     * @param num Número do aluno.
     * @return Resultado da operação.
     */
    public boolean removeCandidatura(long num){
        boolean aux = faseContext.removeCandidatura(num);
        pcs.firePropertyChange(PROP_DATA,null,null);
        return aux;
    }
    /** Edita uma candidatura.
     * @param num Número do aluno.
     * @param lista Nova lista de propostas.
     */
    public void editCandidatura(long num, List<String> lista){
        faseContext.editCandidatura(num, lista);
        pcs.firePropertyChange(PROP_DATA,null,null);
    }
    /** Verifica se uma proposta está atribuida.
     * @param codigo Código da proposta.
     * @return Resultado da operação.
     */
    public boolean propostaAtribuida(String codigo){
        return faseContext.propostaAtribuida(codigo);
    }
    /** Atribui automaticamente as proposts aos docentes.
     */
    public void atribuicaoAutoProp(){
        faseContext.atribuicaoAutoProp();
        pcs.firePropertyChange(PROP_DATA,null,null);
    }
    /** Lista associação dos alunos.
     * @param opcao Opção de critério.
     * @return Lista da associação.
     */
    public String listaAssociacao(int opcao){
        return faseContext.listaAssociacao(opcao);
    }
    /** Lista associação dos alunos.
     * @param opcao Opção de critério.
     * @param ordem Opção de critério.
     * @return Lista da associação.
     */
    public String listaAssociacao(int opcao, int ordem){
        return faseContext.listaAssociacao(opcao, ordem);
    }
    /** Obtém lista alunos sem atribuição.
     * @return String lista de alunos sem atribuição.
     */
    public String alunosSemAtribuicao(){
        return faseContext.alunosSemAtribuicao();
    }
    /** Obtém lista propostas sem atribuição.
     * @return String lista de propostas sem atribuição.
     */
    public String propostasSemAtribuicao(){
        return faseContext.propostasSemAtribuicao();
    }
    /** Verifica se um aluno tem atribuição.
     * @param num número do aluno.
     * @return boolean resultado da operação.
     */
    public boolean isSemAtribuicao(long num){
        return faseContext.isSemAtribuicao(num);
    }
    /** Verifica se uma proposta tem atribuição.
     * @param prop código da proposta.
     * @return boolean resultado da operação.
     */
    public boolean isPropSemAtribuicao(String prop){
        return faseContext.isPropSemAtribuicao(prop);
    }
    /** Remove uma proposta atribuida ao aluno.
     * @param num número do aluno.
     * @return boolean resultado da operação.
     */
    public boolean removePropostaDoAluno(long num){
        boolean aux = cmPropostas.invokeCommand(new RemovePropostaCommand(faseContext, num, faseContext.getAlunoProp(num)));
        pcs.firePropertyChange(PROP_DATA,null,null);
        return aux;
    }
    /** Obtém uma lista dos alunos não atribuidos.
     * @return Set com os alunos não atribuidos.
     */
    public Set<Alunos> getAlunoNaoAtribuidos() {
        return faseContext.getAlunoNaoAtribuidos();
    }
    /** Obtém uma lista das propostas não atribuidas.
     * @return Set com as propostas não atribuidas.
     */
    public Set<Propostas> getPropNaoAtribuidos() {
        return faseContext.getPropNaoAtribuidos();
    }
    /** Obtém uma lista dos alunos atribuidos.
     * @return Set com os alunos atribuidos.
     */
    public Set<Alunos> getAlunoAtribuidos() {
        return faseContext.getAlunoAtribuidos();
    }
    /** Consulta propostas de acordo com várias restrições da fase três.
     * @param opcao opção de restrições.
     * @param crit opção de restrições.
     * @return String listas de propostas de acordo com as opções.
     */
    public String listaPropostasF3(int opcao, int crit){
        return faseContext.listaPropostasF3(opcao, crit);
    }

    /** Obtém o número de docentes.
     * @return Número de docentes.
     */
    public int getNDocentes(){
        return faseContext.getNDocentes();
    }
    /** Atribui as propostas aos alunos que estejam disponiveis.
     * @return  Mapa dos alunos empatados.
     */
    public Map<String, List<Long>> atribuicaoPropDisponiveis(){
        pcs.firePropertyChange(PROP_DATA,null,null);
        return faseContext.atribuicaoPropDisponiveis();
    }
    /** Obtem o numero de alunos sem atruibução.
     * @return int numero de alunos sem atribuição.
     */
    public int getAlunosSemAtribuicao(){
        return faseContext.getAlunosSemAtribuicao();
    }
    /** Atribui a proposta ao aluno.
     * @param num numero do aluno a associar.
     * @param  prop proposta a associar.
     */
    public void atribuiAlunoEmpatado(long num, String prop){
        faseContext.atribuiAlunoEmpatado(num, prop);
        pcs.firePropertyChange(PROP_DATA,null,null);
    }
    /** Atribui o aluno à propostas.
     * @param numALuno Número do aluno.
     * @param propCodigo Código da proposta.
     * @return Resultado da operação.
     */
    public boolean atribuicaoManual(long numALuno, String propCodigo){
        boolean aux = cmPropostas.invokeCommand(new AddPropostaCommand(faseContext, numALuno, propCodigo));
        pcs.firePropertyChange(PROP_DATA,null,null);
        return aux;
    }
    /** Obtém uma lista das entidades ordenadas.
     * @return Mapa das entidades ordenados por número de propostas.
     */
    public LinkedHashMap<String, Integer> getEntidadeTop(){
        return faseContext.getEntidadeTop();
    }
    /** Verifica se o ramo da proposta de um aluno.
     * @param num número do aluno.
     * @param nome código da proposta.
     * @return boolean resultado da operação.
     */

    public boolean verificaRamoPropAluno(long num, String nome){
        return faseContext.verificaRamoPropAluno(num,nome);
    }

    /** Atribui automaticamente as proposts aos docentes.
     */
    public void atribuicaoAutoDocentesProp(){
        faseContext.atribuicaoAutoDocentesProp();
        pcs.firePropertyChange(PROP_DATA,null,null);
    }

    /** Verifica se uma proposta tem atribuição.
     * @param prop código da proposta.
     * @return boolean resultado da operação.
     */
    public boolean isPropAlunoAtribuida(String prop){
        return faseContext.isPropAlunoAtribuida(prop);
    }
    /** Adicona uma proposta a um docente.
     * @param email Email do docentes.
     * @param prop Codigo da proposta..
     */
    public void adicionaPropDocente(String email, String prop){
        //faseContext.adicionaPropDocente(email,prop);
        cmOrientadores.invokeCommand(new AddOrientadorCommand(faseContext, email, prop));
        pcs.firePropertyChange(PROP_DATA,null,null);
    }
    /** Verifica se um docente tem uma proposta associada.
     * @param docente email do docente.
     * @param prop codigo da proposta.
     * @return boolean resultado da operação.
     */
    public boolean isPropDocenteAssociado(String docente, String prop){
        return faseContext.isPropDocenteAssociado(docente, prop);
    }
    /** Verifica se um orientador tem uma proposta associada.
     * @param email email do docente.
     * @return boolean resultado da operação.
     */
    public boolean isOrientadorAtribuido(String email){
        return faseContext.isOrientadorAtribuido(email);
    }
    /** Altera o orientador de uma proposta.
     * @param docenteAntigo docente anteriormente associado.
     * @param docenteNovo novo docente para associar.
     */
    public void alteraOrientador(String docenteAntigo, String docenteNovo) {
        faseContext.alteraOrientador(docenteAntigo,docenteNovo);
        pcs.firePropertyChange(PROP_DATA,null,null);
    }

    /** Consulta orientadores.
     * @param email Email do orientador.
     * @return Dados do orientador.
     */
    public String consultaOrientadores(String email){
        return faseContext.consultaOrientadores(email);
    }
    /** Obtém uma lista dos docentes atribuidos.
     * @return Set com os docentes atribuidos.
     */
    public Set<Docentes> getDocentesAtribuidos() {
        return faseContext.getDocentesAtribuidos();
    }
    /** Obtém uma lista das propostas não atribuidas.
     * @return Set com as propostas não atribuidas.
     */
    public Set<Propostas> getPropOrientadorNaoAtribuidos() {
        return faseContext.getPropOrientadorNaoAtribuidos();
    }
    /** Elimina orientadores.
     * @param email Email do orientador..
     */
    public void eliminarOrientadorAssociado(String email){
        //faseContext.eliminarOrientadorAssociado(email);
        cmOrientadores.invokeCommand(new RemoveOrientadorCommand(faseContext, email, faseContext.getCodigoDocenteOrientador(email)));
        pcs.firePropertyChange(PROP_DATA,null,null);
    }
    /** Obtém uma lista dos alunos.
     * @return Set com os alunos.
     */
    public Set<Alunos> getAlunos() {
        return faseContext.getAlunos();
    }
    /** Obtém uma lista dos docentes.
     * @return Set com os docentes.
     */
    public Set<Docentes> getDocentes() {
        return faseContext.getDocentes();
    }
    /** Obtém uma lista dos propostas.
     * @return Set com os propostas.
     */
    public Set<Propostas> getPropostas() {
        return faseContext.getPropostas();
    }

    /** Obtém uma lista dos candidaturas.
     * @return Set com os candidaturas.
     */
    public Set<Candidaturas> getCandidaturas() {
        return faseContext.getCandidaturas();
    }

    /** Ordena os docentes por número de propostas.
     * @return Mapa com os docentes ordenados.
     */
    public Map<Docentes, List<Propostas>> getDocentesOrdProp(){
        return faseContext.getDocentesOrdProp();
    }
    /** Obtém o número de propostas do ramo de DA.
     * @return Número de propostas do ramo de DA.
     */
    public int getNDAProps(){
        return faseContext.getNDAProps();
    }
    /** Obtém o número de propostas do ramo de SI.
     * @return Número de propostas do ramo de SI.
     */
    public int getNSIProps(){
        return faseContext.getNSIProps();
    }

    /** Obtém o número de propostas do ramo de RAS.
     * @return Número de propostas do ramo de RAS.
     */
    public int getNRASProps(){
        return  faseContext.getNRASProps();
    }
    /** Listagem final.
     * @param opcao de critério.
     * @return Dados da fase cinco.
     */
    public String listaFinalF5(int opcao){
        return faseContext.listaFinalF5(opcao);
    }
    /** Lista dos orientadores.
     * @param opcao Opção de critério.
     * @return Dados dos orientadores.
     */
    public String listarOrientadores(int opcao){
        return faseContext.listarOrientadores(opcao);
    }
    /** Exporta para um ficheiro a informção da primeira fase.
     * @param nome Nome do ficheiro.
     */
    public void exportFase1(String nome){
        faseContext.exportFase1(nome);
    }
    /** Exporta para um ficheiro a informção da segunda fase.
     * @param nome Nome do ficheiro.
     */
    public void exportFase2(String nome){
        faseContext.exportFase2(nome);
    }
    /** Exporta para um ficheiro a informção da terceira fase.
     * @param nome Nome do ficheiro.
     */
    public void exportarFase3(String nome){
        faseContext.exportarFase3(nome);
    }
    /** Exporta para um ficheiro a informção da quarta e quinta fase.
     * @param nome Nome do ficheiro.
     */
    public void exportarFase4E5(String nome){
        faseContext.exportarFase4E5(nome);
    }

    /** Verifica se o ramo existe.
     * @param ramo String com o ramo.
     * @return boolean se o ramo existe ou não.
     */
    public boolean checkRamo(String ramo){
        return faseContext.checkRamo(ramo);
    }
    /** Verifica se o curso existe.
     * @param curso String com o curso.
     * @return boolean se o curso existe ou não.
     */
    public boolean checkCurso(String curso){
        return faseContext.checkCurso(curso);
    }

    /** Load de um ponto da aplicação.
     * @return Resultado da operação.
     */
    public boolean load(){
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME));
            faseContext = (FaseContext) ois.readObject();
            pcs.firePropertyChange(PROP_DATA,null,null);
        }catch (Exception e){
            //System.err
            return false;
        }
        return true;
    }

    /** Save de um ponto da aplicação.
     * @return Resultado da operação.
     */
    public boolean save(){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME));
            oos.writeObject(faseContext);

        }catch (Exception e){
            return false;
        }
        return true;
    }

    /** Undo do operação dos orientadores.
     * @return Resultado da operação.
     */
    public boolean undo() {
        boolean aux =  cmOrientadores.undo();
        pcs.firePropertyChange(PROP_DATA,null,null);
        return aux;

    }
    /** Undo do operação dos orientadores.
     * @return Resultado da operação.
     */
    public boolean redo() {
        boolean aux =  cmOrientadores.redo();
        pcs.firePropertyChange(PROP_DATA,null,null);
        return aux;
    }

    /** Undo do operação dos propostas.
     * @return Resultado da operação.
     */
    public boolean undoProp() {
        boolean aux =  cmPropostas.undo();
        pcs.firePropertyChange(PROP_DATA,null,null);
        return aux;

    }
    /** Redo do operação dos propostas.
     * @return Resultado da operação.
     */
    public boolean redoProp() {
        boolean aux =  cmPropostas.redo();
        pcs.firePropertyChange(PROP_DATA,null,null);
        return aux;
    }


}
