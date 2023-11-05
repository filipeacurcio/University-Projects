package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.data.Docentes;
import pt.isec.pa.apoio_poe.model.data.Propostas;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
import pt.isec.pa.apoio_poe.model.fsm.ImageManager;

import java.io.DataInput;
import java.util.Objects;
import java.util.Set;

/** Representa a classe que gere os orientadores.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */


public class GestaoOrientadoresUI extends BorderPane {
    FaseContextManager fsm;
    HBox menuInferior;
    ComboBox<String> comboBox, comboBoxEditar;
    HBox inserir, editar,remover, consulta;
    Button voltarFase, btnInserir, btnConsulta, btnRemover, btnEditar;
    ListView<String> listView;
    ComboBox<String> dConsultar, dRemover, dInserirDocentes, dInserirPropostas, dEditarNovo, dEditarAlterar;

    TextField nomeFicheiro, numeroDocente, numeroDocenteRemover, valorEditar, numeroDocenteEditar, propostaAssociar;


    /** Cria uma classe que gere os orientadores.
     * @param fsm Dados da máquina de estado.
     */
    public GestaoOrientadoresUI(FaseContextManager fsm){
        this.fsm = fsm;
        menuInferior = new HBox(25);
        comboBox = new ComboBox<>();
        comboBoxEditar = new ComboBox<>();
        propostaAssociar = new TextField();
        propostaAssociar.setPromptText("Proposta a associar");
        inserir = new HBox();
        editar = new HBox();
        remover = new HBox();
        consulta = new HBox();
        listView = new ListView<>();
        nomeFicheiro = new javafx.scene.control.TextField();
        numeroDocente = new javafx.scene.control.TextField();
        numeroDocenteEditar = new javafx.scene.control.TextField();
        numeroDocenteRemover = new javafx.scene.control.TextField();
        valorEditar = new TextField();
        createViews();
        registerHandlers();
        update();
    }

    private void update() {
        dInserirPropostas.getItems().clear();
        Set<Propostas> propostasSet = fsm.getPropOrientadorNaoAtribuidos();
        for (Propostas prop: propostasSet) {
            dInserirPropostas.getItems().add(prop.getCodigo());
        }

        dConsultar.getItems().clear();
        Set<Docentes> alunos = fsm.getDocentes();
        for (Docentes aluno : alunos) {
            dConsultar.getItems().add(aluno.getEmail());
        }
        dInserirDocentes.getItems().clear();
        for (Docentes aluno : alunos) {
            dInserirDocentes.getItems().add(aluno.getEmail());
        }
        dEditarNovo.getItems().clear();
        for (Docentes aluno : alunos) {
            dEditarNovo.getItems().add(aluno.getEmail());
        }

        dRemover.getItems().clear();
        Set<Docentes> alunos1 = fsm.getDocentesAtribuidos();
        for (Docentes aluno : alunos1) {
            dRemover.getItems().add(aluno.getEmail());
        }
        dEditarAlterar.getItems().clear();
        for (Docentes aluno : alunos1) {
            dEditarAlterar.getItems().add(aluno.getEmail());
        }

    }

    /** Cria as vistas.
     */
    private void createViews() {
        dInserirPropostas = new ComboBox<>();
        Set<Propostas> propostas = fsm.getPropOrientadorNaoAtribuidos();
        for (Propostas prop: propostas) {
            dInserirPropostas.getItems().add(prop.getCodigo());
        }

        dConsultar = new ComboBox<>();
        Set<Docentes> alunos = fsm.getDocentes();
        for (Docentes aluno : alunos) {
            dConsultar.getItems().add(aluno.getEmail());
        }
        dRemover = new ComboBox<>();
        Set<Docentes> alunos1 = fsm.getDocentesAtribuidos();
        for (Docentes aluno : alunos1) {
            dRemover.getItems().add(aluno.getEmail());
        }
        dEditarAlterar = new ComboBox<>();
        for (Docentes aluno : alunos1) {
            dEditarAlterar.getItems().add(aluno.getEmail());
        }

        dInserirDocentes = new ComboBox<>();
        for (Docentes aluno : alunos) {
            dInserirDocentes.getItems().add(aluno.getEmail());
        }
        dEditarNovo = new ComboBox<>();
        for (Docentes aluno : alunos) {
            dEditarNovo.getItems().add(aluno.getEmail());
        }
        numeroDocente.setMaxWidth(200);
        numeroDocente.setPromptText("Email do docente");
        numeroDocenteEditar.setPromptText("Email do docente");
        numeroDocenteRemover.setMaxWidth(200);
        numeroDocenteRemover.setPromptText("Email do docente a remover");
        nomeFicheiro.setMaxWidth(200);
        nomeFicheiro.setPromptText("Docente a adicionar");
        valorEditar.setPromptText("Novo valor");
        if(!fsm.getFechado(4)){
            comboBox.getItems().addAll(
                    "Atribuir",
                    "Alterar",
                    "Consultar",
                    "Remover"
            );
        } else{
            comboBox.getItems().clear();
            comboBox.getItems().addAll(
                    "Consultar"
            );
        }

        comboBoxEditar.getItems().addAll(
                "Email"
        );
        comboBoxEditar.setValue("Email");

        //this.setStyle("-fx-background-color: #F7DFA6;");
        Image aux = ImageManager.getImage("fundo.jpeg");

        if(aux != null) {

            this.setBackground(new Background(new BackgroundImage(
                    aux,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(1, 1, true, true, true, true)
            )));
        }
        voltarFase = new Button("Voltar fase Quatro");
        btnInserir = new Button("Inserir");
        btnConsulta = new Button("Consultar");
        btnRemover = new Button("Remover");
        btnEditar = new Button("Editar");
        menuInferior.setPadding(new Insets(15, 20, 15, 30));
        menuInferior.getChildren().addAll(voltarFase);
        inserir.setPadding(new Insets(125, 30, 0, 70));
        consulta.setPadding(new Insets(100, 30, 0, 30));
        remover.setPadding(new Insets(125, 30, 0, 30));
        editar.setPadding(new Insets(125, 30, 0, 30));
        this.setBottom(menuInferior);
        this.setLeft(comboBox);

        VBox consutaVbox = new VBox(25);
        consutaVbox.getChildren().addAll(dConsultar,btnConsulta);

        inserir.getChildren().addAll(dInserirDocentes, dInserirPropostas, btnInserir);
        consulta.getChildren().addAll(consutaVbox, listView);
        remover.getChildren().addAll(dRemover, btnRemover);
        editar.getChildren().addAll(btnEditar);
        VBox editarVBox = new VBox(25);
        HBox editarHBox = new HBox(25);

        editarHBox.getChildren().addAll(comboBoxEditar, dEditarNovo);
        editarVBox.getChildren().addAll(dEditarAlterar,editarHBox , btnEditar);
        editar.getChildren().add(editarVBox);


        StackPane stackPane = new StackPane(inserir, consulta, remover, editar);
        this.setCenter(stackPane);
        comboBox.setValue("Inserir");
        inserir.setVisible(true);
        consulta.setVisible(false);
        remover.setVisible(false);
        editar.setVisible(false);

    }

    /** Regista os handlers para os controlos.
     */
    private void registerHandlers() {
        fsm.addPropertyChangeListener(FaseContextManager.PROP_DATA, evt -> update());

        comboBox.setOnAction(actionEvent -> {
            if(Objects.equals(comboBox.getValue(), "Atribuir")){
                inserir.setVisible(true);
                consulta.setVisible(false);
                remover.setVisible(false);
                editar.setVisible(false);
            }
            else if (Objects.equals(comboBox.getValue(), "Consultar")){
                inserir.setVisible(false);
                remover.setVisible(false);
                consulta.setVisible(true);
                editar.setVisible(false);
            }
            else if (Objects.equals(comboBox.getValue(), "Alterar")){
                inserir.setVisible(false);
                consulta.setVisible(false);
                remover.setVisible(false);
                editar.setVisible(true);
            }
            else if (Objects.equals(comboBox.getValue(), "Remover")){
                inserir.setVisible(false);
                consulta.setVisible(false);
                remover.setVisible(true);
                editar.setVisible(false);
            }
        });


        btnConsulta.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            listView.getItems().clear();
                    listView.getItems().add(fsm.consultaOrientadores(dConsultar.getValue()));
        });

        btnEditar.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);

                 if(!fsm.isOrientadorAtribuido(dEditarAlterar.getValue())){
                    a.setContentText("Docente sem propostas atribuidas. Impossivel de alterar");
                }else{
                    fsm.alteraOrientador(dEditarAlterar.getValue(), dEditarNovo.getValue());
                    a.setContentText("Orientador alterado com sucesso");
                }
                a.show();
        });

        btnInserir.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            fsm.adicionaPropDocente(dInserirDocentes.getValue(), dInserirPropostas.getValue());
            a.setContentText("Docente associado com sucesso");
                    
            a.show();
        });

        btnRemover.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
                    fsm.eliminarOrientadorAssociado(dRemover.getValue());
                    a.setContentText("Docente removido com sucesso!");
                a.show();
            update();

        });

        voltarFase.setOnAction(actionEvent -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.close();
        });
    }

}
