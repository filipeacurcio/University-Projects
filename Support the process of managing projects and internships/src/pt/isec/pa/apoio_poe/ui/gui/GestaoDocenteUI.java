package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.data.Docentes;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
import pt.isec.pa.apoio_poe.model.fsm.ImageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/** Representa a classe que gere os docentes.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class GestaoDocenteUI extends BorderPane {
    FaseContextManager fsm;
    HBox menuInferior;
    ComboBox<String> comboBox, comboBoxEditar;
    HBox inserir, editar,remover, consulta;
    Button voltarFase, btnInserir, btnConsulta, btnRemover, btnEditar;
    ListView<String> listView;
    ComboBox<String> d, dRemover, dEditar;
    TextField nomeFicheiro, numeroDocente, numeroDocenteRemover, valorEditar, numeroDocenteEditar;


    /** Cria uma classe para gerir os docentes.
     * @param fsm Dados da máquina de estado.
     */
    public GestaoDocenteUI(FaseContextManager fsm){
        this.fsm = fsm;
        menuInferior = new HBox(25);
        comboBox = new ComboBox<>();
        comboBoxEditar = new ComboBox<>();
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
        d.getItems().clear();
        Set<Docentes> alunos = fsm.getDocentes();
        for (Docentes aluno : alunos) {
            d.getItems().add(aluno.getEmail());
        }
        dRemover.getItems().clear();
        for (Docentes aluno : alunos) {
            dRemover.getItems().add(aluno.getEmail());
        }

        dEditar.getItems().clear();
        for (Docentes aluno : alunos) {
            dEditar.getItems().add(aluno.getEmail());
        }
    }


    /** Cria as vistas.
     */
    private void createViews() {
        d = new ComboBox<>();
        Set<Docentes> alunos = fsm.getDocentes();
        for (Docentes aluno : alunos) {
            d.getItems().add(aluno.getEmail());
        }
        dRemover = new ComboBox<>();
        for (Docentes aluno : alunos) {
            dRemover.getItems().add(aluno.getEmail());
        }
        dEditar = new ComboBox<>();
        dEditar.getItems().clear();
        for (Docentes aluno : alunos) {
            dEditar.getItems().add(aluno.getEmail());
        }

        numeroDocente.setMaxWidth(200);
        numeroDocente.setPromptText("Email do docente");
        numeroDocenteEditar.setPromptText("Email do docente");
        numeroDocenteRemover.setMaxWidth(200);
        numeroDocenteRemover.setPromptText("Email do docente a remover");
        nomeFicheiro.setMaxWidth(200);
        nomeFicheiro.setPromptText("Nome do ficheiro .cvs");
        valorEditar.setPromptText("Novo valor");
        if(!fsm.getFechado(1)){
            comboBox.getItems().addAll(
                    "Inserir",
                    "Editar",
                    "Consultar",
                    "Remover"
            );
            inserir.setVisible(true);
            consulta.setVisible(false);
            remover.setVisible(false);
            editar.setVisible(false);
            comboBox.setValue("Inserir");
        } else{
            comboBox.getItems().clear();
            comboBox.getItems().addAll(
                    "Consultar"
            );
            comboBox.setValue("Consultar");
            inserir.setVisible(false);
            consulta.setVisible(true);
            remover.setVisible(false);
            editar.setVisible(false);

        }

        comboBoxEditar.getItems().addAll(
                "Nome"
        );
        comboBoxEditar.setValue("Nome");

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
        voltarFase = new Button("Voltar fase Um");
        btnInserir = new Button("Inserir");
        btnConsulta = new Button("Consultar");
        btnRemover = new Button("Remover");
        btnEditar = new Button("Editar");
        menuInferior.setPadding(new Insets(15, 20, 15, 30));
        menuInferior.getChildren().addAll(voltarFase);
        inserir.setPadding(new Insets(175, 30, 0, 150));
        consulta.setPadding(new Insets(100, 30, 0, 30));
        remover.setPadding(new Insets(125, 30, 0, 30));
        editar.setPadding(new Insets(125, 30, 0, 30));
        this.setBottom(menuInferior);
        this.setLeft(comboBox);

        VBox consutaVbox = new VBox(25);
        consutaVbox.getChildren().addAll(d,btnConsulta);

        inserir.getChildren().addAll( btnInserir);
        consulta.getChildren().addAll(consutaVbox, listView);
        remover.getChildren().addAll(dRemover, btnRemover);
        editar.getChildren().addAll(btnEditar);
        VBox editarVBox = new VBox(25);
        HBox editarHBox = new HBox(25);

        editarHBox.getChildren().addAll(comboBoxEditar, valorEditar);
        editarVBox.getChildren().addAll(dEditar,editarHBox , btnEditar);
        editar.getChildren().add(editarVBox);


        StackPane stackPane = new StackPane(inserir, consulta, remover, editar);
        this.setCenter(stackPane);

    }

    /** Regista os handlers para os controlos.
     */
    private void registerHandlers() {
        fsm.addPropertyChangeListener(FaseContextManager.PROP_DATA, evt -> update());

        comboBox.setOnAction(actionEvent -> {
            if(Objects.equals(comboBox.getValue(), "Inserir")){
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
            else if (Objects.equals(comboBox.getValue(), "Editar")){
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
            listView.getItems().clear();
                listView.getItems().add(fsm.consultaDocente(d.getValue()));
        });

        btnEditar.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);

                    if (Objects.equals(valorEditar.getText(), "")) {
                        a.setContentText("Valor a editar vazio");

                    } else {
                        if (Objects.equals(comboBoxEditar.getValue(), "Nome")) {
                            fsm.editNomeDocente(dEditar.getValue(), valorEditar.getText());
                            a.setContentText("Docente editado com sucesso");

                        }
                    }
                a.show();
        });

        btnInserir.setOnAction(actionEvent -> {
            FileChooser fil_chooser = new FileChooser();

            fil_chooser.setTitle("Select File");

            fil_chooser.setInitialDirectory(new File("."));

            File file = fil_chooser.showOpenDialog(this.getScene().getWindow());

            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            if( inserirDocentes(file.getAbsolutePath())== 0){
                a.setContentText("Dados carregados com sucesso!");
            }else{
                a.setContentText("Erro a carregar o ficheiro!");

            }
            a.show();
        });

        btnRemover.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);

                if(fsm.removeDocente(dRemover.getValue())){
                    a.setContentText("Docente removido com sucesso!");
                }else{
                    a.setContentText("Erro a remover o Docente!");
                }
                a.show();
        });

        voltarFase.setOnAction(actionEvent -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.close();
        });
    }

    /** Insere os docentes presentes num ficheiro
     * @param nomeFich Nome do ficheiro onde estão presentes os docentes
     * @return Valor que indica como a operação correu
     */
    private int inserirDocentes(String nomeFich){
        String delimiter = ",";
        try{
            File ficheiro = new File(nomeFich);
            FileReader fr = new FileReader(ficheiro);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String[] tempArr;
            while((line = br.readLine()) != null){
                tempArr = line.split(delimiter);
                if(tempArr.length != 2){
                    return -1;
                }
                fsm.adicionaDocente(new Docentes(tempArr[0], tempArr[1]));
            }
            br.close();

        }catch (IOException ioe){
            ioe.printStackTrace();
            return -1;
        }
        return 0;
    }
}
