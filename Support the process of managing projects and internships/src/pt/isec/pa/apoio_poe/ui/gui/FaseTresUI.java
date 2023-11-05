package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
import pt.isec.pa.apoio_poe.model.fsm.FaseState;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Representa a classe fase três da máquina de estados.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */
public class FaseTresUI extends BorderPane {
    FaseContextManager fsm;
    Button  btnModo;
    ComboBox<String> comboBox;
    TextField ficheiroExportar;
    ListView<String> listView;
    HBox hBox;
    Menu menu, menuFase;
    MenuItem mSave, mExportar, mFaseSeguinte, mFecharFase, mFaseAnterior, mSair, mUndo, mRedo;

    /** Cria uma classe fase três da máquina de estados.
     * @param fsm Dados da máquina de estado.
     */
    public FaseTresUI(FaseContextManager fsm){
        this.fsm = fsm;
        listView = new ListView<>();
        createViews();
        registerHandlers();
        update();
    }
    /** Cria as vistas.
     */
    private void createViews() {
        //this.setStyle("-fx-background-color: #F7DFA6;");
        comboBox = new ComboBox<>();
        ficheiroExportar = new TextField();
        ficheiroExportar.setPromptText("Nome do ficheiro a exportar");
        btnModo = new Button("Configurar");
        menu = new Menu("Menu");
        mSave = new MenuItem("Save");
        mExportar = new MenuItem("Exportar");
        Label spacing = new Label("\t\t");
        menuFase = new Menu("Fase");
        mFaseSeguinte = new MenuItem("Fase Seguinte");
        mFecharFase = new MenuItem("Fechar Fase");
        mFaseAnterior = new MenuItem("Fase Anterior");
        mUndo = new MenuItem("Undo");
        mRedo = new MenuItem("Redo");
        mUndo.setVisible(true);
        mRedo.setVisible(true);
        mSair = new MenuItem("Sair");
        SeparatorMenuItem sep = new SeparatorMenuItem();

        menuFase.getItems().add(mFaseSeguinte);
        menuFase.getItems().add(mFaseAnterior);
        menuFase.getItems().add(mFecharFase);
        menuFase.getItems().add(mRedo);
        menuFase.getItems().add(mUndo);

        comboBox.getItems().addAll(
            "Atribuição automatica",
                "listar Alunos",
                "Listas Propostas"
        );
        comboBox.setValue("Atribuição automatica");
        hBox = new HBox(25);
        VBox vBox = new VBox();
        menu.getItems().add(mSave);
        menu.getItems().add(mExportar);
        menu.getItems().add(mSair);
        menu.getItems().add(2, sep);

        MenuBar mb = new MenuBar();
        mb.getMenus().addAll(menu, menuFase);
        this.setTop(mb);

        hBox.getChildren().addAll(comboBox, btnModo);
        vBox.getChildren().addAll(hBox);
        hBox.setPadding(new Insets(125, 0, 0, 200));

        this.setCenter(vBox);

    }

    /** Regista os handlers.
     */
    private void registerHandlers() {
        fsm.addPropertyChangeListener(FaseContextManager.PROP_STATE, evt -> update());

        fsm.addPropertyChangeListener(FaseContextManager.PROP_DATA, evt -> updateData());

        mFaseAnterior.setOnAction(actionEvent -> fsm.backState());

        mFaseSeguinte.setOnAction(actionEvent -> fsm.mudaEstado());

        mUndo.setOnAction(actionEvent -> {
            fsm.undoProp();
        });

        mRedo.setOnAction(actionEvent -> {
            fsm.redoProp();
        });


        mExportar.setOnAction(actionEvent -> {
            FileChooser fil_chooser = new FileChooser();

            fil_chooser.setTitle("Select File");

            fil_chooser.setInitialDirectory(new File("."));

            File file = fil_chooser.showSaveDialog(this.getScene().getWindow());

            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);

            fsm.exportarFase3(file.getAbsolutePath());
            a.setContentText("Dados exportados com sucesso.");

            a.show();
        });

        mSave.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            if(fsm.save()){
                a.setContentText("Estado guardado com sucesso!");
            }else{
                a.setContentText("Erro ao guardar o estado da aplicação.");
            }
            a.show();
        });

        btnModo.setOnAction(actionEvent -> {
            listView.getItems().clear();
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            if(Objects.equals(comboBox.getValue(), "Atribuição automatica")){
                fsm.atribuicaoAutoProp();
                a.setContentText("Atribuição automatica feita com sucesso!");
                a.show();
                comboBox.getItems().clear();
                comboBox.getItems().addAll(
                        "Atribuição automatica",
                        "Atribução automática propostas disponiveis",
                        "Atribuicão manual",
                        "Remoção Manual",
                        "listar Alunos",
                        "Listas Propostas"
                );
                hBox.setPadding(new Insets(125, 0, 0, 150));
                comboBox.setValue("Atribuição automatica");

            }else if(Objects.equals(comboBox.getValue(), "Atribução automática propostas disponiveis")){
                atribuicaoAutomatica();
                a.setContentText("Atribuição automatica feita com sucesso!");
                a.show();
            }
            else if(Objects.equals(comboBox.getValue(), "Atribuicão manual")){
                associacaoManual();
            }

            else if(Objects.equals(comboBox.getValue(), "Remoção Manual")){
                remocaoManual();
            }
            else if(Objects.equals(comboBox.getValue(), "listar Alunos")){
                listaAssociacao();
            }
            else if(Objects.equals(comboBox.getValue(), "Listas Propostas")){
                listaPropostas();
            }

        });

        mSair.setOnAction(actionEvent -> {
            Platform.exit();
        });

        mFecharFase.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            if(fsm.verificaAlunosCandidaturas()){
                fsm.setFechado(true, 3);
                fsm.mudaEstado();
                a.setContentText("Fase fechada com sucesso!");
                mFecharFase.setDisable(true);
            }
            else{
                a.setContentText("Fase não fechada! Alunos sem projeto atribuido");
            }
            a.show();
        });
    }

    /** Atualiza a informação.
     */
    private void update() {
        this.setVisible(fsm != null && fsm.getState() == FaseState.FASE_TRES);
    }

    /** Representa a criação da nova janela para a listagem das propostas.
     */
    private void listaPropostas(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        VBox box = new VBox();
        box.setPadding(new Insets(10));

        // How to center align content in a layout manager in JavaFX
        box.setAlignment(Pos.CENTER);

        Label label = new Label("Lista Propostas");
        ComboBox<String> comboboxOpcao = new ComboBox<>();
        ComboBox<String> comboboxCrit = new ComboBox<>();
        comboboxOpcao.getItems().addAll(
                "Nenhum",
                "Autopropostas de alunos",
                "Propostas de docentes"
        );

        comboboxOpcao.setValue("Nenhum");

        comboboxCrit.getItems().addAll(
                "Nenhum",
                "Propostas disponiveis",
                "Propostas atribuidas"

        );
        comboboxCrit.setValue("Nenhum");

        Button listar = new Button("Listar");

        listar.setOnAction(actionEvent -> {
            int opcao = 0;
            switch (comboboxOpcao.getValue()){
                case "Nenhum"-> opcao = 1;
                case "Autopropostas de alunos" -> opcao = 2;
                case "Propostas de docentes"  -> opcao = 3;

            }

            int crit = 0;
            switch (comboboxCrit.getValue()){
                case "Nenhum"-> crit = 1;
                case "Propostas disponiveis" -> crit = 2;
                case "Propostas atribuidas"  -> crit = 3;

            }
            listView.getItems().clear();
            listView.getItems().add(fsm.listaPropostasF3(opcao, crit));
        });


        box.getChildren().addAll(label,comboboxOpcao, comboboxCrit ,listView, listar);


        Scene scene = new Scene(box, 500, 300);
        stage.setScene(scene);
        stage.show();
    }

    /** Representa a criação da nova janela para a listagem das associações.
     */
    private void listaAssociacao(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        VBox box = new VBox();
        box.setPadding(new Insets(10));

        // How to center align content in a layout manager in JavaFX
        box.setAlignment(Pos.CENTER);

        Label label = new Label("Lista Propostas");
        ComboBox<String> comboboxOpcao = new ComboBox<>();
        ComboBox<String> comboboxCrit = new ComboBox<>();
        comboboxOpcao.getItems().addAll(
                "Com autoproposta associada",
                "Com candidatura já registada",
                "Com proposta atribuida",
                "Sem proposta atribuida"
        );
        comboboxOpcao.setValue("Com autoproposta associada");

        comboboxCrit.getItems().addAll(
                "Estágio",
                "Projecto"
        );
        comboboxCrit.setValue("Estágio");


        Button listar = new Button("Listar");

        listar.setOnAction(actionEvent -> {
            int opcao = 0;
            switch (comboboxOpcao.getValue()){
                case "Com autoproposta associada"-> opcao = 1;
                case "Com candidatura já registada" -> opcao = 2;
                case "Com proposta atribuida"  -> opcao = 3;
                case "Sem proposta atribuida" -> opcao = 4;

            }

            int crit = 0;
            if(opcao == 3){
                switch (comboboxCrit.getValue()){
                    case "Estágio"-> crit = 1;
                    case "Projecto disponiveis" -> crit = 2;
                }
                listView.getItems().clear();
                listView.getItems().add(fsm.listaAssociacao(opcao, crit));
            }else{
                listView.getItems().clear();
                listView.getItems().add(fsm.listaAssociacao(opcao));
            }


        });


        box.getChildren().addAll(label,comboboxOpcao, comboboxCrit ,listView, listar);


        Scene scene = new Scene(box, 500, 300);
        stage.setScene(scene);
        stage.show();
    }
    /** Atualiza as informações.
     */
    private void updateData() {
        mRedo.setDisable(!fsm.hasRedoProps());
        mUndo.setDisable(!fsm.hasUndoProps());
        if(fsm.getFechado(1)){
            comboBox.getItems().clear();
            comboBox.getItems().addAll(
                    "Atribuição automatica",
                    "Atribuicão manual",
                    "Remoção Manual",
                    "listar Alunos",
                    "Listas Propostas"
            );
            hBox.setPadding(new Insets(125, 0, 0, 150));
            comboBox.setValue("Atribuição automatica");
        }
    }

    /** Representa a criação da nova janela para asssociar manualmente aluno a uma propostas.
     */
    private void associacaoManual(){
        AssociacaoManualAlunoUI associacaoManualAluno = new AssociacaoManualAlunoUI(fsm);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(associacaoManualAluno, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
    /** Representa a criação da nova janela para remover manualmente aluno a uma propostas.
     */
    private void remocaoManual(){
        RemocaoManualAlunoUI remocaoManualAluno = new RemocaoManualAlunoUI(fsm);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(remocaoManualAluno, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
    /** Representa a criação da nova janela para atribuir automaticamente uma propostas a um aluno.
     */
    private void atribuicaoAutomatica(){
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(Alert.AlertType.INFORMATION);
        Map<String, List<Long>> alunosList;
        for (int i = 0; i < fsm.getNAlunos(); i++) {
            alunosList = fsm.atribuicaoPropDisponiveis();
            if(alunosList != null){
                for(Map.Entry<String, List<Long>> entry : alunosList.entrySet()) {
                    String propostaAdd = entry.getKey();
                    List<Long> value = entry.getValue();

                    ChoiceDialog<Long> d = new ChoiceDialog<>();
                    d.setTitle("Qual quer Associar");
                    d.getItems().addAll(value.get(0),value.get(1));
                    d.initStyle(StageStyle.UNDECORATED);
                    d.initModality(Modality.APPLICATION_MODAL);
                    d.setContentText("Proposta : " + propostaAdd);

                    d.initOwner(this.getScene().getWindow());
                    d.showAndWait();


                   if(d.getSelectedItem().toString().compareToIgnoreCase(fsm.consulta(value.get(0))) == 0){
                       fsm.atribuiAlunoEmpatado( value.get(0),propostaAdd);

                   }else{
                       fsm.atribuiAlunoEmpatado( value.get(1),propostaAdd);
                   }

                }
            }
        }

    }
}
