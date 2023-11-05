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

/** Representa a classe fase quatro da máquina de estados.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class FaseQuatroUI extends BorderPane {
    ListView<String> listView;
    FaseContextManager fsm;
    ComboBox<String> comboBox;
    Button btnConfig;
    TextField ficheiroExportar;
    Menu menu, menuFase;
    MenuItem mSave, mExportar, mFaseSeguinte, mFecharFase, mFaseAnterior, mSair, mUndo, mRedo;

    /** Cria uma classe fase quatro da máquina de estados.
     * @param fsm Dados da máquina de estado.
     */
    public FaseQuatroUI(FaseContextManager fsm){
        this.fsm = fsm;
        comboBox = new ComboBox<>();
        createViews();
        registerHandlers();
        update();
    }
    /** Cria as vistas
     */
    private void createViews() {
        //this.setStyle("-fx-background-color: #F7DFA6;");
        listView = new ListView<>();
        ficheiroExportar = new TextField();
        ficheiroExportar.setPromptText("Nome do ficheiro a exportar");
        menu = new Menu("Menu");
        mSave = new MenuItem("Save");
        mExportar = new MenuItem("Exportar");
        menuFase = new Menu("Fase");
        mFecharFase = new MenuItem("Fechar Fase");
        mFaseSeguinte = new MenuItem("Fase Seguinte");
        mSair = new MenuItem("Sair");
        mFaseAnterior = new MenuItem("Fase Anterior");
        mRedo = new MenuItem("Redo");
        mRedo.setDisable(true);
        mUndo = new MenuItem("Undo");
        mUndo.setDisable(true);
        Label spacing = new Label("\t\t");

        comboBox.getItems().addAll(
          "Atribuição automática",
                "Atribuição manual",
                "Listar orientadores"
        );
        comboBox.setValue("Atribuição automática");
        btnConfig = new Button("Configurar");
        HBox hBox = new HBox(25);
        VBox vBox = new VBox();
        SeparatorMenuItem sep = new SeparatorMenuItem();
        menu.getItems().add(mSave);
        menu.getItems().add(mExportar);
        menu.getItems().add(mSair);
        menu.getItems().add( 2, sep);
        menuFase.getItems().add(mFaseSeguinte);
        menuFase.getItems().add(mFaseAnterior);
        menuFase.getItems().add(mFecharFase);
        menuFase.getItems().add(mUndo);
        menuFase.getItems().add(mRedo);

        MenuBar mb = new MenuBar();
        mb.getMenus().addAll(menu, menuFase);
        this.setTop(mb);

        hBox.getChildren().addAll(comboBox, btnConfig);
        vBox.getChildren().addAll(hBox);
        hBox.setPadding(new Insets(125, 0, 0, 200));

        this.setCenter(vBox);
    }
    /** Regista os handlers
     */
    private void registerHandlers() {
        fsm.addPropertyChangeListener(FaseContextManager.PROP_STATE, evt -> update());
        fsm.addPropertyChangeListener(FaseContextManager.PROP_DATA, evt -> updateData());
        mFaseAnterior.setOnAction(actionEvent -> fsm.backState());

        mFaseSeguinte.setOnAction(actionEvent -> fsm.mudaEstado());

        mUndo.setOnAction(actionEvent -> {
            fsm.undo();
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


        btnConfig.setOnAction(actionEvent -> {

            if(Objects.equals(comboBox.getValue(), "Atribuição automática")){
                fsm.atribuicaoAutoDocentesProp();
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setContentText("Docentes atribuidos com sucesso!");
                a.show();

            }else if(Objects.equals(comboBox.getValue(), "Atribuição manual")){
                gerirOrientadores();

            }else if(Objects.equals(comboBox.getValue(), "Listar orientadores")){
                listaOrientadores();

            }

        });
        mRedo.setOnAction(actionEvent -> {
            fsm.redo();
        });

        mFecharFase.setOnAction(actionEvent -> {
            fsm.setFechado(true, 4);
            fsm.mudaEstado();
            mFecharFase.setDisable(true);
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setContentText("Fase fechada com sucesso!");
            a.show();

        });

        mSair.setOnAction(actionEvent -> {
            Platform.exit();
        });

        mExportar.setOnAction(actionEvent -> {
            FileChooser fil_chooser = new FileChooser();

            fil_chooser.setTitle("Select File");

            fil_chooser.setInitialDirectory(new File("."));

            File file = fil_chooser.showSaveDialog(this.getScene().getWindow());

            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);

            fsm.exportarFase4E5(file.getAbsolutePath());
            a.setContentText("Dados exportados com sucesso.");

            a.show();
        });

    }
    /** Atualiza as informações.
     */
    private void updateData() {
        mRedo.setDisable(!fsm.hasRedoOrientadores());
        mUndo.setDisable(!fsm.hasUndoOrientadores());

    }

    /** Atualiza as informações.
     */
    private void update() {
        this.setVisible(fsm != null && fsm.getState() == FaseState.FASE_QUATRO);
    }

    /** Representa a criação da nova janela para gerir os orientadores.
     */
    private void gerirOrientadores(){
        GestaoOrientadoresUI gestaoOrientadoresUI = new GestaoOrientadoresUI(fsm);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(gestaoOrientadoresUI, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    /** Representa a criação da nova janela para listar a associção dos orientadores.
     */
    private void listaOrientadores(){

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        VBox box = new VBox();
        box.setPadding(new Insets(10));
        listView.getItems().clear();

        // How to center align content in a layout manager in JavaFX
        box.setAlignment(Pos.CENTER);

        Label label = new Label("Lista Propostas");
        ComboBox<String> comboboxOpcao = new ComboBox<>();

        comboboxOpcao.getItems().addAll(
                "Estudantes com proposta atribuida e orientador associado",
                "Estudantes com proposta atribuida sem orientador associado",
                "Numero de orientações por docente"
        );

        comboboxOpcao.setValue("Estudantes com proposta atribuida e orientador associado");
        
        Button listar = new Button("Listar");

        listar.setOnAction(actionEvent -> {
            int opcao = 0;

            switch (comboboxOpcao.getValue()){
                case "Estudantes com proposta atribuida e orientador associado"-> opcao = 1;
                case "Estudantes com proposta atribuida sem orientador associado" -> opcao = 2;
                case "Numero de orientações por docente"  -> opcao = 3;

            }
            listView.getItems().clear();
            listView.getItems().add(fsm.listarOrientadores(opcao));

        });


        box.getChildren().addAll(label,comboboxOpcao, listView, listar);


        Scene scene = new Scene(box, 500, 300);
        stage.setScene(scene);
        stage.show();
    }
}
