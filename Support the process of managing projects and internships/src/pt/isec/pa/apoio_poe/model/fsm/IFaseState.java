package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.data.Candidaturas;
import pt.isec.pa.apoio_poe.model.data.Docentes;
import pt.isec.pa.apoio_poe.model.data.Propostas;

import java.util.List;
import java.util.Map;

/** Cria um interface da máquina de estados.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public interface IFaseState {

    /** Adiciona um aluno.
     * @param aluno Aluno a adicionar.
     * @return Resultado da operação.
     */
    boolean addAluno(Alunos aluno);
    /** Consulta um aluno.
     * @param numEstudante Número do aluno a consultar.
     * @return String com os dados do aluno.
     */
    String consultaAluno(long numEstudante);
    /** Edita um aluno.
     * @param numero Número do aluno a editar.
     * @param  novoNome Novo nome do aluno.
     */
    void editNomeAluno(long numero, String novoNome);
    /** Edita um aluno.
     * @param numero Número do aluno a editar.
     * @param  novoEmail Novo email do aluno.
     */
    void editNomeEmail(long numero, String novoEmail);
    /** Edita um aluno.
     * @param numero Número do aluno a editar.
     * @param  novaSigla Nova sigla do curso do aluno.
     */
    void editSiglaCurso(long numero, String novaSigla);
    /** Edita um aluno.
     * @param numero Número do aluno a editar.
     * @param  novaSigla Nova sigla do ramo do aluno.
     */
    void editSiglaRamo(long numero, String novaSigla);
    /** Edita um aluno.
     * @param numero Número do aluno a editar.
     * @param classificacao Nova classificação do aluno.
     */
    void editClassificacao(long numero, double classificacao);
    /** Edita um aluno.
     * @param numero Número do aluno a editar.
     */
    void editPossibilidade(long numero);
    /** Remove um aluno.
     * @param numEstudante Número do aluno a remover.
     * @return Resultado da operação.
     */
    boolean removeAluno(long numEstudante);

    /** Adiciona um docente.
     * @param docente Docente a adiconar.
     * @return Resultado da operação.
     */
    boolean addDocente(Docentes docente);
    /** Consulta um docente.
     * @param nome Email do docente.
     * @return dados do docente.
     */
    String consultaDocente(String nome);
    /** Remove um docente.
     * @param nome Email do docente.
     * @return Resultado da operação.
     */
    boolean removeDocente(String nome);
    /** Edita um docente.
     * @param nome Email do docente.
     * @param novoNome Novo Nome do docente.
     */
    void editNomeDocente(String nome, String novoNome);
    /** Edita um docente.
     * @param nome Email do docente.
     * @param novoEmail Novo Nome do docente.
     */
    void editEmailDocente(String nome, String novoEmail);


    /** Adiciona uma proposta.
     * @param prop Proposta a adiconar.
     * @return Resultado da operação.
     */
    boolean adicionaProposta(Propostas prop);
    /** Consulta uma proposta.
     * @param codigo Codigo da proposta.
     * @return Dados da proposta.
     */
    String consultaProposta(String codigo);
    /** Remove uma proposta.
     * @param codigo Codigo da proposta.
     * @return Resultado da operação.
     */
    boolean removeProposta(String codigo);
    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param titulo Novo titulo.
     */
    void editTituloProp(String codigo, String titulo);
    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param titulo Nova entidade de acolhimento.
     */
    void editEntidadeAcolhimento(String codigo, String titulo);
    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param novoNumero Novo numero do estudante.
     */
    void editNumEstudantePropostas(String codigo, long novoNumero);
    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param ramo Novo ramo da proposta.
     */
    void editRamoPropostas(String codigo, String ramo);
    /** Edita uma proposta.
     * @param codigo Codigo da proposta.
     * @param email Novo email do docente da proposta.
     */
    void editProfEmailPropostas(String codigo, String email);


    /** Adiciona uma candidatura.
     * @param candidatura Nova candidatura.
     * @return Resultado da operação.
     */
    boolean adicionaCandidatura(Candidaturas candidatura);
    /** Consulta uma candidatura.
     * @param num Número do aluno
     * @return Dados da candidatura.
     */
    String consultaCandidatura(long num);
    /** Remove uma candidatura.
     * @param num Número do aluno.
     * @return Resultado da operação.
     */
    boolean removeCandidatura(long num);
    /** Edita uma candidatura.
     * @param num Número do aluno.
     * @param list Nova lista de propostas.
     */
    void editCandidatura(long num, List<String> list);

    /** Obtem a lista de alunos.
     * @return Devolve a lista de alunos.
     */
    String listarAlunos();
    /** Obtem a lista de propostas.
     * @param crit Opção de restrinção.
     * @param opcao Opção de restrinção.
     * @return Devolve a lista de propostas.
     */
    String consultaPropostas(int opcao ,int crit);
    /** Atribui o aluno à propostas.
     * @param numALuno Número do aluno.
     * @param propCodigo Código da proposta.
     * @return Resultado da operação.
     */
    boolean atribuicaoManual(long numALuno, String propCodigo);
    /** Atribui automaticamente as proposts aos docentes.
     */
    void atribuicaoAutoDocentesProp();
    /** Atribui automaticamente as proposts aos alunos.
     */
    void atribuicaoAutoProp();
    /** Lista associação dos alunos.
     * @param opcao Opçáo de critério.
     * @return Lista da associação.
     */
    String listaAssociacao(int opcao);
    /** Lista associação dos alunos.
     * @param opcao Opção de critério.
     * @param ordem Opção de critério.
     * @return Lista da associação.
     */
    String listaAssociacao(int opcao, int ordem);

    /** Adicona uma proposta a um docente.
     * @param email Email do docentes.
     * @param prop Codigo da proposta..
     */
     void adicionaPropDocente(String email, String prop);
    /** Altera o orientador de uma proposta.
     * @param docenteAntigo Email do docente antigo.
     * @param docenteNovo Email do novo docente.
     */
     void alteraOrientador(String docenteAntigo, String docenteNovo);
    /** Atribui as propostas aos alunos que estejam disponiveis.
     * @return  Mapa dos alunos empatados.
     */
     Map<String, List<Long>> atribuicaoPropDisponiveis();

    /** Consulta orientadores.
     * @param email Email do orientador.
     * @return Dados do orientador.
     */
    String consultaOrientadores(String email);
    /** Elimina orientadores.
     * @param email Email do orientador..
     */
    void eliminarOrientadorAssociado(String email);

    /** Lista dos orientadores.
     * @param opcao Opção de critério.
     * @return Dados dos orientadores.
     */
    String listarOrientadores(int opcao);
    /** Listagem final.
     * @param opcao de critério.
     * @return Dados da fase cinco.
     */
    String listaFinalF5(int opcao);

    /** Volta um estado atrás na máquina de estados.
     */
    void backState();
    /** Avança um estado na máquina de estados.
     */
    void changeState();
    /** Obtém o estado atual da aplicação
     * @return Devolve o estado atual.
     */
    FaseState getState();

    /** Obtém o estado de uma fase.
     * @param fase Número da fase.
     * @return Estado da fase.
     */
    boolean getFechado(int fase);
    /** Obtém o estado de uma fase anterior.
     * @param fase Número da fase.
     * @return Estado da fase.
     */
    boolean getFaseAnteriorFechado(int fase);
    /** Altera o estado de uma fase.
     * @param state Novo estado.
     * @param fase Número da fase.
     */
    void setFechado(boolean state, int fase);
}
