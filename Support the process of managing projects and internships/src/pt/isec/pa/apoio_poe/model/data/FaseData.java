package pt.isec.pa.apoio_poe.model.data;

import java.io.FileWriter;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/** Representa os dados da aplicação.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */
public class FaseData implements Serializable {
    static final long serialVersionUID = 100L;
    private static final String DELIMITADOR = ";";
    private static final String SEPARADOR = "\n";
    /** Set dos alunos.
     */
    private Set<Alunos> alunos;
    /** Set dos docentes.
     */
    private Set<Docentes> docentes;
    /** Set das propostas.
     */
    private Set<Propostas> propostas;
    /** Set das candidaturas.
     */
    private Set<Candidaturas> candidaturas;
    /** Mapa dos alunos asssociados a propostas.
     */
    private Map<Alunos, Propostas> alunosPropAssociados;
    /** Mapa dos docentes asssociados a propostas.
     */
    private Map<Docentes, List<Propostas>> docentesPropAssociados;
    /** Lista com a informção do estado das fases.
     */
    private List<Boolean> fasesFechadas;

    /** Cria uma classe dos dados da aplicação.
     */
    public FaseData() {
        alunos = new HashSet<>();
        docentes = new HashSet<>();
        propostas = new HashSet<>();
        candidaturas = new HashSet<>();
        alunosPropAssociados = new HashMap<>();
        docentesPropAssociados = new HashMap<>();
        fasesFechadas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fasesFechadas.add(i, false);
        }
    }
    /** Obtém uma lista dos alunos.
     * @return Set com os alunos.
     */
    public Set<Alunos> getAlunos() {
        return alunos;
    }
    /** Obtém uma lista dos docentes.
     * @return Set com os docentes.
     */
    public Set<Docentes> getDocentes() {
        return docentes;
    }

    /** Obtém uma lista dos propostas.
     * @return Set com os propostas.
     */
    public Set<Propostas> getPropostas() {
        return propostas;
    }

    /** Obtém uma lista dos candidaturas.
     * @return Set com os candidaturas.
     */
    public Set<Candidaturas> getCandidaturas() {
        return candidaturas;
    }

    /** Obtém uma lista dos alunos atribuidos.
     * @return Set com os alunos atribuidos.
     */
    public Set<Alunos> getAlunoAtribuidos() {
        return alunosPropAssociados.keySet();
    }
    /** Obtém uma lista dos docentes atribuidos.
     * @return Set com os docentes atribuidos.
     */
    public Set<Docentes> getDocentesAtribuidos() {
        return docentesPropAssociados.keySet();
    }


    /** Obtém uma lista dos alunos não atribuidos.
     * @return Set com os alunos não atribuidos.
     */
    public Set<Alunos> getAlunoNaoAtribuidos() {
        Set<Alunos> alunosNaoAssociados = new HashSet<>();
            for (Alunos aluno1 : alunos) {
                if(!alunosPropAssociados.containsKey(aluno1)){
                    alunosNaoAssociados.add(aluno1);
                }
        }
        return alunosNaoAssociados;
    }
    /** Obtém uma lista das propostas não atribuidas.
     * @return Set com as propostas não atribuidas.
     */
    public Set<Propostas> getPropOrientadorNaoAtribuidos() {
        Set<Propostas> alunosNaoAssociados = new HashSet<>();
        int aux = 0;
        for (Propostas aluno1 : propostas) {
            aux = 0;
            for(Map.Entry<Docentes, List<Propostas>> entry : docentesPropAssociados.entrySet()) {
                List<Propostas> prop = entry.getValue();
                if (prop.contains(aluno1)) {
                    aux = 1;
                    break;
                }
            }
            if(aux == 0){
                alunosNaoAssociados.add(aluno1);
            }
        }
        return alunosNaoAssociados;
    }


    /** Obtém uma lista das propostas não atribuidas.
     * @return Set com as propostas não atribuidas.
     */
    public Set<Propostas> getPropNaoAtribuidos() {
        Set<Propostas> propostasNaoAssociados = new HashSet<>();
        for (Propostas aluno1 : propostas) {
            if(!alunosPropAssociados.containsValue(aluno1)){
                propostasNaoAssociados.add(aluno1);
            }
        }
        return propostasNaoAssociados;
    }

    /** Obtém uma lista dos docentes ordenados.
     * @return Mapa dos docentes ordenados por número de propostas.
     */
    public Map<Docentes, List<Propostas>> getDocentesOrdProp(){
        List<Map.Entry<Docentes, List<Propostas>>> list =
                new LinkedList<Map.Entry<Docentes, List<Propostas>>>(docentesPropAssociados.entrySet());

        list.sort(new Comparator<Map.Entry<Docentes, List<Propostas>>>() {
            @Override
            public int compare(Map.Entry<Docentes, List<Propostas>> o1, Map.Entry<Docentes, List<Propostas>> o2) {
                return Integer.compare(o2.getValue().size(), o1.getValue().size());
            }

        });

        Map<Docentes, List<Propostas>> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Docentes, List<Propostas>> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }


        return sortedMap;
    }
    /** Obtém a lista de propostas de um orientador.
     * @param email Email do docente.
     * @return  Lista das propostas.
     */
    public List<Propostas> getCodigoDocenteOrientador(String email){
        for(Map.Entry<Docentes, List<Propostas>> entry : docentesPropAssociados.entrySet()) {
            Docentes alunoAdd = entry.getKey();
            if(alunoAdd.getEmail().equals(email))
                return entry.getValue();
        }
        return null;
    }

    /** Obtém o numero de propostas do ramo DA.
     * @return Int com o número de propostas.
     */
    public int getNDAProps(){
        int nProp = 0;
        for (Alunos aluno : alunosPropAssociados.keySet()) {
            if(Objects.equals(aluno.getSiglaRamo(), "DA")){
                nProp++;
            }
        }

        return nProp;
    }
    /** Obtém o numero de propostas do ramo SI.
     * @return Int com o número de propostas.
     */
    public int getNSIProps(){
        int nProp = 0;
        for (Alunos aluno : alunosPropAssociados.keySet()) {
            if(Objects.equals(aluno.getSiglaRamo(), "SI")){
                nProp++;
            }
        }

        return nProp;
    }

    /** Obtém o numero de propostas do ramo RAS.
     * @return Int com o número de propostas.
     */
    public int getNRASProps(){int nProp = 0;
        for (Alunos aluno : alunosPropAssociados.keySet()) {
            if(Objects.equals(aluno.getSiglaRamo(), "RAS")){
                nProp++;
            }
        }
        return nProp;
    }

    /** Modifica o estado de uma determinada fase.
     * @param estado boolean com o novo estado.
     * @param fase int com o número da fase.
     */
    public void setFechada(boolean estado, int fase){
        fasesFechadas.set(fase, estado);
    }

    /** Obtem o estado de uma fase anterior à atual.
     * @param fase int com o número da fase.
     * @return Resultado da operação.
     */
    public boolean getFaseAnterior(int fase){
        return fasesFechadas.get(fase - 1);
    }

    /** Obtem o estado de uma determinada fase.
     * @param fase int com o número da fase.
     * @return Resultado da operação.
     */
    public boolean getFaseFechada(int fase){
        return fasesFechadas.get(fase);
    }

    /** Verifica se o ramo existe.
     * @param ramo String com o ramo.
     * @return boolean se o ramo existe ou não.
     */
    public boolean checkRamo(String ramo){
        if(ramo.compareTo("DA") == 0)
            return true;
        else if(ramo.compareTo("SI") == 0)
            return true;
        else return ramo.compareTo("RAS") == 0;
    }

    /** Verifica se o curso existe.
     * @param ramo String com o curso.
     * @return boolean se o curso existe ou não.
     */
    public boolean checkCurso(String ramo){
        if(ramo.compareTo("LEI") == 0)
            return true;
        else return ramo.compareTo("LEI-PL") == 0;
    }


    /** Obtém o número de docentes.
     * @return int número de docentes existentes.
     */
    public int getNDocentes(){
        return docentes.size();
    }

    /** Obtém o número de alunos com propostas associadas.
     * @return int número de alunos com propostas associadas.
     */
    public int getNAlunosPropAssociados(){
        return alunosPropAssociados.keySet().size();
    }

    /** Obtém o número de docentes com propostas associadas.
     * @return int número de docentes com propostas associadas.
     */
    public int getNDocentesPropAssociados(){
        return docentesPropAssociados.keySet().size();
    }

    /** Obtém o número de candidaturas.
     * @return int número de candidaturas.
     */
    public int getNCandidaturas(){
        return candidaturas.size();
    }

    /** Adiciona uma candidatura à lista de candidaturas.
     * @param candidatura Nova candidatura.
     * @return boolean resultado da operação.
     */
    public boolean addCandidatura(Candidaturas candidatura) {
        return candidaturas.add(candidatura);
    }

    /** Consulta uma candidatura.
     * @param numero número do aluno.
     * @return String valores da candidatura.
     */
    public String consultaCandidatura(long numero) {
        Candidaturas cand = Candidaturas.getDummyCandidatura(numero);
        List<Candidaturas> candida = new ArrayList<>(candidaturas);
        int a = candida.indexOf(cand);
        if (a != -1)
            return candida.get(a).toString();
        return "Candidatura não encontrado\n";
    }

    /** Verifica se uma proposta está atribuida.
     * @param proposta nome da proposta.
     * @return boolean se está atribuida ou não.
     */
    public boolean propostaAtribuida(String proposta) {
        Propostas aux = Propostas.getDummyProposta(proposta);
        return propostas.contains(aux);
    }

    /** Obtém os alunos com candidatura.
     * @return String alunos com candidatura.
     */
    public String listaAlunosComCandidatura() {
        StringBuilder sb = new StringBuilder("Alunos com Candidatura: \n");
        for (Alunos item : alunos) {
            for (Candidaturas candidatura : candidaturas) {
                if (candidatura.getNumero() == item.getNumEstudante())
                    sb.append(item);
            }
        }
        return sb.toString();
    }

    /** Obtém os alunos sem candidatura.
     * @return String alunos sem candidatura.
     */
    public String listaAlunosSemCandidatura() {
        StringBuilder sb = new StringBuilder("Alunos sem Candidatura: \n");
        boolean flag;
        for (Alunos item : alunos) {
            flag = false;
            for (Candidaturas candidatura : candidaturas) {
                if (candidatura.getNumero() == item.getNumEstudante())
                    flag = true;
            }
            if (!flag)
                sb.append(item);
        }
        return sb.toString();
    }

    /** Obtém os alunos autopropostos.
     * @return String alunos autopropostos.
     */
    public String listaAlunosAutoPropostos() {
        StringBuilder sb = new StringBuilder("Alunos auto propostos: \n");
        for (Alunos item : alunos) {
            for (Propostas proposta : propostas) {
                if (proposta.getNumAluno() == item.getNumEstudante() && proposta instanceof T3)
                    sb.append(item);

            }
        }
        return sb.toString();
    }

    /** Remove uma candidatura.
     * @param numero número do aluno.
     * @return boolean resultado da operação.
     */
    public boolean removeCandidatura(long numero) {
        return candidaturas.remove(Candidaturas.getDummyCandidatura(numero));
    }

    /** Edita uma candidatura.
     * @param numero número do aluno.
     * @param novaLista lista com as novas propostas.
     */
    public void editaCandidatura(long numero, ArrayList<String> novaLista) {
        Candidaturas cand = Candidaturas.getDummyCandidatura(numero);
        for (Candidaturas item : candidaturas) {
            if (item.equals(cand)) {
                item.setPropostas(novaLista);
            }
        }
    }

    /** Adiciona um novo aluno.
     * @param aluno novo aluno a adicionar.
     * @return boolean resultado da operação.
     */
    public boolean addAluno(Alunos aluno) {
        return alunos.add(aluno);
    }

    /** Consulta um aluno.
     * @param numEstudante Número do aluno a consultar.
     * @return String dados do aluno.
     */
    public String consultaAluno(long numEstudante) {
        Alunos aux = Alunos.getDummyAluno(numEstudante);
        for (Alunos item : alunos) {
            if (item.equals(aux)) {
                return item.toString();
            }
        }
        return "Aluno não encontrado\n";
    }

    /** Edita nome de um aluno.
     * @param numero Número do aluno a editar.
     * @param novoNome novo nome do aluno.
     */
    public void editNomeAluno(long numero, String novoNome) {
        Alunos aux = Alunos.getDummyAluno(numero);
        for (Alunos item : alunos) {
            if (item.equals(aux)) {
                item.setNome(novoNome);
            }
        }
    }

    /** Edita email de um aluno.
     * @param numero Número do aluno a editar.
     * @param novoEmail Novo email do aluno.
     */
    public void editNomeEmail(long numero, String novoEmail) {
        Alunos aux = Alunos.getDummyAluno(numero);
        for (Alunos item : alunos) {
            if (item.equals(aux)) {
                item.setEmail(novoEmail);
            }
        }
    }

    /** Edita sigla do curso de um aluno.
     * @param numero Número do aluno a editar.
     * @param novaSigla Novo sigla do aluno.
     */
    public void editSiglaCurso(long numero, String novaSigla) {
        Alunos aux = Alunos.getDummyAluno(numero);
        for (Alunos item : alunos) {
            if (item.equals(aux)) {
                item.setSiglaCurso(novaSigla);
            }
        }
    }

    /** Edita sigla do ramo de um aluno.
     * @param numero Número do aluno a editar.
     * @param novaSigla Novo sigla do aluno.
     */
    public void editSiglaRamo(long numero, String novaSigla) {
        Alunos aux = Alunos.getDummyAluno(numero);
        for (Alunos item : alunos) {
            if (item.equals(aux)) {
                item.setSiglaRamo(novaSigla);
            }
        }
    }

    /** Edita classificação de um aluno.
     * @param numero Número do aluno a editar.
     * @param classificacao Nova classificação.
     */
    public void editClassificacao(long numero, double classificacao) {
        Alunos aux = Alunos.getDummyAluno(numero);
        for (Alunos item : alunos) {
            if (item.equals(aux)) {
                item.setClassificacao(classificacao);
            }
        }
    }
    /** Edita possibilidade de acesso ao estágio de um aluno.
     * @param numero Número do aluno a editar.
     */
    public void editPossibilidade(long numero) {
        Alunos aux = Alunos.getDummyAluno(numero);
        for (Alunos item : alunos) {
            if (item.equals(aux)) {
                item.setAcessoEstagios(!item.getAcessoEstagios());
            }
        }
    }
    /** Remove um aluno.
     * @param numEstudante Número do aluno a remover.
     * @return boolean resultado da operação.
     */
    public boolean removeAluno(long numEstudante) {
        if(alunos.remove(Alunos.getDummyAluno(numEstudante))){
            propostas.removeIf(prop -> prop.getNumAluno() == numEstudante && prop instanceof T3);
            return true;
        }

        return false;
    }

    /** Adiciona um docente.
     * @param docente docente a adicionar.
     * @return boolean resultado da operação.
     */
    public boolean addDocente(Docentes docente) {
        return docentes.add(docente);
    }

    /** Consulta um docente.
     * @param nome email do docente.
     * @return String dados do docente.
     */
    public String consultaDocente(String nome) {
        Docentes aux = Docentes.getDummyDocente(nome);
        for (Docentes item : docentes) {
            if (item.equals(aux)) {
                return item.toString();
            }
        }
        return "Docente não encontrado\n";
    }

    /** Adiciona um docente.
     * @param nome email do docente.
     * @return boolean resultado da operação.
     */
    public boolean removeDocente(String nome) {
        return docentes.remove(Docentes.getDummyDocente(nome));
    }

    /** Edita um docente.
     * @param nome email do docente.
     * @param novoNome novo nome do docente.
     */
    public void editNomeDocente(String nome, String novoNome) {
        Docentes aux = Docentes.getDummyDocente(nome);
        for (Docentes item : docentes) {
            if (item.equals(aux)) {
                item.setNome(novoNome);
            }
        }
    }

    /** Adiciona uma proposta.
     * @param prop proposta a adiconar.
     * @return boolean resultado da operação.
     */
    public boolean adicionaProposta(Propostas prop) {
        return propostas.add(prop);
    }

    /** Consulta uma proposta.
     * @param codigo codigo da proposta.
     * @return String dados da proposta.
     */
    public String consultaProposta(String codigo) {
        Propostas aux = Propostas.getDummyProposta(codigo);
        for (Propostas item : propostas) {
            if (item.equals(aux)) {
                return item.toString();
            }
        }
        return "Propostas não encontrada\n";
    }

    /** Obtém o tipo de proposta.
     * @param codigo codigo da proposta.
     * @return String tipo da proposta.
     */
    public String getTipoPropostas(String codigo) {
        Propostas aux = Propostas.getDummyProposta(codigo);
        for (Propostas item : propostas) {
            if (item.equals(aux)) {
                if (item instanceof T1)
                    return "T1";
                else if (item instanceof T2)
                    return "T2";
                else if (item instanceof T3)
                    return "T3";
            }
        }
        return null;
    }

    /** Obtém o tipo de proposta.
     * @param codigo codigo da proposta.
     * @param novoTitulo novo titulo.
     */
    public void editTituloProposta(String codigo, String novoTitulo) {
        Propostas aux = Propostas.getDummyProposta(codigo);
        for (Propostas item : propostas) {
            if (item.equals(aux)) {
                item.setTitulo(novoTitulo);

            }
        }
    }

    /** Edita o num do estudante de uma proposta.
     * @param codigo codigo da proposta.
     * @param novoNumero novo número.
     */
    public void editNumEstudantePropostas(String codigo, long novoNumero) {
        Propostas aux = Propostas.getDummyProposta(codigo);
        for (Propostas item : propostas) {
            if (item.equals(aux)) {
                item.setNumAluno(novoNumero);

            }
        }
    }

    /** Edita a entidade da proposta.
     * @param codigo codigo da proposta.
     * @param novaEntidade nova entidade.
     */
    public void editEntidadeProposta(String codigo, String novaEntidade) {
        Propostas aux = Propostas.getDummyProposta(codigo);
        for (Propostas item : propostas) {
            if (item.equals(aux)) {
                item.setEntidade(novaEntidade);

            }
        }
    }

    /** Edita o ramo de uma proposta.
     * @param codigo codigo da proposta.
     * @param novoRamo novo ramo.
     */
    public void editRamoProposta(String codigo, String novoRamo) {
        Propostas aux = Propostas.getDummyProposta(codigo);
        for (Propostas item : propostas) {
            if (item.equals(aux)) {
                item.setRamo(novoRamo);

            }
        }
    }

    /** Remove uma proposta.
     * @param codigo codigo da proposta.
     * @return boolean resultado da operação.
     */
    public boolean removeProposta(String codigo) {
        return propostas.remove(Propostas.getDummyProposta(codigo));
    }

    /** Obtém número de alunos.
     * @return int número de alunos.
     */
    public int getNAlunos() {
        return alunos.size();
    }

    /** Obtém número de propostas.
     * @return int número de propostas.
     */
    public int getNPropostas() {
        return propostas.size();
    }

    /** Verifica se o número de alunos é igual ao número de alunos com propostas associados.
     * @return boolean resultado da operação.
     */
    public boolean verificaAlunosCandidaturas() {
        return alunosPropAssociados.keySet().size() == alunos.size();

    }

    /** Obtém número de alunos.
     * @return int número de alunos.
     */
    private String listaAlunosPropAtribuidos() {
        return alunosPropAssociados.toString();
    }

    /** Obtém lista docentes sem propostas.
     * @return String lista de docentes propostas.
     */
    public String listaPropsSemDocente(){
        StringBuilder sb = new StringBuilder();
        sb.append("Propostas sem Docente associado: \n");
        for (Propostas prop:propostas) {
            for(Map.Entry<Docentes, List<Propostas>> entry : docentesPropAssociados.entrySet()) {
                List<Propostas> value = entry.getValue();
                if(!value.contains(prop))
                    sb.append(prop);
            }
        }
        return sb.toString();
    }

    /** Obtém lista associação dos alunos.
     * @param opcao Critério de restrinção.
     * @return String lista associação dos alunos.
     */
    public String listaAssociacao(int opcao) {
        StringBuilder sb = new StringBuilder();
        if (opcao == 1) {
            for (Propostas prop : alunosPropAssociados.values()) {
                if (prop instanceof T3)
                    sb.append(prop);
            }
        }

        if (opcao == 2) {
            sb.append(listaAlunosComCandidatura());
        }

        if (opcao == 3) {
            sb.append(listaAlunosPropAtribuidos());
        }

        if (opcao == 4) {
            sb.append(alunosSemAtribuicao());
        }

        return sb.toString();

    }

    /** Obtém lista alunos sem atribuição.
     * @return String lista de alunos sem atribuição.
     */
    public String alunosSemAtribuicao() {
        StringBuilder sb = new StringBuilder("Alunos sem atribuição:\n");
        for (Alunos aluno : alunos) {
            if (!alunosPropAssociados.containsKey(aluno))
                sb.append(aluno);
        }
        sb.append("\n");
        return sb.toString();
    }

    /** Obtém lista propostas sem atribuição.
     * @return String lista de propostas sem atribuição.
     */
    public String propostasSemAtribuicao() {
        StringBuilder sb = new StringBuilder("Propostas sem atribuição:\n");
        for (Propostas prop : propostas) {
            if (!alunosPropAssociados.containsValue(prop))
                sb.append(prop);
        }
        sb.append("\n");
        return sb.toString();
    }

    /** Verifica se um aluno tem atribuição.
     * @param numAluno número do aluno.
     * @return boolean resultado da operação.
     */
    public boolean isSemAtribuicao(long numAluno) {
        Alunos alunotemp = Alunos.getDummyAluno(numAluno);
        return !alunosPropAssociados.containsKey(alunotemp);
    }

    /** Verifica se uma proposta tem atribuição.
     * @param prop código da proposta.
     * @return boolean resultado da operação.
     */
    public boolean isPropSemAtribuicao(String prop) {
        Propostas proptemp = Propostas.getDummyProposta(prop);
        return !alunosPropAssociados.containsValue(proptemp);
    }
    /** Atribui as propostas automaticamente aos alunos
     */
    public void atribuicaoAutoProp() {
        for (Propostas prop : propostas) {
            if (prop instanceof T2 || prop instanceof T3) {
                for (Alunos aluno : alunos) {
                    if (prop.getNumAluno() == aluno.getNumEstudante()){
                        alunosPropAssociados.put(aluno, prop);
                        prop.setNumAluno(aluno.getNumEstudante());
                    }
                }
            }
        }
    }

    /** Atribuir manualmente uma proposta a um aluno.
     * @param numALuno número do aluno.
     * @param propCodigo codigo da proposta.
     * @return boolean resultado da operação.
     */
    public boolean atribuicaoManual(long numALuno, String propCodigo) {
        Alunos alunoAux = null;
        Propostas propAux = null;
        for (Alunos aluno : alunos) {
            if (aluno.getNumEstudante() == numALuno)
                alunoAux = aluno;
        }
        for (Propostas prop : propostas) {
            if (prop.getCodigo().equals(propCodigo))
                propAux = prop;
        }
        if(alunoAux == null || propAux == null)
            return false;

        if(!alunoAux.getAcessoEstagios() && propAux instanceof T1)
            return false;

        alunosPropAssociados.put(alunoAux, propAux);
        return true;
    }
    /** Obtém uma lista das entidades ordenadas.
     * @return Mapa das entidades ordenados por número de propostas.
     */
    public  LinkedHashMap<String, Integer> getEntidadeTop(){
        Map <String, Integer> unsortedMap = new HashMap<>();
        for (Map.Entry<Alunos, Propostas> entry : alunosPropAssociados.entrySet()) {
            //System.out.println(entry.getValue());
            if(entry.getValue() instanceof T1){
                unsortedMap.merge(((T1) entry.getValue()).getEntidade(),1,Integer::sum);
            }
        }

        //ordenar o mapa
        LinkedHashMap<String, Integer> sortedMap = unsortedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(/* Optional: Comparator.reverseOrder() */))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        return sortedMap;
    }
    /** Verifica se o ramo da proposta de um aluno.
     * @param num número do aluno.
     * @param nome código da proposta.
     * @return boolean resultado da operação.
     */
    public boolean verificaRamoPropAluno(long num, String nome){
        Propostas proptemp = Propostas.getDummyProposta(nome);
        Alunos aluno = Alunos.getDummyAluno(num);
        String[] companies = new String[3];

        for (Propostas prop: propostas) {
            if(prop.equals(proptemp)){
                if(prop instanceof T3)
                    return true;
                if(prop instanceof T2){
                    companies = ((T2) prop).getRamo().split("\\|");
                    break;
                }
                if(prop instanceof T1){
                    companies = ((T1) prop).getRamo().split("\\|");
                    break;
                }
            }

        }

        for (Alunos alunos1: alunos) {
            if(alunos1.equals(aluno)){
                for (String company : companies) {
                    if (company.equals(alunos1.getSiglaRamo())) {
                        return true;
                    }
                }
            }
        }


        return false;
    }

    /** Remove uma proposta atribuida ao aluno.
     * @param numAluno número do aluno.
     * @return boolean resultado da operação.
     */
    public boolean removePropostaDoAluno(long numAluno) {
        Alunos aluno = Alunos.getDummyAluno(numAluno);
        Propostas prop = alunosPropAssociados.get(aluno);
        if (prop instanceof T3)
            return alunosPropAssociados.remove(aluno, prop);
        int aux = 0;
        if (prop instanceof T2) {
            for(Map.Entry<Docentes, List<Propostas>> entry : docentesPropAssociados.entrySet()) {
                List<Propostas> value = entry.getValue();
                if(value.contains(prop))
                  aux = 1;
            }
            if(aux == 0)
            return alunosPropAssociados.remove(aluno, prop);
        }

            return false;

    }
    /** Obtém o aluno dassociado a uma proposta.
     * @param numAluno Número do aluno.
     * @return Dados do aluno.
     */
    public String getAlunoProp(long numAluno){
        Propostas prop = alunosPropAssociados.get(Alunos.getDummyAluno(numAluno));
        return prop.getCodigo();
    }

    /** Consulta propostas de acordo com várias restrições.
     * @param opcao opção de restrições.
     * @param crit opção de restrições.
     * @return String listas de propostas de acordo com as opções.
     */
    public String consultaPropostas(int opcao, int crit) {
        StringBuilder sb = new StringBuilder();

        if (opcao == 1 && crit == 1)
            return propostas.toString();
        if(opcao == 1){
            if(crit == 2){
                for (Propostas prop : propostas) {
                        for (Candidaturas cand : candidaturas) {
                            if (cand.getPropostas().contains(prop.getCodigo())){
                                sb.append(prop);
                                break;
                            }
                        }
                }
            }
            if(crit == 3){
                if(candidaturas.size() == 0){
                    for (Propostas prop : propostas) {
                        sb.append(prop);
                    }
                }
                for (Propostas prop : propostas) {
                    for (Candidaturas cand : candidaturas) {
                        if (!cand.getPropostas().contains(prop.getCodigo())){
                            sb.append(prop);
                            break;
                        }

                    }
                }
            }
            return sb.toString();
        }

        if (opcao == 2) {
            if (crit == 1) {
                for (Propostas prop : propostas) {
                    if (prop instanceof T3)
                        sb.append(prop);
                }
                return sb.toString();
            }
            if (crit == 2) {
                for (Propostas prop : propostas) {
                    if (prop instanceof T3) {
                        for (Candidaturas cand : candidaturas) {
                            if (cand.getPropostas().contains(prop.getCodigo()))
                                sb.append(prop);
                        }
                    }
                }
                return sb.toString();
            }
            if (crit == 3) {
                if(candidaturas.size() == 0){
                    for (Propostas prop : propostas) {
                        if (prop instanceof T3) {
                            sb.append(prop);
                            }
                        }
                    }
                for (Propostas prop : propostas) {
                    if (prop instanceof T3) {
                        for (Candidaturas cand : candidaturas) {
                            if (!cand.getPropostas().contains(prop.getCodigo())) {
                                sb.append(prop);
                                break;
                            }
                        }
                    }
                }
                return sb.toString();
            }
        }

        if (opcao == 3) {
            if (crit == 1) {
                for (Propostas prop : propostas) {
                    if (prop instanceof T2)
                        sb.append(prop);
                }
                return sb.toString();
            }

            if (crit == 2) {
                for (Propostas prop : propostas) {
                    if (prop instanceof T2) {
                        for (Candidaturas cand : candidaturas) {
                            if (cand.getPropostas().contains(prop.getCodigo())) {
                                sb.append("Candidatura de: " + cand.getNumero() + " ");
                                sb.append(prop);
                            }
                        }
                    }
                }
                return sb.toString();
            }
            if (crit == 3) {
                boolean flag;
                if(candidaturas.size() == 0){
                    for (Propostas prop : propostas) {
                        if (prop instanceof T2)
                            sb.append(prop);
                    }
                    return sb.toString();
                }

                for (Propostas prop : propostas) {
                    flag = true;
                    if (prop instanceof T2) {
                        for (Candidaturas cand : candidaturas) {
                            if (cand.getPropostas().contains(prop.getCodigo())) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag)
                            sb.append(prop);
                    }

                }
                return sb.toString();
            }
        }
        return "Nenhuma proposta";
    }

    /** Consulta propostas de acordo com várias restrições da fase três.
     * @param opcao opção de restrições.
     * @param crit opção de restrições.
     * @return String listas de propostas de acordo com as opções.
     */
    public String listaPropostasF3(int opcao, int crit) {
        StringBuilder sb = new StringBuilder();

        if (opcao == 1 && crit == 1)
            return propostas.toString();
        if(opcao == 1){
            if(crit == 2){
                for (Propostas prop : propostas) {
                    if (!alunosPropAssociados.containsValue(prop))
                        sb.append(prop);
                }
                return sb.toString();
            }
            if(crit == 3){
                for (Propostas prop : propostas) {
                    if (alunosPropAssociados.containsValue(prop))
                        sb.append(prop);
                }
                return sb.toString();
            }
        }

        if (opcao == 2) {
            if (crit == 1) {
                for (Propostas prop : propostas) {
                    if (prop instanceof T3)
                        sb.append(prop);
                }
                return sb.toString();
            }

            if (crit == 2) {
                for (Propostas prop : propostas) {
                    if (!alunosPropAssociados.containsValue(prop) && prop instanceof T3)
                        sb.append(prop);
                }
                return sb.toString();

            }

            if (crit == 3) {
                for (Propostas prop : propostas) {
                    if (alunosPropAssociados.containsValue(prop) && prop instanceof T3)
                        sb.append(prop);
                }
                return sb.toString();
            }
        }
        if (opcao == 3) {
            if (crit == 1) {
                for (Propostas prop : propostas) {
                    if (prop instanceof T2)
                        sb.append(prop);
                }
                return sb.toString();
            }

            if (crit == 2) {
                for (Propostas prop : propostas) {
                    if (!alunosPropAssociados.containsValue(prop) && prop instanceof T2)
                        sb.append(prop);
                }
                return sb.toString();

            }

            if (crit == 3) {
                for (Propostas prop : propostas) {
                    if (alunosPropAssociados.containsValue(prop) && prop instanceof T2)
                        sb.append(prop);
                }
                return sb.toString();
            }
        }
        return "Nenhuma Proposta";
    }

    /** Consulta a lista de associações.
     * @param opcao opção de restrições.
     * @param ordem opção de restrições.
     * @return String listas as associações de acordo com as opções.
     */
    public String listaAssociacao(int opcao, int ordem) {
        StringBuilder sb = new StringBuilder();
        List<Propostas> propaux = new ArrayList<>(alunosPropAssociados.values());
        if(ordem == 1){
            //estagio primeiro
            propaux.sort((o1, o2) -> {
                if (o1 instanceof T3)
                    return -1;
                else if (o2 instanceof T3)
                    return 1;
                else if (o1 instanceof T1 && o2 instanceof T2)
                    return -1;
                else if (o1 instanceof T2 && o2 instanceof T1)
                    return 1;

                return 0;
            });
        }else {
            //T2 primeiro
            propaux.sort((o1, o2) -> {
                if (o1 instanceof T3)
                    return -1;
                else if (o2 instanceof T3)
                    return 1;
                else if (o1 instanceof T1 && o2 instanceof T2)
                    return 1;
                else if (o1 instanceof T2 && o2 instanceof T1)
                    return -1;

                return 0;
            });

        }

        List<Alunos> alunosChaves = new ArrayList<>(alunosPropAssociados.keySet());

        for (Propostas value : propaux) {
            for (Alunos aluno : alunosChaves) {
                if (alunosPropAssociados.get(aluno).equals(value)) {
                    sb.append(aluno);
                    sb.append(value);
                }

            }
        }
        return sb.toString();
    }

    /** Obtem o numero de alunos sem atruibução.
     * @return int numero de alunos sem atribuição.
     */
    public int getAlunosSemAtribuicao(){
        return alunos.size() - alunosPropAssociados.keySet().size();
    }

    /** Atribui a proposta ao aluno.
     * @param numAluno numero do aluno a associar.
     * @param  proposta proposta a associar.
     */
    public void atribuiAlunoEmpatado(long numAluno, String proposta){
        Alunos alunoAux = null;
        Propostas propAux = null;
        for (Alunos aluno: alunos) {
            if(aluno.getNumEstudante() == numAluno)
                alunoAux = aluno;
        }

        for (Propostas prop: propostas) {
            if(prop.getCodigo().equals(proposta))
                propAux = prop;
        }

        alunosPropAssociados.put(alunoAux, propAux);


    }
    /** Verifica se uma proposta está atribuida.
     * @param propostasList lista de propostas.
     * @param prop codigo da proposta.
     * @return boolean resultado da operação.
     */
    private boolean isPropostaAtribuida(List<Propostas> propostasList, String prop){
        for (Propostas value : propostasList) {
            if (value.getCodigo().equals(prop))
                return true;
        }
        return false;
    }

    /** Atribui uma proposta disponivel a um aluno disponivel.
     * @return Map com os alunos empatados caso exista.
     */
    public Map<String, List<Long>>  atribuicaoPropDisponiveis(){
        List <Alunos> listaAlunos = new ArrayList<>();
        List <Propostas> listaPropostas = new ArrayList<>();
        List <Candidaturas> candidaturasLista = new ArrayList<>(candidaturas);
        Map<String, List<Long>>  alunosEmpatados = new HashMap<>();
        int conta = 0;
        int empateFlag;
        int empatePos;
        int contaCand = 0;

        for (Alunos aluno:alunos) {
            if(!alunosPropAssociados.containsKey(aluno)){
                listaAlunos.add(aluno);
            }
        }
        for (Propostas prop: propostas) {
            if(!alunosPropAssociados.containsValue(prop))
                listaPropostas.add(prop);
        }

        for (int i = 0; i < listaAlunos.size(); i++) {
            if(!listaAlunos.get(i).getAcessoEstagios())
                contaCand++;
        }

        for (int i = 0; i < listaPropostas.size(); i++) {
            if(listaPropostas.get(i) instanceof T1)
                conta++;
        }
        if(contaCand == conta && listaPropostas.size() == conta)
            return null;

        for (int i = 0; i < listaAlunos.size(); i++) {
            conta = 0;
            empateFlag = 0;
            empatePos = 0;
            contaCand = 0;
            for (int j = 0; j < listaAlunos.size(); j++) {
                if (i != j) {
                    if (listaAlunos.get(i).getClassificacao() > listaAlunos.get(j).getClassificacao()) {
                        conta++;
                    }
                    if (listaAlunos.get(i).getClassificacao() == listaAlunos.get(j).getClassificacao()) {
                        empateFlag++;
                        empatePos = j;
                    }
                }
            }

            for (Candidaturas cand : candidaturas) {
                if (conta == listaAlunos.size() - 1) {
                    int aux = candidaturasLista.indexOf(Candidaturas.getDummyCandidatura(listaAlunos.get(i).getNumEstudante()));
                    Candidaturas candidaturas1 = candidaturasLista.get(aux);
                    for (int j = 0; j < candidaturas1.getPropostas().size(); j++) {
                        if (isPropostaAtribuida(listaPropostas, candidaturas1.getPropostas().get(j))) {
                            int index = listaPropostas.indexOf(Propostas.getDummyProposta(candidaturas1.getPropostas().get(j)));
                            if (listaPropostas.get(index) instanceof T2 || listaPropostas.get(index) instanceof T1 && listaAlunos.get(i).getAcessoEstagios()) {
                                listaPropostas.get(index).setNumAluno(listaAlunos.get(i).getNumEstudante());
                                if(verificaRamoPropAluno(listaAlunos.get(i).getNumEstudante(), listaPropostas.get(j).getCodigo())){
                                    alunosPropAssociados.put(listaAlunos.get(i), listaPropostas.get(index));
                                    listaPropostas.get(index).setNumAluno(listaAlunos.get(i).getNumEstudante());
                                    listaPropostas.remove(Propostas.getDummyProposta(candidaturas1.getPropostas().get(j)));
                                    return null;
                                }

                            }
                        } else {
                            contaCand++;
                        }
                    }
                    if (contaCand == candidaturas1.getPropostas().size()) {
                        if(verificaRamoPropAluno(listaAlunos.get(i).getNumEstudante(), listaPropostas.get(0).getCodigo())){
                            alunosPropAssociados.put(listaAlunos.get(i), listaPropostas.get(0));
                            listaPropostas.get(0).setNumAluno(listaAlunos.get(i).getNumEstudante());
                            listaPropostas.remove(Propostas.getDummyProposta(candidaturas1.getPropostas().get(0)));
                        }

                    }
                }

                if (cand.getNumero() == listaAlunos.get(i).getNumEstudante()) {
                    if (conta == listaAlunos.size() - 2 && empateFlag >= 1) {
                        int sizeaux;
                        int aux = candidaturasLista.indexOf(Candidaturas.getDummyCandidatura(listaAlunos.get(i).getNumEstudante()));
                        int aux2 = candidaturasLista.indexOf(Candidaturas.getDummyCandidatura(listaAlunos.get(empatePos).getNumEstudante()));
                        Candidaturas candidaturas1 = candidaturasLista.get(aux);
                        Candidaturas candidaturas2 = candidaturasLista.get(aux2);

                        if (listaAlunos.get(i).getAcessoEstagios() && listaAlunos.get(empatePos).getAcessoEstagios()) {
                            sizeaux = Math.min(candidaturas1.getPropostas().size(), candidaturas2.getPropostas().size());
                            for (int j = 0; j < sizeaux; j++) {
                                if (candidaturas1.getPropostas().get(j).equals(candidaturas2.getPropostas().get(j))) {

                                    List<Long> alunosList = new ArrayList<>();
                                    int index = listaPropostas.indexOf(Propostas.getDummyProposta(candidaturas1.getPropostas().get(j)));
                                    alunosList.add(listaAlunos.get(i).getNumEstudante());
                                    alunosList.add(listaAlunos.get(empatePos).getNumEstudante());
                                    String prop = listaPropostas.get(index).getCodigo();
                                    alunosEmpatados.put(prop, alunosList);
                                    return alunosEmpatados;
                                }
                            }
                        } else {
                            if (listaAlunos.get(i).getAcessoEstagios() && !listaAlunos.get(empatePos).getAcessoEstagios()) {
                                    int index = listaPropostas.indexOf(Propostas.getDummyProposta(candidaturas1.getPropostas().get(0)));
                                if(verificaRamoPropAluno(listaAlunos.get(i).getNumEstudante(), listaPropostas.get(index).getCodigo())){
                                    listaPropostas.get(index).setNumAluno(listaAlunos.get(i).getNumEstudante());
                                    alunosPropAssociados.put(listaAlunos.get(i), listaPropostas.get(index));
                                    listaPropostas.remove(Propostas.getDummyProposta(listaPropostas.get(0).getCodigo()));
                                }


                            }
                        }

                    }
                }
            }
        }

        // alunos sem empate associados
        for (int i = 0; i < listaAlunos.size(); i++) {
            for (int j = 0; j < listaPropostas.size(); j++) {
                if (!listaAlunos.get(i).getAcessoEstagios() && listaPropostas.get(i) instanceof T2) {
                    if(verificaRamoPropAluno(listaAlunos.get(i).getNumEstudante(), listaPropostas.get(j).getCodigo())){
                        listaPropostas.get(j).setNumAluno(listaAlunos.get(i).getNumEstudante());
                        alunosPropAssociados.put(listaAlunos.get(i), listaPropostas.get(j));
                        listaPropostas.remove(Propostas.getDummyProposta(listaPropostas.get(j).getCodigo()));
                        return null;
                    }

                }
            }
        }



        return null;
    }


    /** Atribui uma proposta a um docente.
     */
    public void atribuicaoDocentesProp(){
        List <Propostas> prop = new ArrayList<>(propostas);
        List <Docentes> docent = new ArrayList<>(docentes);

        for (int i = 0; i < prop.size(); i++) {
            if(prop.get(i) instanceof T2){
                int index = docent.indexOf(Docentes.getDummyDocente(((T2) prop.get(i)).getDocenteProponente().getEmail()));
                Docentes doc = docent.get(index);
                if(docentesPropAssociados.get(doc) == null){
                    List <Propostas> propDoc = new ArrayList<>();
                    propDoc.add(prop.get(i));
                    ((T2) prop.get(i)).setDocenteProponente(doc);
                    docentesPropAssociados.put(doc , propDoc);
                }else{
                    List <Propostas> propDoc = new ArrayList<>(docentesPropAssociados.get(doc));
                    propDoc.add(prop.get(i));
                    ((T2) prop.get(i)).setDocenteProponente(doc);
                    docentesPropAssociados.put(doc , propDoc);
                }
            }
        }
    }

    /** Verifica se um docente tem uma proposta associada.
     * @param docente email do docente.
     * @param prop codigo da proposta.
     * @return boolean resultado da operação.
     */
    public boolean isPropDocenteAssociado(String docente, String prop){
        for(Map.Entry<Docentes, List<Propostas>> entry : docentesPropAssociados.entrySet()) {
            List<Propostas> value = entry.getValue();
            if(value.contains(Propostas.getDummyProposta(prop)))
                return true;
        }
        return false;
    }
    /** Verifica se uma proposta tem uma proposta associada.
     * @param prop codigo da proposta.
     * @return boolean resultado da operação.
     */
    public boolean isPropAlunoAtribuida(String prop){
        return alunosPropAssociados.containsValue(Propostas.getDummyProposta(prop));
    }
    /** Verifica se um orientador tem uma proposta associada.
     * @param email email do docente.
     * @return boolean resultado da operação.
     */
    public boolean isOrientadorAtribuido(String email){
        return docentesPropAssociados.containsKey(Docentes.getDummyDocente(email));
    }

    /** Altera o orientador de uma proposta.
     * @param docenteAntigo docente anteriormente associado.
     * @param docenteNovo novo docente para associar.
     */
    public void alteraOrientador(String docenteAntigo, String docenteNovo){
        if(docentesPropAssociados.containsKey(Docentes.getDummyDocente(docenteNovo))){
            List<Propostas> propNovo = docentesPropAssociados.get(Docentes.getDummyDocente(docenteNovo));
            propNovo.addAll(docentesPropAssociados.get(Docentes.getDummyDocente(docenteAntigo)));
            docentesPropAssociados.remove(Docentes.getDummyDocente(docenteAntigo));

        }else{
            List<Propostas> propVelho = docentesPropAssociados.get(Docentes.getDummyDocente(docenteAntigo));
            List<Docentes> docentesList = new ArrayList<>(docentes);
            int index = docentesList.indexOf(Docentes.getDummyDocente(docenteNovo));
            docentesPropAssociados.put(docentesList.get(index) , propVelho);
            docentesPropAssociados.remove(Docentes.getDummyDocente(docenteAntigo));
        }

    }
    /** Associa um docente a um orientador.
     * @param email email do docente.
     * @param codigo codigo da proposta.
     */
    public void adicionaPropDocente(String email, String codigo){
        List <Propostas> prop = new ArrayList<>(propostas);
        List <Docentes> docent = new ArrayList<>(docentes);

        int index = docent.indexOf(Docentes.getDummyDocente(email));
        Docentes doc = docent.get(index);
        int index2 = prop.indexOf(Propostas.getDummyProposta(codigo));
        Propostas propostas1 = prop.get(index2);
        if(docentesPropAssociados.get(doc) == null){
            List <Propostas> propDoc = new ArrayList<>();
            propDoc.add(propostas1);
            docentesPropAssociados.put(doc , propDoc);
        }else{
            List <Propostas> propDoc = new ArrayList<>(docentesPropAssociados.get(doc));
            propDoc.add(propostas1);
            docentesPropAssociados.put(doc , propDoc);
        }

    }

    /** Consulta um orientador.
     * @param email email do docente.
     * @return String com os dados do orientador.
     */
    public String consultaOrientadores(String email){
        return "Docente: " + email + " Com as seguintes propostas: \n" + docentesPropAssociados.get(Docentes.getDummyDocente(email));
    }

    /** Obter a lista dos orientadores.
     * @param opcao opção para restringir a lista.
     * @return String lista dos orientados.
     */
    public String listarOrientadores(int opcao){
        StringBuilder sb = new StringBuilder();

        if(opcao == 1){
            sb.append("Alunos: ");
            for(Map.Entry<Alunos, Propostas> entry : alunosPropAssociados.entrySet()) {
                Alunos alunoAdd = entry.getKey();
                Propostas value = entry.getValue();
                for(Map.Entry<Docentes, List<Propostas>> entry1 : docentesPropAssociados.entrySet()) {
                    List<Propostas> value1 = entry1.getValue();
                    if(value1.contains(value))
                        sb.append(alunoAdd);
                }
            }
            return sb.toString();
        }

        if(opcao == 2){
            int conta;
            sb.append("Alunos: ");
            for(Map.Entry<Alunos, Propostas> entry : alunosPropAssociados.entrySet()) {
                conta = 0;
                Alunos alunoAdd = entry.getKey();
                Propostas value = entry.getValue();
                for(Map.Entry<Docentes, List<Propostas>> entry1 : docentesPropAssociados.entrySet()) {
                    List<Propostas> value1 = entry1.getValue();
                    if(!value1.contains(value)){
                        conta++;
                    }
                    if(conta == docentesPropAssociados.entrySet().size())
                        sb.append(alunoAdd);
                }
            }
        }

        if(opcao == 3){
            int max = 0;
            int min = propostas.size();
            int media = 0;
            for(Map.Entry<Docentes, List<Propostas>> entry1 : docentesPropAssociados.entrySet()) {
                Docentes docente = entry1.getKey();
                List<Propostas> value1 = entry1.getValue();
                int nprop = value1.size();
                sb.append("Docente: ").append(docente.getEmail()).append(" tem associado: ").append(nprop).append("\n");
                media += value1.size();
                if(nprop > max)
                    max = nprop;

                if(nprop < min)
                    min = nprop;
            }
            if(min > max){
                min = 0;
            }
            if(docentesPropAssociados.keySet().size() != 0)
            media = media / docentesPropAssociados.keySet().size();
            sb.append("Media de orientações por docente : ").append(media).append("\nMinimo de orientações: ")
                    .append(min).append("\nMaximo de orientações: ").append(max);

        }


        return sb.toString();
    }

    /** Elimina orientadores.
     * @param email Email do orientador.
     */
    public void eliminarOrientadorAssociado(String email){
        docentesPropAssociados.remove(Docentes.getDummyDocente(email));
    }

    /** Elimina proposta orientadores.
     * @param email Email do orientador.
     * @param prop propostas a eliminar
     */
    public void eliminarOrientadorPropAssociado(String email, String prop){
        for(Map.Entry<Docentes, List<Propostas>> entry : docentesPropAssociados.entrySet()) {
            if(entry.getKey().getEmail().equals(email)){
                entry.getValue().remove(Propostas.getDummyProposta(prop));
                if(entry.getValue().size() == 0)
                    docentesPropAssociados.remove(Docentes.getDummyDocente(email));
            }
        }
    }
    /** Obtém a lista dos dados necessários na fase 5
     * @param opcao opcao do que pretende ver.
     * @return String lista com os dados.
     */
    public String listaFinalF5(int opcao){
        StringBuilder sb = new StringBuilder();

        if(opcao == 1){
            sb.append("Estudantes com propostas atribuidas: \n");
            sb.append(alunosPropAssociados.keySet());
        }
        if(opcao == 2){
            sb.append("Estudantes sem proposta atribuida e com candidatura\n");
            for (Alunos aluno: alunos) {
                if(!alunosPropAssociados.containsKey(aluno)){
                    if(candidaturas.contains(Candidaturas.getDummyCandidatura(aluno.getNumEstudante())))
                        sb.append(aluno);
                }
            }
        }
        if(opcao == 3){
            sb.append("Propostas disponiveis: \n");
            for (Propostas prop: propostas) {
                if(!alunosPropAssociados.containsValue(prop))
                    sb.append(prop);
            }
        }
        if(opcao == 4){
            sb.append("Propostas atribuidas: \n");
            for (Propostas prop: propostas) {
                if(alunosPropAssociados.containsValue(prop))
                    sb.append(prop);
            }
        }
        if (opcao == 5){
            int max = 0;
            int min = propostas.size();
            int media = 0;
            for(Map.Entry<Docentes, List<Propostas>> entry1 : docentesPropAssociados.entrySet()) {
                Docentes docente = entry1.getKey();
                List<Propostas> value1 = entry1.getValue();
                int nprop = value1.size();
                sb.append("Docente: ").append(docente.getEmail()).append(" tem associado: ").append(nprop).append("\n");
                media += value1.size();
                if(nprop > max)
                    max = nprop;

                if(nprop < min)
                    min = nprop;
            }
            if(docentesPropAssociados.keySet().size() != 0)
                media = media / docentesPropAssociados.keySet().size();
            sb.append("Media de orientações por docente : ").append(media).append("\nMinimo de orientações: ")
                    .append(min).append("\nMaximo de orientações: ").append(max);

        }
        return sb.toString();
    }
    /** Exporta dados do programa na fase um.
     * @param nomeFich nome do ficheiro para guardar dados.
     */
    public void exportFase1(String nomeFich){
        FileWriter file = null;
        try{
           file = new FileWriter(nomeFich);
           file.append("Alunos");
           file.append(SEPARADOR);
            for (Alunos a : alunos) {
                file.append(String.valueOf(a.getNumEstudante()));
                file.append(DELIMITADOR);
                file.append(a.getNome());
                file.append(DELIMITADOR);
                file.append(a.getEmail());
                file.append(DELIMITADOR);
                file.append(a.getSiglaCurso());
                file.append(DELIMITADOR);
                file.append(a.getSiglaRamo());
                file.append(DELIMITADOR);
                file.append(String.valueOf(a.getClassificacao()));
                file.append(DELIMITADOR);
                file.append(String.valueOf(a.getAcessoEstagios()));
                file.append(SEPARADOR);
            }

            file.append("Docentes");
            file.append(SEPARADOR);
            for(Docentes d : docentes){
                file.append(d.getEmail());
                file.append(DELIMITADOR);
                file.append(d.getNome());
                file.append(SEPARADOR);
            }

            file.append("Propostas");
            file.append(SEPARADOR);
            for (Propostas p : propostas){
               if(p instanceof T1){
                   file.append("T1");
                   file.append(DELIMITADOR);
                   file.append(p.getCodigo());
                   file.append(DELIMITADOR);
                   file.append(p.getTitulo());
                   file.append(DELIMITADOR);
                   file.append(String.valueOf(p.getNumAluno()));
                   file.append(DELIMITADOR);
                   file.append(((T1) p).getRamo());
                   file.append(DELIMITADOR);
                   file.append(((T1) p).getEntidade());
                   file.append(SEPARADOR);
               }
               else if(p instanceof  T2){
                   file.append("T2");
                   file.append(DELIMITADOR);
                   file.append(p.getCodigo());
                   file.append(DELIMITADOR);
                   file.append(p.getTitulo());
                   file.append(DELIMITADOR);
                   file.append(String.valueOf(p.getNumAluno()));
                   file.append(DELIMITADOR);
                   file.append(((T2) p).getRamo());
                   file.append(DELIMITADOR);
                   file.append(((T2) p).getDocenteProponente().getEmail());
                   file.append(SEPARADOR);

               }else if(p instanceof T3){
                   file.append("T3");
                   file.append(DELIMITADOR);
                   file.append(p.getCodigo());
                   file.append(DELIMITADOR);
                   file.append(p.getTitulo());
                   file.append(DELIMITADOR);
                   file.append(String.valueOf(p.getNumAluno()));
                   file.append(SEPARADOR);
               }
            }

           file.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /** Exporta dados do programa na fase dois.
     * @param nome nome do ficheiro para guardar dados.
     */
    public void exportFase2(String nome){
        FileWriter file = null;
        try{
            file = new FileWriter(nome);
            file.append("Candidaturas");
            file.append(SEPARADOR);
            for (Candidaturas c : candidaturas){
                file.append(String.valueOf(c.getNumero()));
                file.append(DELIMITADOR);
                List<String> props = new ArrayList<>(c.getPropostas());
                for (String prop : props) {
                    file.append(prop);
                    file.append(DELIMITADOR);
                }
                file.append(SEPARADOR);
            }
            file.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /** Exporta dados do programa na fase três.
     * @param nome nome do ficheiro para guardar dados.
     */
    public void exportarFase3(String nome){
        FileWriter file = null;
        try{
            file = new FileWriter(nome);
            file.append("Candidaturas");
            file.append(SEPARADOR);
            for (Candidaturas c : candidaturas){
                file.append(String.valueOf(c.getNumero()));
                file.append(DELIMITADOR);
                List<String> props = new ArrayList<>(c.getPropostas());
                for (String prop : props) {
                    file.append(prop);
                    file.append(DELIMITADOR);
                }
                file.append(SEPARADOR);
            }

                file.append("Aluno - Proposta");
                file.append(SEPARADOR);
                for(Map.Entry<Alunos, Propostas> entry : alunosPropAssociados.entrySet()) {
                    Alunos alunoAdd = entry.getKey();
                    Propostas value = entry.getValue();
                    file.append(String.valueOf(alunoAdd.getNumEstudante()));
                    file.append(DELIMITADOR);
                    file.append(value.getCodigo());
                    file.append(SEPARADOR);
                    for (Candidaturas cand : candidaturas){
                        List<String> propostas = new ArrayList<>(cand.getPropostas());
                        for (int i = 0; i <propostas.size(); i++) {
                            if(propostas.get(i).equals(value.getCodigo()) && cand.getNumero() == alunoAdd.getNumEstudante()){
                                file.append("Ordem da proposta na candidatura");
                                file.append(DELIMITADOR);
                                file.append(String.valueOf(i + 1));
                                file.append(SEPARADOR);
                                break;
                            }
                        }
                    }

                }

            file.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /** Exporta dados do programa na fase quatro e cinco.
     * @param nome nome do ficheiro para guardar dados.
     */
    public void exportarFase4E5(String nome){
        FileWriter file = null;
        try{
            file = new FileWriter(nome);
            file.append("Candidaturas");
            file.append(SEPARADOR);
            for (Candidaturas c : candidaturas){
                file.append(String.valueOf(c.getNumero()));
                file.append(DELIMITADOR);
                List<String> props = new ArrayList<>(c.getPropostas());
                for (String prop : props) {
                    file.append(prop);
                    file.append(DELIMITADOR);
                }
                file.append(SEPARADOR);
            }

            file.append("Aluno - Proposta");
            file.append(SEPARADOR);
            for(Map.Entry<Alunos, Propostas> entry : alunosPropAssociados.entrySet()) {
                Alunos alunoAdd = entry.getKey();
                Propostas value = entry.getValue();
                file.append(String.valueOf(alunoAdd.getNumEstudante()));
                file.append(DELIMITADOR);
                file.append(value.getCodigo());
                file.append(SEPARADOR);
                for (Candidaturas cand : candidaturas){
                    List<String> propostas = new ArrayList<>(cand.getPropostas());
                    for (int i = 0; i <propostas.size(); i++) {
                        if(propostas.get(i).equals(value.getCodigo()) && cand.getNumero() == alunoAdd.getNumEstudante()){
                            file.append("Ordem da proposta na candidatura");
                            file.append(DELIMITADOR);
                            file.append(String.valueOf(i));
                            file.append(SEPARADOR);
                            break;
                        }
                    }
                }
                for(Map.Entry<Docentes, List<Propostas>> entry1 : docentesPropAssociados.entrySet()) {
                    Docentes docenteAdd = entry1.getKey();
                    List<Propostas> valueprops = entry1.getValue();
                    if(valueprops.contains(value)){
                        file.append("Docente orientador: ");
                        file.append(DELIMITADOR);
                        file.append(docenteAdd.getEmail());
                        file.append(SEPARADOR);
                        break;
                    }
                }

                }

            file.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
