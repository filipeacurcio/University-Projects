package pt.isec.pa.apoio_poe.ui.text;


import pt.isec.pa.apoio_poe.model.data.*;
import pt.isec.pa.apoio_poe.model.fsm.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/** Representa a classe de interface de texto.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class UIText {
    /** Representa a ligação entre a classe interface com a lógica do programa.
     */
    private FaseContextManager fsm;
    private boolean finish = false;

    /** Cria uma interface de texto com uma máquina de estados associada.
     * @param fsm A máquina de estado.
     */
    public UIText(FaseContextManager fsm){
        this.fsm = fsm;
    }

    /** Adiciona os alunos presentes num ficheiro .csv.
     */
    private void inserirAlunos(){
        String nomeFich = PAInput.readString("Nome do ficheiro: ", false);
        String delimiter = ",";
        try{
            File ficheiro = new File(nomeFich);
            FileReader fr = new FileReader(ficheiro);
            BufferedReader br = new BufferedReader(fr);
            String line = " ";
            String[] tempArr;
            while((line = br.readLine()) != null){
                tempArr = line.split(delimiter);

                if(tempArr.length != 7){
                    System.out.println("Formato do ficheiro errado a sair da inserção....");
                    return;
                }

                if(!fsm.checkCurso(tempArr[3])){
                    System.out.println("Sigla de curso invalido, Foi predefinido o curso como LEI");
                    tempArr[3] = "LEI";
                }
                if(!fsm.checkRamo(tempArr[4])){
                    System.out.println("Ramo invalido. Foi predefinido o ramo como DA");
                    tempArr[4] = "DA";
                }

                fsm.adiciona(new Alunos(Long.parseLong(tempArr[0]),tempArr[1],tempArr[2],tempArr[3],tempArr[4],
                        Double.parseDouble(tempArr[5]),Boolean.parseBoolean(tempArr[6])));
            }
            br.close();

        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /** Consulta um aluno.
     */
    private void consultaAluno(){
        long numAluno = PAInput.readLong("Digite o numero de estudante que pretende ver: ");
        System.out.println(fsm.consulta(numAluno));
    }
    /** Remove um aluno.
     */
    private void removeAluno(){
        long numAluno = PAInput.readLong("Digite o numero de estudante que pretende remover: ");
        if(fsm.remove(numAluno)){
            System.out.println("Aluno removido com sucesso.");
        }else
            System.out.println("Aluno não removido.");
    }

    /** Edita um aluno.
     */
    private void editarAluno(){
        long num = PAInput.readLong("Digite o numero do aluno que quer editar: ");
        if(fsm.consulta(num).compareToIgnoreCase("aluno não encontrado\n") == 0){
            System.out.println("Numero de aluno não encontrado");
            return;
        }
        int opcao = PAInput.chooseOption("Digite o que quer editar: ", "Nome" , "Email",
                "Sigla do curso","Sigla do ramo", "Classificação", "Possibilidade de acesso");

        switch (opcao) {
            case 1 -> {
                String nome = PAInput.readString("Digite o novo nome: ", false);
                fsm.editNome(num, nome);
            }
            case 2 -> {
                String email = PAInput.readString("Digite o novo Email: ", true);
                fsm.editEmail(num, email);
            }
            case 3 -> {
                String siglaC = PAInput.readString("Digite o novo Curso: ", true);
                if (fsm.checkCurso(siglaC))
                    fsm.editSiglaCurso(num, siglaC);
                else
                    System.out.println("Curso Invalido.");
            }
            case 4 -> {
                String siglaR = PAInput.readString("Digite o novo Ramo: ", true);
                if (fsm.checkRamo(siglaR))
                    fsm.editSiglaRamo(num, siglaR);
                else
                    System.out.println("Ramo invalido.");
            }
            case 5 -> {
                double classificacao = PAInput.readNumber("Digite a nova classificacao: ");
                fsm.editClassificacao(num, classificacao);
            }
            case 6 -> fsm.editPossibilidade(num);
        }
    }

    /** Fecha a fase um.
     */
    private void fecharFase1(){
        if(fsm.getNPropostas() >= fsm.getNAlunos()){
            System.out.println("Fase 1 fechada com sucesso.");
            fsm.setFechado(true, 1);
        }else{
            System.out.println("Numero de propostas inferior ao numero de alunos. Impossivel de fechar a fase.\n" +
                    "Faltam: " +  (fsm.getNAlunos() - fsm.getNPropostas()) + " para ser possivel a fase ser fechada.");
        }
    }

    /** Representa o menu para gerir os alunos.
     */
    private void gestaoAlunos(){
        boolean finish_help = false;
        if(fsm.getFechado(1)){
            while(!finish_help){
                int opcao = PAInput.chooseOption("O que pretende fazer:",  "Consultar", "Sair");
                switch (opcao){
                    case 1 -> consultaAluno();
                    case 2 -> finish_help = true;
                }
            }
        }else if(!fsm.getFechado(1)){
            while(!finish_help){
                int opcao = PAInput.chooseOption("O que pretende fazer:", "Inserir", "Consultar", "Editar", "Eliminar", "Sair");
                switch (opcao) {
                    case 1 -> inserirAlunos();
                    case 2 -> consultaAluno();
                    case 3 -> editarAluno();
                    case 4 -> removeAluno();
                    case 5 -> {
                        fsm.backState();
                        finish_help = true;
                    }
                }
            }
        }
    }

    /** Edita um docente.
     */
    private void editarDocente(){
        String nome = PAInput.readString("Digite o email do docente que quer editar: ", false);
        if(fsm.consultaDocente(nome).compareToIgnoreCase("Docente não encontrado\n") == 0){
            System.out.println("Nome do docente não encontrado");
            return;
        }

            String nomeNovo = PAInput.readString("Digite o novo nome:\n", false);
            fsm.editNomeDocente(nome, nomeNovo);
    }

    /** Remove um docente.
     */
    private void removeDocente(){
        String nome = PAInput.readString("Digite o email do docente que pretende remover: ", false);
        if(fsm.removeDocente(nome)){
            System.out.println("Docente removido com sucesso.");
        }else
            System.out.println("Docente não removido.");
    }


    /** Adiciona os docentes presentes num ficheiro .csv.
     */
    private void inserirDocentes(){
        String nomeFich = PAInput.readString("Nome do ficheiro: ", false);
        String delimiter = ",";
        try{
            File ficheiro = new File(nomeFich);
            FileReader fr = new FileReader(ficheiro);
            BufferedReader br = new BufferedReader(fr);
            String line = " ";
            String[] tempArr;
            while((line = br.readLine()) != null){
                tempArr = line.split(delimiter);
                if(tempArr.length != 2){
                    System.out.println("Ficheiro com formato errado a sair da inserção....");
                    return;
                }
                fsm.adicionaDocente(new Docentes(tempArr[0], tempArr[1]));
            }
            br.close();

        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /** Consulta um docente.
     */
    private void consultaDocente(){
        String nomeDocente = PAInput.readString("Digite o email do docente que pretende ver: ", true);
        System.out.println(fsm.consultaDocente(nomeDocente));
    }



    /** Representa o menu para gerir os docentes.
     */
    private void gestaoDocentes(){
        boolean finish_help = false;
        if(fsm.getFechado(1)){
            while(!finish_help){
                int opcao = PAInput.chooseOption("O que pretende fazer:",  "Consultar", "Sair");
                switch (opcao){
                    case 1 -> consultaDocente();
                    case 2 -> finish_help = true;
                }
            }
        }else if(!fsm.getFechado(1)){
            while(!finish_help){
                int opcao = PAInput.chooseOption("O que pretende fazer:", "Inserir", "Consultar", "Editar", "Eliminar", "Sair");
                switch (opcao) {
                    case 1 -> inserirDocentes();
                    case 2 -> consultaDocente();
                    case 3 -> editarDocente();
                    case 4 -> removeDocente();
                    case 5 -> {
                        fsm.backState();
                        finish_help = true;
                    }
                }
            }
        }

    }

    /** Consulta um proposta.
     */
    private void consultaProposta(){
        String codigoProposta = PAInput.readString("Digite o codigo da proposta que pretende ver: ", true);
        System.out.println(fsm.consultaProposta(codigoProposta));
    }

    /** Adiciona as propostas presentes num ficheiro .csv.
     */
    private void inserirPropostas(){
        String nomeFich = PAInput.readString("Nome do ficheiro: ", false);
        String delimiter = ",";
        try{
            File ficheiro = new File(nomeFich);
            FileReader fr = new FileReader(ficheiro);
            BufferedReader br = new BufferedReader(fr);
            String line = " ";
            String[] tempArr;
            while((line = br.readLine()) != null){
                tempArr = line.split(delimiter);

                if(tempArr[0].equals("T1")){
                    fsm.adicionaProposta(new T1(tempArr[1], tempArr[2], tempArr[3], tempArr[4]));
                }

                if(tempArr[0].equals("T2")){
                    if(fsm.consultaDocente(tempArr[4]).compareToIgnoreCase("docente não encontrado\n") == 0){
                        System.out.println("Docente não encontrado proposta não adicionada.");
                        continue;
                    }
                    if(tempArr.length == 6)
                    fsm.adicionaProposta(new T2(tempArr[1], tempArr[2], tempArr[3], tempArr[4], Long.parseLong(tempArr[5])));
                    else
                        fsm.adicionaProposta(new T2(tempArr[1], tempArr[2], tempArr[3], tempArr[4]));
                }

                if(tempArr[0].equals("T3")){
                    if(fsm.consulta(Long.parseLong(tempArr[3])).compareToIgnoreCase("aluno não encontrado\n") == 0){
                        System.out.println("Numero de aluno não encontrado: " + tempArr[3]);
                        break;
                    }else
                    fsm.adicionaProposta(new T3(tempArr[1], tempArr[2], Long.parseLong(tempArr[3])));
                }

            }
            br.close();

        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /** Edita uma proposta.
     */
    private void editarProsposta(){
        String codigo = PAInput.readString("Digite o codigo da proposta que quer editar: ", true);
        if(fsm.consultaProposta(codigo).compareToIgnoreCase("proposta não encontrada\n") == 0){
            System.out.println("Codigo da proposta não encontrada");
            return;
        }

        if(fsm.getTipoPropostas(codigo).equals("T1")){
            int opcao = PAInput.chooseOption("Digite o que quer editar: ", "Titulo" , "NumAluno", "Ramo",
                    "Entidade");
            switch (opcao){
                case 1: String titulo = PAInput.readString("Digite o novo Titulo:\n", false);
                        fsm.editTituloPropostas(codigo, titulo);
                    break;
                case 2: long NumAluno = PAInput.readLong("Digite o novo numero do Aluno: ");
                    fsm.editNumEstudante(codigo, NumAluno);
                    break;

                case 3: String ramo = PAInput.readString("Digite o novo Ramo: ", true);
                    if(fsm.checkRamo(ramo)){
                        fsm.editRamoPropostas(codigo, ramo);
                    }
                    else
                        System.out.println("Ramo desconhecido.");
                    break;

                case 4:String entidade = PAInput.readString("Digite a nova entidade: ", false);
                        fsm.editEntidadePropostas(codigo, entidade);
                          break;
            }
        }

        else if(fsm.getTipoPropostas(codigo).equals("T2")){
            int opcao = PAInput.chooseOption("Digite o que quer editar: ", "Titulo" , "NumAluno", "Ramo",
                    "Docente Proponente");
            switch (opcao){
                case 1: String titulo = PAInput.readString("Digite o novo Titulo:\n", false);
                    fsm.editTituloPropostas(codigo, titulo);
                    break;
                case 2: long NumAluno = PAInput.readLong("Digite o novo numero do Aluno: ");
                    fsm.editNumEstudante(codigo, NumAluno);
                    break;

                case 3: String ramo = PAInput.readString("Digite o novo Ramo: ", true);
                    if(fsm.checkRamo(ramo)){
                        fsm.editRamoPropostas(codigo, ramo);
                    }
                    else
                        System.out.println("Ramo desconhecido.");
                    break;
                case 4 :
                    String email = PAInput.readString("Digite o email do docente a editar: ", true);
                    if(fsm.consultaDocente(email).compareToIgnoreCase("Docente não encontrado\n") == 0){
                        System.out.println("Nome do docente não encontrado");
                    }
                    else{
                        String novoEmail = PAInput.readString("Novo email: ", true);
                        fsm.editProfEmailPropostas(codigo, novoEmail);
                        }
                    break;
            }
        }
        else if(fsm.getTipoPropostas(codigo).equals("T3")){
            int opcao = PAInput.chooseOption("Digite o que quer editar: ", "Titulo" , "NumEstudante");
            switch (opcao){
                case 1: String titulo = PAInput.readString("Digite o novo Titulo:\n", false);
                    fsm.editTituloPropostas(codigo, titulo);
                    break;
                case 2: long numEstudante = PAInput.readLong("Digite o novo numeroEstudante: ");
                        fsm.editNumEstudante(codigo, numEstudante);
                    break;
            }
        }

    }

    /** Remove uma proposta.
     */
    private void removeProposta(){
        String codigo = PAInput.readString("Digite o email do docente que pretende remover: ", true);
        if(fsm.removeProposta(codigo)){
            System.out.println("Proposta removida com sucesso.");
        }else
            System.out.println("Proposta não removida.");
    }

    /** Representa o menu para gerir os docentes.
     */
    private void gestaoPropostas(){
        boolean finish_help = false;
        if(fsm.getFechado(1)){
            while(!finish_help){
                int opcao = PAInput.chooseOption("O que pretende fazer:",  "Consultar", "Sair");
                switch (opcao){
                    case 1 -> consultaProposta();
                    case 2 -> finish_help = true;
                }
            }
        }else if(!fsm.getFechado(1)){
            while(!finish_help){
                int opcao = PAInput.chooseOption("O que pretende fazer:", "Inserir", "Consultar", "Editar", "Eliminar", "Sair");
                switch (opcao){
                    case 1 : inserirPropostas();
                        break;
                    case 2 : consultaProposta();
                        break;
                    case 3 : editarProsposta();
                        break;
                    case 4 :removeProposta();
                        break;
                    case 5 :
                        fsm.backState();
                        finish_help = true;
                        break;
                }
            }
        }

    }

    /** Exporta para um ficheiro a informção da primeira fase.
     */
    private void exportarFase1(){
        String nome = PAInput.readString("Digite o nome do ficheiro onde quer guardar os dados: \n", true);
        fsm.exportFase1(nome);
    }

    /** Representa o menu para gerir a fase um.
     */
    private void faseUmUI(){
        int opcao = PAInput.chooseOption("Escolha a opção que pretende:", "Gestão de alunos", "Gestão de professores",
                "Gestão de propostas", "Avançar fase", "Fechar fase","save", "load", "Exportar", "Sair");
        switch (opcao){
            case 1 -> gestaoAlunos();
            case 2 -> gestaoDocentes();
            case 3-> gestaoPropostas();
            case 4 -> fsm.mudaEstado();
            case 5 -> fecharFase1();
            case 6 -> fsm.save();
            case 7 -> fsm.load();
            case 8 -> exportarFase1();
            case 9 -> finish = true;
        }
    }

    /** Adiciona as candidaturas presentes num ficheiro .csv.
     */
    private void inserirCandidaturas(){
        String nomeFich = PAInput.readString("Nome do ficheiro: ", false);
        String delimiter = ",";
        try{
            File ficheiro = new File(nomeFich);
            FileReader fr = new FileReader(ficheiro);
            BufferedReader br = new BufferedReader(fr);
            String line = " ";
            String[] tempArr;
            while((line = br.readLine()) != null){
                tempArr = line.split(delimiter);
                ArrayList<String> aux = new ArrayList<>();
                for (int i = 1; i < tempArr.length; i++) {
                    if(!fsm.propostaAtribuida(tempArr[i])){
                        System.out.println("Proposta: " + tempArr[i] + " não adicionada.");
                    }else{
                          aux.add(tempArr[i]);
                      }
                }
                if(aux.size() != 0)
                fsm.adicionaCandidatura(new Candidaturas(Long.parseLong(tempArr[0]), aux));
                else{
                    System.out.println("Candidatura não adicionada. Propostas insuficientes");
                }

            }
            br.close();

        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /** Remove uma candidatura.
     */
    private void removerCandidaturas(){
        long num = PAInput.readLong("Digite o numero do aluno que pretende remover: ");
        if(fsm.removeCandidatura(num)){
            System.out.println("Candidatura removida com sucesso.");
        }else
            System.out.println("Candidatura não removido.");
    }

    /** Edita uma candidatura.
     */
    private void editarCandidaturas() {
        long num = PAInput.readLong("Digite o Numero do aluno da Candidatura a editar: ");
        if (fsm.consultaCandidatura(num).equals("Candidatura não encontrado\n")) {
            System.out.println("Candidatura nao encontrada");
            return;
        }

        List<String> opcoes = new ArrayList<>();
        String opcaoCand;
        do {
            opcaoCand = PAInput.readString("Digite o numero da proposta ou \"sair\" para parar a edição: ", true);
            if (fsm.consultaProposta(opcaoCand).equals("Propostas não encontrada\n")) {
                System.out.println("Proposta não encontrada.");
                }
            else if(fsm.propostaAtribuida(opcaoCand)){
                System.out.println("Proposta já atribuida.");

            } else if (!opcaoCand.equalsIgnoreCase("sair"))
                opcoes.add(opcaoCand);
        } while (!opcaoCand.equalsIgnoreCase("sair"));

        if(opcoes.size() != 0)
        fsm.editCandidatura(num, opcoes);
        else
            System.out.println("Opcoes não alteradas, nenhuma proposta foi escolhida.");
    }


    /** Consulta uma candidatura.
     */
    private void consultarCandidaturas(){
        long num = PAInput.readLong("Digite o Numero do aluno da Candidatura: ");
        System.out.println(fsm.consultaCandidatura(num));
    }

    /** Representa o menu para gerir as candidaturas.
     */
    private void gestaoCandidaturas(){
        boolean finish_help = false;
        if(fsm.getFechado(2)) {
            while (!finish_help) {
                int opcao = PAInput.chooseOption("O que pretende fazer:", "Consultar", "Sair");
                switch (opcao) {
                    case 1 -> consultaProposta();
                    case 2 -> finish_help = true;
                }
            }
        }
        while(!finish_help) {
            int opcao = PAInput.chooseOption("Escolha a opção que pretende:", "Inserir", "Consulta", "Editar", "Remover", "Sair");
            switch (opcao) {
                case 1 -> inserirCandidaturas();
                case 2 -> consultarCandidaturas();
                case 3 -> editarCandidaturas();
                case 4 -> removerCandidaturas();
                case 5 -> finish_help = true;
            }
        }
    }


    /** Fecha a fase dois.
     */
    private void fecharFase2(){
        if(!fsm.getFaseAnteriorFechado(2)){
            System.out.println("Fase um ainda não fechada.");

        }else{
            fsm.setFechado(true, 2);
            System.out.println("Fase fechada com sucesso");
            fsm.mudaEstado();
        }
    }

    /** Representa a função para listar os alunos.
     */
    private void listarAlunos(){
        System.out.println(fsm.listarAlunos());
    }

    /** Representa a função para listar as propostas.
     */
    private void listarPropostas(){
        int opcao = PAInput.chooseOption("Qual criterios quer usar:\n",
                "Nenhum", "Autopropostas de alunos", "Propostas de docentes");

         int crit = PAInput.chooseOption("Escolha qual criterio quer usar : ",
                    "Nenhum", "Propostas com candidaturas", "Propostas sem candidaturas");

        System.out.println( fsm.consultaPropostas(opcao, crit));


    }
    /** Exporta para um ficheiro a informção da segunda fase.
     */
    private void exportarFase2(){
        String nome = PAInput.readString("Digite o nome do ficheiro onde quer guardar os dados: \n", true);
        fsm.exportFase2(nome);
    }
    /** Representa o menu para gerir a fase dois.
     */
    public void faseDoisUI(){
        int opcao = PAInput.chooseOption("Escolha a opção que pretende:", "Gerir candidaturas","Obter lista de Alunos",
                "Obter lista de propostas ","Fechar fase", "Voltar à fase anterior", "Avançar de fase",
                "Save", "Exportar", "Sair");
        switch (opcao){
            case 1 -> gestaoCandidaturas();
            case 2 -> listarAlunos();
            case 3 -> listarPropostas();
            case 4 -> fecharFase2();
            case 5 -> fsm.backState();
            case 6 -> fsm.mudaEstado();
            case 7 -> fsm.save();
            case 8 -> exportarFase2();
            case 9 -> finish = true;
        }

    }
    /** Fecha a fase três.
     */
    private void fecharFase3(){
        if(fsm.verificaAlunosCandidaturas()){
            fsm.setFechado(true, 3);
            fsm.mudaEstado();
        }
        else
            System.out.println("Fase não fechada. Alunos sem projeto atribuido.");
    }

    /** Representa a atribuição automatica das propostas aos alunos.
     */
    private void atribuicaoAutoProp(){
        fsm.atribuicaoAutoProp();
    }

    /** Lista a associação entre de um aluno com proposta.
     */
    private void listaAssociacao(){
        int opcao = PAInput.chooseOption("Qual critério quer usar: ", "Com autoproposta associada",
                "Com candidatura já registada",
                "Com proposta atribuida" ,"Sem proposta atribuida");
        if(opcao == 3){
            int ordem = PAInput.chooseOption("Ordem de Preferencia", "Estágio", "Projecto");
            System.out.println(fsm.listaAssociacao(opcao, ordem));
        }else
        System.out.println(fsm.listaAssociacao(opcao));
    }

    /** Representa a associação manual de um aluno com uma proposta.
     */
    private void associacaoManual(){
        System.out.println(fsm.alunosSemAtribuicao());
        long numAluno = PAInput.readLong("Digite o aluno que pretende aossociar: ");
        if(fsm.consulta(numAluno).compareToIgnoreCase("aluno não encontrado\n") == 0){
            System.out.println("Aluno não encontrado.");
            return;
        }
        if(!fsm.isSemAtribuicao(numAluno)){
            System.out.println("Aluno já com associação realizada");
        }else{
            System.out.println(fsm.propostasSemAtribuicao());
            String prop = PAInput.readString("Digite a proposta que pretende associar: ", true);
            if(fsm.consultaProposta(prop).compareToIgnoreCase("Propostas não encontrada\n") == 0){
                System.out.println("Proposta não econtrada.");
                return;
            }

            if(!fsm.verificaRamoPropAluno(numAluno,prop)){
                System.out.println("Proposta não destinada a àrea do aluno.");
                return;
            }
            if(!fsm.isPropSemAtribuicao(prop)){
                System.out.println("Proposta já associada.");
            }
            else{
                if(fsm.atribuicaoManual(numAluno, prop))
                System.out.println("Proposta associada com sucesso");
                else
                    System.out.println("Erro na associção do aluno");
            }
        }
    }

    /** Lista as propostas na fase três.
     */
    private void listaPropostasF3(){
        int opcao = PAInput.chooseOption("Qual criterios quer usar:\n",
                "Nenhum", "Autopropostas de alunos", "Propostas de docentes");

        int crit = PAInput.chooseOption("Escolha qual criterio quer usar : ",
                "Nenhum", "Propostas disponiveis", "Propostas atribuidas");
        System.out.println(fsm.listaPropostasF3(opcao, crit));

    }

    /** Remove a associação de um aluno com uma proposta manualmente.
     */
    private void removeManualF3(){
        long numAluno = PAInput.readLong("Digite o aluno que pretende remover associação: ");
        if(fsm.consulta(numAluno).compareToIgnoreCase("aluno não encontrado\n") == 0){
            System.out.println("Aluno não encontrado.");
            return;
        }
        if(fsm.isSemAtribuicao(numAluno)){
            System.out.println("Aluno sem associação realizada");
            return;
        }
        if(fsm.removePropostaDoAluno(numAluno)){
            System.out.println("Proposta removida do aluno");
        }else
            System.out.println("Proposta não removida do aluno");

    }

    /** Atribui as propostas disponiveis aos alunos também disponiveis.
     */
    private void atribuicaoPropDisponiveis(){
        Map<String, List<Long>>  alunosList = new HashMap<>();
        for (int i = 0; i < fsm.getNAlunos(); i++) {
            alunosList = fsm.atribuicaoPropDisponiveis();
            if(alunosList != null){
                System.out.println("Alunos Empatados e a proposta a associar: ");
                for(Map.Entry<String, List<Long>> entry : alunosList.entrySet()) {
                    String propostaAdd = entry.getKey();
                    List<Long> value = entry.getValue();
                    System.out.println(fsm.consulta(value.get(0)));
                    System.out.println(fsm.consulta(value.get(1)));
                    System.out.println(fsm.consultaProposta(propostaAdd));
                    int aluno = PAInput.chooseOption("Aluno escolhido", "Aluno 1", "Aluno 2 ");
                    if(aluno == 1)
                        fsm.atribuiAlunoEmpatado( value.get(0),propostaAdd);
                    else
                        fsm.atribuiAlunoEmpatado( value.get(1),propostaAdd);
                    break;
                }
            }
        }
    }
    /** Exporta para um ficheiro a informção da terceira fase.
     */
    private void exportarFase3(){
        String nome = PAInput.readString("Digite o nome do ficheiro onde quer guardar os dados: \n", true);
        fsm.exportarFase3(nome);
    }

    /** Representa o menu para gerir a fase três.
     */
    private void faseTresUI(){
        if(!fsm.getFaseAnteriorFechado(3)) {
            int opcao = PAInput.chooseOption("Escolha a opção que pretende:", "atribuição automatica","listar Alunos",
                    "Listar Propostas","Fechar fase", "Voltar a fase anterior", "Avancar fase",
                    "Save","Exportar", "Sair");
            switch (opcao) {
                case 1 -> atribuicaoAutoProp();
                case 2 -> listaAssociacao();
                case 3 -> listaPropostasF3();
                case 4 -> fecharFase3();
                case 5 -> fsm.backState();
                case 6 -> fsm.mudaEstado();
                case 7 -> fsm.save();
                case 8 -> exportarFase3();
                case 9 -> finish = true;
            }
        }
        else {
            int opcao = PAInput.chooseOption("Escolha a opção que pretende:", "Atribuição automatica",
                    "Atribução automática propostas disponiveis", "Atribuicão manual", "Remoção Manual", "listar Alunos",
                    "Listas Propostas", "Fechar fase", "Voltar a fase anterior", "Avancar fase", "Save", "Exportar", "Sair");
            switch (opcao) {
                case 1 -> atribuicaoAutoProp();
                case 2 -> atribuicaoPropDisponiveis();
                case 3 -> associacaoManual();
                case 4 -> removeManualF3();
                case 5 -> listaAssociacao();
                case 6 -> listaPropostasF3();
                case 7 -> fecharFase3();
                case 8 -> fsm.backState();
                case 9 -> fsm.mudaEstado();
                case 10 -> fsm.save();
                case 11 -> exportarFase3();
                case 12 -> finish = true;
            }
        }

    }
    /** Fecha a fase quatro.
     */
    private void fecharFase4(){
        fsm.setFechado(true, 4);
        fsm.mudaEstado();
    }
    /** Representa a atribuição automática das propostas aos docentes.
     */
    private void atribuicaoAutoDocenteProp(){
        fsm.atribuicaoAutoDocentesProp();
    }
    /** Representa a atribuição manual das propostas aos docentes.
     */
    private void atribuicaoManualDocenteProp(){
        String docente = PAInput.readString("Digite o email docente que pretende associar: ", true);
        if(fsm.consultaDocente(docente).compareToIgnoreCase("Docente não encontrado\n") == 0){
            System.out.println("Nome do docente não encontrado");
            return;
        }

        String codigo = PAInput.readString("Digite o codigo da proposta que quer associar: ", true);
        if(fsm.consultaProposta(codigo).compareToIgnoreCase("proposta não encontrada\n") == 0){
            System.out.println("Codigo da proposta não encontrada");
            return;
        }
        if(!fsm.isPropAlunoAtribuida(codigo)){
            System.out.println("Proposta sem aluno atribuido");
            return;
        }
        if(fsm.isPropDocenteAssociado(docente,codigo)){
            System.out.println("Proposta com docente já associado");
            return;
        }

        fsm.adicionaPropDocente(docente, codigo);
    }
    /** Consulta um orientador.
     */
    private void consultaOrientador(){
        String docente = PAInput.readString("Digite o email docente que pretende consultar: ", true);
        if(fsm.consultaDocente(docente).compareToIgnoreCase("Docente não encontrado\n") == 0){
            System.out.println("Nome do docente não encontrado");
            return;
        }
        System.out.println(fsm.consultaOrientadores(docente));
    }
    /** Elimina a associação entre uma proposta e um docente.
     */
    private void eliminarOrientadorAssociado(){
        String docente = PAInput.readString("Digite o email docente que pretende consultar: ", true);
        if(fsm.consultaDocente(docente).compareToIgnoreCase("Docente não encontrado\n") == 0){
            System.out.println("Nome do docente não encontrado");
            return;
        }

        fsm.eliminarOrientadorAssociado(docente);
    }
    /** Altera um orientador de uma proposta.
     */
    private void alterarOrientador(){
        String docenteAlterar = PAInput.readString("Digite o email do docente que pretende alterar: ", true);
        if(fsm.consultaDocente(docenteAlterar).compareToIgnoreCase("Docente não encontrado\n") == 0){
            System.out.println("Nome do docente não encontrado");
            return;
        }
        if(!fsm.isOrientadorAtribuido(docenteAlterar)){
            System.out.println("Docente sem propostas atribuidas. Impossivel de alterar");
            return;
        }

        String docenteNovo = PAInput.readString("Digite o email do novo docente : ", true);
        if(fsm.consultaDocente(docenteNovo).compareToIgnoreCase("Docente não encontrado\n") == 0){
            System.out.println("Nome do docente não encontrado");
            return;
        }
        fsm.alteraOrientador(docenteAlterar, docenteNovo);

    }

    /** Representa o menu para gerir os orientadores.
     */
    private void gerirOrientadores(){
        boolean finish_help = false;
        while(!finish_help){
            int opcao = PAInput.chooseOption("Escolha a opção que pretende fazer: ", "Atruibuir","Consulta", "Alterar"
                    , "Eliminar", "Sair");
            switch (opcao) {
             case 1 -> atribuicaoManualDocenteProp();
             case 2 -> consultaOrientador();
             case 3 -> alterarOrientador();
             case 4 -> eliminarOrientadorAssociado();
             case 5 -> finish_help = true;
            }
        }
    }
    /** Lista os orientadores.
     */
    private void listarOrientadores(){
        int opcao = PAInput.chooseOption("Opção de listagem:", "Estudantes com proposta atribuida e orientador associado",
                "Estudantes com proposta atribuida sem orientador associado", "Numero de orientações por docente");
        System.out.println(fsm.listarOrientadores(opcao));
    }


    /** Exporta para um ficheiro a informção da quarta e quinta fase.
     */
    private void exportarFase4E5(){
        String nome = PAInput.readString("Digite o nome do ficheiro onde quer guardar os dados: \n", true);
        fsm.exportarFase4E5(nome);
    }

    /** Representa o menu para gerir a fase quatro.
     */
    private void faseQuatroUI(){
        if(!fsm.getFechado(4)){
            int opcao = PAInput.chooseOption("Escolha a opção que pretende:", "Atribuição automática", "Atribuição manual"
                    ,"Listar orientadores","Voltar à fase anterior", "Fechar fase", "Save", "Exportar", "Sair");
            switch (opcao) {
                case 1 -> atribuicaoAutoDocenteProp();
                case 2 -> gerirOrientadores();
                case 3 -> listarOrientadores();
                case 4 -> fsm.backState();
                case 5 -> fecharFase4();
                case 6 -> fsm.save();
                case 7 -> exportarFase4E5();
                case 8 -> finish = true;
            }
        }else {
            int opcao = PAInput.chooseOption("Escolha a opção que pretende:",
                    "Listar orientadores","Voltar à fase anterior", "Sair");
            switch (opcao) {
                case 3 -> listarOrientadores();
                case 4 -> fsm.backState();
                case 5 -> finish = true;
            }
        }
    }
    /** Lista toda a informação necessária acerca do programa.
     */
    private void listaFinal(){
        int opcao = PAInput.chooseOption("O que pretende visualizar:", "Lista estudantes com propostas",
                "Lista de estudantes sem propostas e com candidatura", "Propostas disponiveis", "Propostas atribuidas");
        System.out.println(fsm.listaFinalF5(opcao));
    }

    /** Representa o menu para gerir a fase cinco.
     */
    private void faseCinco(){
        int opcao = PAInput.chooseOption("Digite a opcao pretendida: ", "Lista", "Save", "Exportar", "Sair");

        switch (opcao){
            case 1 -> listaFinal();
            case 2 -> fsm.save();
            case 3 -> exportarFase4E5();
            case 4 -> finish = true;
        }
    }
    /** Representa o início da interface de texto
     */
    public void start() {
        System.out.println("Inicio do Programa!");
        fsm.start();
        while (!finish) {
            switch (fsm.getState()) {
                case FASE_UM -> faseUmUI();
                case FASE_DOIS -> faseDoisUI();
                case FASE_TRES -> faseTresUI();
                case FASE_QUATRO ->  faseQuatroUI();
                case FASE_CINCO ->  faseCinco();
            }
        }
    }

}
