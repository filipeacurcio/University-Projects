package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
import pt.isec.pa.apoio_poe.model.fsm.FaseState;
import pt.isec.pa.apoio_poe.model.fsm.ImageManager;

import java.io.File;
import java.util.Objects;

/** Representa a classe fase um da máquina de estados.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class FaseUmUI extends BorderPane {
    FaseContextManager fsm;
    Button btnModo;
    Menu menu, menuFase;
    MenuItem mLoad, mSave, mExportar, mFecharFase, mFaseSeguinte, mSair;
    TextField ficheiroExportar;
    ComboBox<String> comboBox;

    /** Cria uma classe fase um da máquina de estados.
     * @param fsm Dados da máquina de estado.
     */
    public FaseUmUI(FaseContextManager fsm){
        this.fsm = fsm;
        menu = new Menu("Menu");
        mLoad = new MenuItem("_Load");
        mSave = new MenuItem("_Save");
        mSair = new MenuItem("_Sair");
        mExportar = new MenuItem("_Exportar");
        menuFase = new Menu("Fase");
        mFecharFase = new MenuItem("_Fechar Fase");
        mFaseSeguinte = new MenuItem("_Fase Seguinte");
        comboBox = new ComboBox<>();
        createViews();
        registerHandlers();
        update();
    }
    /** Cria as vistas.
     */
    private void createViews() {
        //this.setStyle("-fx-background-color: #F7DFA6;");
        ficheiroExportar = new TextField();
        ficheiroExportar.setPromptText("Nome do ficheiro a exportar");
        btnModo = new Button("Configurar");
        comboBox.getItems().addAll(
                "Alunos",
                "Docentes",
                "Propostas"
        );
        comboBox.setValue("Alunos");
        SeparatorMenuItem sep = new SeparatorMenuItem();
        menu.getItems().add(mSave);
        menu.getItems().add(mLoad);
        menu.getItems().add(mExportar);
        menu.getItems().add(mSair);
        menu.getItems().add(3, sep);
        menuFase.getItems().add(mFaseSeguinte);
        menuFase.getItems().add(mFecharFase);

        MenuBar mb = new MenuBar();
        mb.getMenus().addAll(menu, menuFase);

        this.setTop(mb);

        Label spacing = new Label("\t\t\t\t");
        HBox hBox = new HBox(25);
        VBox vBox = new VBox();
        
        hBox.getChildren().addAll(comboBox, btnModo);
        vBox.getChildren().addAll(hBox);
        hBox.setPadding(new Insets(125, 0, 0, 200));

        this.setCenter(vBox);

    }
    /** Registas os handlers.
     */
    private void registerHandlers() {
        fsm.addPropertyChangeListener(FaseContextManager.PROP_STATE, evt -> update());

        btnModo.setOnAction(actionEvent -> {
            if(Objects.equals(comboBox.getValue(), "Alunos"))
            gestaoAlunos();

            if(Objects.equals(comboBox.getValue(), "Docentes"))
                gestaoDocentes();

            if(Objects.equals(comboBox.getValue(), "Propostas"))
               gestaoPropostas();

        });

        mFaseSeguinte.setOnAction(actionEvent -> fsm.mudaEstado());

        mExportar.setOnAction(actionEvent -> {
            FileChooser fil_chooser = new FileChooser();

            fil_chooser.setTitle("Select File");

            fil_chooser.setInitialDirectory(new File("."));

            File file = fil_chooser.showSaveDialog(this.getScene().getWindow());

            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);

                fsm.exportFase1(file.getAbsolutePath());
                a.setContentText("Dados exportados com sucesso.");

            a.show();
        });

        mLoad.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            if(fsm.load()){
                a.setContentText("Estado carregado com sucesso!");
            }else{
                a.setContentText("Erro no carregamento do estado.");
            }
            a.show();
        });
        mSair.setOnAction(actionEvent -> {
            Platform.exit();
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

        mFecharFase.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            if(fsm.getNPropostas() >= fsm.getNAlunos()){
                a.setContentText("Fase fechada com sucesso!");
                fsm.setFechado(true, 1);
                mFecharFase.setDisable(true);
            }else{
                a.setContentText("Não foi possível fechar a fase!");
            }

            a.show();
        });
    }
    /** Atualiza os valores.
     */
    private void update() {
        this.setVisible(fsm != null && fsm.getState() == FaseState.FASE_UM);
    }


    /** Representa a criação da nova janela para gerir os alunos.
     */
    private void gestaoAlunos(){
        GestaoAlunoUI faseGestaoAlunoUI = new GestaoAlunoUI(fsm);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(faseGestaoAlunoUI, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
    /** Representa a criação da nova janela para gerir os docentes.
     */
    private void gestaoDocentes(){
        GestaoDocenteUI faseGestaoDocentesUI = new GestaoDocenteUI(fsm);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(faseGestaoDocentesUI, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
    /** Representa a criação da nova janela para gerir as propostas.
     */
    private void gestaoPropostas(){
        GestaoPropostasUI faseGestaoPropostasUI = new GestaoPropostasUI(fsm);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(faseGestaoPropostasUI, 600, 400);
        stage.setScene(scene);
        stage.show();
    }


}
