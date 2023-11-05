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
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
import pt.isec.pa.apoio_poe.model.fsm.FaseState;

import java.io.File;
import java.util.Objects;
/** Representa a classe fase dois da máquina de estados.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class FaseDoisUI extends BorderPane {
    FaseContextManager fsm;
    Button  btnModo;
    TextField ficheiroExportar;
    ComboBox<String> comboBox;
    ListView<String> listView;
    Menu menu, menuFase;
    MenuItem mSave, mExportar, mFaseSeguinte, mFecharFase, mFaseAnterior, mSair;

    /** Cria uma classe fase dois da máquina de estados.
     * @param fsm Dados da máquina de estado.
     */
    public FaseDoisUI(FaseContextManager fsm){
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
        btnModo = new Button("Configurar");
        ficheiroExportar = new TextField();
        ficheiroExportar.setPromptText("Nome do ficheiro a exportar");
        comboBox = new ComboBox<>();
        mFaseAnterior = new MenuItem("Fase Anterior");
        mFaseSeguinte = new MenuItem("Fase Seguinte");
        mFecharFase  = new MenuItem("Fechar Fase");
        mSair = new MenuItem("Sair");
        menuFase = new Menu("Fase");

        menu = new Menu("Menu");
        mSave = new MenuItem("Save");
        mExportar = new MenuItem("Exportar");
        SeparatorMenuItem sep = new SeparatorMenuItem();
        menu.getItems().add(mSave);
        menu.getItems().add(mExportar);
        menu.getItems().add(mSair);
        menu.getItems().add(2, sep);
        menuFase.getItems().add(mFaseSeguinte);
        menuFase.getItems().add(mFaseAnterior);
        menuFase.getItems().addAll(mFecharFase);

        MenuBar mb = new MenuBar();
        mb.getMenus().addAll(menu, menuFase);

        this.setTop(mb);
        comboBox.getItems().addAll(
                "Candidaturas",
                "Lista Alunos",
                "Lista Propostas"
        );
        comboBox.setValue("Candidaturas");
        HBox hBox = new HBox(25);
        VBox vBox = new VBox();

        hBox.getChildren().addAll(comboBox, btnModo);
        vBox.getChildren().addAll(hBox);
        hBox.setPadding(new Insets(125, 0, 0, 200));

        this.setCenter(vBox);


    }

    /** Regista os handlers.
     */
    private void registerHandlers() {
        fsm.addPropertyChangeListener(FaseContextManager.PROP_STATE, evt -> update());

        btnModo.setOnAction(actionEvent -> {
            if(Objects.equals(comboBox.getValue(), "Lista Alunos")){
                listaAlunos();

            }else if(Objects.equals(comboBox.getValue(), "Candidaturas")){
                gestaoCandidaturas();

            }else if(Objects.equals(comboBox.getValue(), "Lista Propostas")){
                listaPropostas();
            }

        });

        mExportar.setOnAction(actionEvent -> {
            FileChooser fil_chooser = new FileChooser();

            fil_chooser.setTitle("Select File");

            fil_chooser.setInitialDirectory(new File("."));

            File file = fil_chooser.showSaveDialog(this.getScene().getWindow());

            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);

            fsm.exportFase2(file.getAbsolutePath());
            a.setContentText("Dados exportados com sucesso.");

            a.show();
        });

        mFaseSeguinte.setOnAction(actionEvent -> fsm.mudaEstado());

        mFaseAnterior.setOnAction(actionEvent -> fsm.backState());

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

        mSair.setOnAction(actionEvent -> {
            Platform.exit();
        });

        mFecharFase.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            if(!fsm.getFaseAnteriorFechado(2)){
                a.setContentText("Fase um ainda não fechada.");
            }else{
                fsm.setFechado(true,2);
                fsm.mudaEstado();
                a.setContentText("Fase fechada com sucesso.");
                mFecharFase.setDisable(true);
            }
            a.show();

        });


    }

    /** Atualiza as informções.
     */
    private void update() {
        this.setVisible(fsm != null && fsm.getState() == FaseState.FASE_DOIS);

    }

    /** Representa a criação da nova janela para listar os alunos.
     */
    private void listaAlunos(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox box = new VBox();
        box.setPadding(new Insets(10));

        // How to center align content in a layout manager in JavaFX
        box.setAlignment(Pos.CENTER);

        Label label = new Label("Lista Alunos");
        ListView<String> listView = new ListView<>();
        listView.getItems().clear();
        listView.getItems().add(fsm.listarAlunos());
        box.getChildren().addAll(label, listView);

        Scene scene = new Scene(box, 500, 300);
        stage.setScene(scene);
        stage.show();
    }

    /** Representa a criação da nova janela para listar as propostas.
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

        comboboxCrit.getItems().addAll(
                "Nenhum",
                "Propostas com candidaturas",
                "Propostas sem candidaturas"

        );

        comboboxOpcao.setValue("Nenhum");
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
                case "Propostas com candidaturas" -> crit = 2;
                case "Propostas sem candidaturas"  -> crit = 3;

            }

            listView.getItems().clear();
            listView.getItems().add(fsm.consultaPropostas(opcao, crit));
        });


        box.getChildren().addAll(label,comboboxOpcao, comboboxCrit ,listView, listar);


        Scene scene = new Scene(box, 500, 300);
        stage.setScene(scene);
        stage.show();
    }

    /** Representa a criação da nova janela para gerir as candidaturas.
     */
    private void gestaoCandidaturas(){
       GestaoCandidaturasUI faseGestaoCandidaturasUI = new GestaoCandidaturasUI(fsm);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(faseGestaoCandidaturasUI, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
}
