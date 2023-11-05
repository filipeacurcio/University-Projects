package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
import pt.isec.pa.apoio_poe.model.fsm.ImageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/** Representa a classe que gere os alunos.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class GestaoAlunoUI extends BorderPane {
    FaseContextManager fsm;
    HBox menuInferior;
    ComboBox<String> comboBox, comboBoxEditar;
    HBox inserir, editar,remover, consulta;
    Button voltarFase, btnInserir, btnConsulta, btnRemover, btnEditar;
    ListView<String> listView;
    ComboBox<Long> d, dRemover, dEditar;
    TextField nomeFicheiro, numeroAluno, numeroAlunoRemover, valorEditar,numeroAlunoEditar;

    /** Cria uma classe para gerir os alunos.
     * @param fsm Dados da máquina de estado.
     */
    public GestaoAlunoUI(FaseContextManager fsm){
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
    /** Atualiza os valores.
     */
    private void update() {
        d.getItems().clear();
        Set<Alunos> alunos = fsm.getAlunos();
        for (Alunos aluno : alunos) {
            d.getItems().add(aluno.getNumEstudante());
        }
        dRemover.getItems().clear();
        for (Alunos aluno : alunos) {
            dRemover.getItems().add(aluno.getNumEstudante());
        }
        dEditar.getItems().clear();
        for (Alunos aluno : alunos) {
            dEditar.getItems().add(aluno.getNumEstudante());
        }
    }

    /** Cria as vistas.
     */
    private void createViews() {
        d = new ComboBox<>();
        Set<Alunos> alunos = fsm.getAlunos();
        for (Alunos aluno : alunos) {
            d.getItems().add(aluno.getNumEstudante());
        }
        dRemover = new ComboBox<>();
        for (Alunos aluno : alunos) {
            dRemover.getItems().add(aluno.getNumEstudante());
        }
        dEditar = new ComboBox<>();
        for (Alunos aluno : alunos) {
            dEditar.getItems().add(aluno.getNumEstudante());
        }
        numeroAluno.setMaxWidth(200);
        numeroAluno.setPromptText("Numero do aluno");
        numeroAlunoEditar.setPromptText("Numero do aluno");
        numeroAlunoRemover.setMaxWidth(200);
        numeroAlunoRemover.setPromptText("Numero do aluno a remover");
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
            comboBox.setValue("Inserir");
            inserir.setVisible(true);
            consulta.setVisible(false);
            remover.setVisible(false);
            editar.setVisible(false);
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
                "Nome",
                "Email",
                "Sigla Curso",
                "Sigla Ramo",
                "Classificação",
                "Possibilidade de acesso"
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
        remover.setPadding(new Insets(150, 30, 0, 30));
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
            listView.getItems().add(fsm.consulta(d.getValue()));
        });

        btnInserir.setOnAction(actionEvent -> {
            FileChooser fil_chooser = new FileChooser();

            fil_chooser.setTitle("Select File");

            fil_chooser.setInitialDirectory(new File("."));

            File file = fil_chooser.showOpenDialog(this.getScene().getWindow());

            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            if( inserirAlunos(file.getAbsolutePath())== 0){
                a.setContentText("Dados carregados com sucesso!");
            }else{
                a.setContentText("Erro a carregar o ficheiro!");

            }
            a.show();

        });

        btnRemover.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);

                if(fsm.remove(dRemover.getValue())){
                    a.setContentText("Aluno removido com sucesso!");
                }else{
                    a.setContentText("Erro a remover o aluno!");
                a.show();
            }
                update();
        });

        btnEditar.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
           if(Objects.equals(comboBoxEditar.getValue(), "Possibilidade de acesso")){
                fsm.editPossibilidade(dEditar.getValue());
                a.setContentText("Aluno editado com sucesso");

            }else if(Objects.equals(valorEditar.getText(), "")){
                        a.setContentText("Valor a editar vazio");
                    }else{
                        if(Objects.equals(comboBoxEditar.getValue(), "Nome")){
                            fsm.editNome(dEditar.getValue(), valorEditar.getText());
                            a.setContentText("Aluno editado com sucesso");

                        }else if(Objects.equals(comboBoxEditar.getValue(), "Email")){
                            fsm.editEmail(dEditar.getValue(), valorEditar.getText());
                            a.setContentText("Aluno editado com sucesso");


                        }else if(Objects.equals(comboBoxEditar.getValue(), "Sigla Curso")){
                            if(fsm.checkCurso(valorEditar.getText())){
                                fsm.editSiglaCurso(dEditar.getValue(), valorEditar.getText());
                                a.setContentText("Aluno editado com sucesso");

                            }else
                            a.setContentText("Sigla Inválida");

                        }else if(Objects.equals(comboBoxEditar.getValue(), "Sigla Ramo")){
                            if(fsm.checkRamo(valorEditar.getText())){
                                fsm.editSiglaRamo(dEditar.getValue(), valorEditar.getText());
                                a.setContentText("Aluno editado com sucesso");

                            }else
                            a.setContentText("Sigla Inválida");

                        }else if(Objects.equals(comboBoxEditar.getValue(), "Classificação")){
                            fsm.editClassificacao(dEditar.getValue(), Double.parseDouble(valorEditar.getText()));
                            a.setContentText("Aluno editado com sucesso");

                        }
                    }

                a.show();
        });

        voltarFase.setOnAction(actionEvent -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.close();
        });
    }
    /** Insere os alunos presentes num ficheiro
     * @param nomeFich Nome do ficheiro onde estão presentes os alunos
     * @return Valor que indica como a operação correu
     */
    private int inserirAlunos(String nomeFich){
        String delimiter = ",";
        try{
            File ficheiro = new File(nomeFich);
            FileReader fr = new FileReader(ficheiro);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String[] tempArr;
            while((line = br.readLine()) != null){
                tempArr = line.split(delimiter);

                if(tempArr.length != 7){
                    return -1;
                }

                if(!fsm.checkCurso(tempArr[3])){
                    tempArr[3] = "LEI";
                }
                if(!fsm.checkRamo(tempArr[4])){
                    tempArr[4] = "DA";
                }

                fsm.adiciona(new Alunos(Long.parseLong(tempArr[0]),tempArr[1],tempArr[2],tempArr[3],tempArr[4],
                        Double.parseDouble(tempArr[5]),Boolean.parseBoolean(tempArr[6])));
            }
            br.close();

        }catch (IOException ioe){
            ioe.printStackTrace();
            return -1;
        }

        return 0;
    }


}
