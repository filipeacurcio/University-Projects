package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.data.Candidaturas;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
import pt.isec.pa.apoio_poe.model.fsm.ImageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/** Representa a classe que gere as candidaturas.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */


public class GestaoCandidaturasUI extends BorderPane {
    FaseContextManager fsm;
    HBox menuInferior;
    ComboBox<String> comboBox, comboBoxEditar;
    HBox inserir, editar,remover, consulta;
    Button voltarFase, btnInserir, btnConsulta, btnRemover, btnEditar;
    ListView<String> listView;
    ComboBox<Long> d, dRemover, dEditar;
    TextField nomeFicheiro, numeroAluno, numeroAlunoRemover, valorEditar,numeroAlunoEditar;

    /** Cria uma classe para gerir as candidaturas.
     * @param fsm Dados da máquina de estado.
     */
    public GestaoCandidaturasUI(FaseContextManager fsm){
        this.fsm = fsm;
        menuInferior = new HBox(25);
        comboBox = new ComboBox<>();
        comboBoxEditar = new ComboBox<>();
        inserir = new HBox();
        editar = new HBox();
        remover = new HBox();
        consulta = new HBox();
        listView = new ListView<>();
        nomeFicheiro = new TextField();
        numeroAluno = new TextField();
        numeroAlunoEditar = new TextField();
        numeroAlunoRemover = new TextField();
        valorEditar = new TextField();
        createViews();
        registerHandlers();
        update();
    }

    private void update() {
        d.getItems().clear();
        Set<Candidaturas> alunos = fsm.getCandidaturas();
        for (Candidaturas aluno : alunos) {
            d.getItems().add(aluno.getNumero());
        }
        dRemover.getItems().clear();
        for (Candidaturas aluno : alunos) {
            dRemover.getItems().add(aluno.getNumero());
        }
        dEditar.getItems().clear();
        for (Candidaturas aluno : alunos) {
            dEditar.getItems().add(aluno.getNumero());
        }
    }

    /** Cria as vistas.
     */
    private void createViews() {
        d = new ComboBox<>();
        Set<Candidaturas> alunos = fsm.getCandidaturas();
        for (Candidaturas aluno : alunos) {
            d.getItems().add(aluno.getNumero());
        }
        dRemover = new ComboBox<>();
        for (Candidaturas aluno : alunos) {
            dRemover.getItems().add(aluno.getNumero());
        }
        dEditar = new ComboBox<>();
        for (Candidaturas aluno : alunos) {
            dEditar.getItems().add(aluno.getNumero());
        }
        numeroAluno.setMaxWidth(200);
        numeroAluno.setPromptText("Numero do aluno");
        numeroAlunoEditar.setPromptText("Numero do aluno");
        numeroAlunoRemover.setMaxWidth(200);
        numeroAlunoRemover.setPromptText("Numero do aluno a remover");
        nomeFicheiro.setMaxWidth(200);
        nomeFicheiro.setPromptText("Nome do ficheiro .cvs");
        valorEditar.setPromptText("Novas propostas (separadas por \",\")");
        if(!fsm.getFechado(2)){
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
                "Propostas"
        );

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
        voltarFase = new Button("Voltar fase Dois");
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

        inserir.getChildren().addAll(btnInserir);
        consulta.getChildren().addAll(consutaVbox, listView);
        remover.getChildren().addAll(dRemover, btnRemover);
        editar.getChildren().addAll(btnEditar);
        VBox editarVBox = new VBox(25);
        HBox editarHBox = new HBox(25);

        editarHBox.getChildren().addAll(comboBoxEditar, valorEditar);
        editarVBox.getChildren().addAll(dEditar ,editarHBox , btnEditar);
        editar.getChildren().add(editarVBox);


        StackPane stackPane = new StackPane(inserir, consulta, remover, editar);
        this.setCenter(stackPane);

    }

    /** Regista os handlers.
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
                valorEditar.clear();
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
                listView.getItems().add(fsm.consultaCandidatura(d.getValue()));
        });

        btnInserir.setOnAction(actionEvent -> {
            FileChooser fil_chooser = new FileChooser();

            fil_chooser.setTitle("Select File");

            fil_chooser.setInitialDirectory(new File("."));

            File file = fil_chooser.showOpenDialog(this.getScene().getWindow());

            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            if( inserirCandidaturas(file.getAbsolutePath())== 0){
                a.setContentText("Dados carregados com sucesso!");
            }else{
                a.setContentText("Erro a carregar o ficheiro!");

            }
            a.show();
        });

        btnRemover.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);

                if(fsm.removeCandidatura(dRemover.getValue())){
                    a.setContentText("Candidatura removida com sucesso!");
                }else{
                    a.setContentText("Erro a remover a candidatura!");

                }
                a.show();
        });

        btnEditar.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
                    String propostas = valorEditar.getText();
                    String delimiter = ",";
                    int flag = 0;
                    List<String> props = new ArrayList<>(Arrays.asList(propostas.split(delimiter)));
                    for (String prop : props) {
                        if (Objects.equals(fsm.consultaProposta(prop), "Propostas não encontrada" +
                                "\n")) {
                            a.setContentText("Proposta :" + prop + " não encontrada!");
                            flag = 1;
                        }
                    }
                    if(flag == 0){
                        fsm.editCandidatura(dEditar.getValue(), props);
                        a.setContentText("Candidatura editada!");
                    }

                a.show();
        });

        voltarFase.setOnAction(actionEvent -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.close();
        });
    }

    /** Insere as candidaturas presentes num ficheiro
     * @param nomeFich Nome do ficheiro onde estão presentes as candidaturas
     * @return Valor que indica como a operação correu
     */
    private int inserirCandidaturas(String nomeFich){
        String delimiter = ",";
        try{
            File ficheiro = new File(nomeFich);
            FileReader fr = new FileReader(ficheiro);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String[] tempArr;
            while((line = br.readLine()) != null){
                tempArr = line.split(delimiter);
                ArrayList<String> aux = new ArrayList<>();
                for (int i = 1; i < tempArr.length; i++) {
                    if(fsm.propostaAtribuida(tempArr[i])) {
                        aux.add(tempArr[i]);
                    }
                }
                if(aux.size() != 0)
                    fsm.adicionaCandidatura(new Candidaturas(Long.parseLong(tempArr[0]), aux));

            }
            br.close();

        }catch (IOException ioe){
            ioe.printStackTrace();
            return -1;
        }
        return 0;
    }


}
