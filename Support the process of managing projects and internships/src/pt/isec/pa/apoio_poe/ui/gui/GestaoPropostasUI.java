package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.data.*;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
import pt.isec.pa.apoio_poe.model.fsm.ImageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/** Representa a classe que gere as propostas.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class GestaoPropostasUI extends BorderPane {
    FaseContextManager fsm;
    HBox menuInferior;
    ComboBox<String> comboBox, comboBoxEditar, comboBoxTipos;
    HBox inserir, editar,remover, consulta;
    Button voltarFase, btnInserir, btnConsulta, btnRemover, btnEditar;
    ListView<String> listView;
    ComboBox<String> d, dRemover, dEditar;
    TextField nomeFicheiro, numeroProposta, numeroPropostaRemover, valorEditar, numeroPropostaEditar;

    /** Cria uma classe para gerir as propostas.
     * @param fsm Dados da máquina de estado.
     */
    public GestaoPropostasUI(FaseContextManager fsm){
        this.fsm = fsm;
        menuInferior = new HBox(25);
        comboBox = new ComboBox<>();
        comboBoxEditar = new ComboBox<>();
        comboBoxTipos = new ComboBox<>();
        inserir = new HBox();
        editar = new HBox();
        remover = new HBox();
        consulta = new HBox();
        listView = new ListView<>();
        nomeFicheiro = new TextField();
        numeroProposta = new TextField();
        numeroPropostaEditar = new TextField();
        numeroPropostaRemover = new TextField();
        valorEditar = new TextField();
        createViews();
        registerHandlers();
        update();

    }

    private void update() {
        d.getItems().clear();
        Set<Propostas> alunos = fsm.getPropostas();
        for (Propostas aluno : alunos) {
            d.getItems().add(aluno.getCodigo());
        }
        dRemover.getItems().clear();
        for (Propostas aluno : alunos) {
            dRemover.getItems().add(aluno.getCodigo());
        }

        dEditar.getItems().clear();
        for (Propostas aluno : alunos) {
            dEditar.getItems().add(aluno.getCodigo());
        }
    }

    /** Cria as vistas.
     */
    private void createViews() {
        d = new ComboBox<>();
        Set<Propostas> alunos = fsm.getPropostas();
        for (Propostas aluno : alunos) {
            d.getItems().add(aluno.getCodigo());
        }
        dRemover = new ComboBox<>();
        for (Propostas aluno : alunos) {
            dRemover.getItems().add(aluno.getCodigo());
        }
        dEditar = new ComboBox<>();
        for (Propostas aluno : alunos) {
            dEditar.getItems().add(aluno.getCodigo());
        }
        numeroProposta.setMaxWidth(200);
        numeroProposta.setPromptText("Codigo da proposta");
        numeroPropostaEditar.setPromptText("Codigo da proposta");
        numeroPropostaRemover.setMaxWidth(200);
        numeroPropostaRemover.setPromptText("Codigo da proposta a remover");
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


        comboBoxTipos.getItems().addAll(
                "T1",
                "T2",
                "T3"
        );
        comboBoxTipos.setValue("T3");
        comboBoxEditar.getItems().addAll(
                "Titulo",
                "Numero do Aluno"
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

        editarHBox.getChildren().addAll(comboBoxTipos, comboBoxEditar, valorEditar);
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
                listView.getItems().add(fsm.consultaProposta(d.getValue()));
        });

        comboBoxTipos.setOnAction(actionEvent -> {
            if(Objects.equals(comboBoxTipos.getValue(), "T1")){
                comboBoxEditar.getItems().clear();
                comboBoxEditar.getItems().addAll(
                        "Titulo",
                        "Numero do Aluno",
                        "Ramo",
                        "Entidade"
                );

            }else if(Objects.equals(comboBoxTipos.getValue(), "T2")){
                comboBoxEditar.getItems().clear();
                comboBoxEditar.getItems().addAll(
                        "Titulo",
                        "Numero do Aluno",
                        "Ramo",
                        "Email"
                );


            }else if(Objects.equals(comboBoxTipos.getValue(), "T3")){
                comboBoxEditar.getItems().clear();
                comboBoxEditar.getItems().addAll(
                        "Titulo",
                        "Numero do Aluno"
                );

            }
        });

        btnEditar.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);

                if(Objects.equals(fsm.consultaProposta(dEditar.getValue()), "Proposta não encontrado\n")){
                    a.setContentText("Proposta não encontrado!");
                }else {
                    if(Objects.equals(comboBoxEditar.getValue(), "Titulo")){
                            fsm.editTituloPropostas(dEditar.getValue() , valorEditar.getText());
                            a.setContentText("Proposta editada com sucesso");
                        }
                    else if(Objects.equals(comboBoxEditar.getValue(), "Numero do Aluno")){
                        fsm.editNumEstudante(dEditar.getValue(), Long.parseLong(valorEditar.getText()));
                        a.setContentText("Proposta editada com sucesso");

                    }
                    else if(Objects.equals(comboBoxEditar.getValue(), "Email")){
                        if(Propostas.getDummyProposta(dEditar.getValue()) instanceof T2) {
                            fsm.editProfEmailPropostas(dEditar.getValue() , valorEditar.getText());
                            a.setContentText("Proposta editada com sucesso");
                        } else
                            a.setContentText("Proposta de outro tipo, não editada");

                    }
                    else if(Objects.equals(comboBoxEditar.getValue(), "Entidade")){
                        if(Propostas.getDummyProposta(dEditar.getValue()) instanceof T1) {
                            fsm.editEntidadePropostas(dEditar.getValue(), valorEditar.getText());
                            a.setContentText("Proposta editada com sucesso");
                        }
                        else
                            a.setContentText("Proposta de outro tipo, não editada");
                    }
                    else if(Objects.equals(comboBoxEditar.getValue(), "Ramo")){
                        if(fsm.checkRamo(valorEditar.getText())){
                            if(Propostas.getDummyProposta(dEditar.getValue()) instanceof T1 ||
                                    Propostas.getDummyProposta(dEditar.getValue()) instanceof T2) {
                                a.setContentText("Proposta editada com sucesso");
                                fsm.editRamoPropostas(dEditar.getValue() , valorEditar.getText());
                            }
                            else
                                a.setContentText("Proposta de outro tipo, não editada");
                        }else
                            a.setContentText("Erro no ramo");
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
            if( inserirPropostas(file.getAbsolutePath())== 0){
                a.setContentText("Dados carregados com sucesso!");
            }else{
                a.setContentText("Erro a carregar o ficheiro!");

            }
            a.show();
        });

        btnRemover.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);

                if(fsm.removeProposta(dRemover.getValue())){
                    a.setContentText("proposta removido com sucesso!");
                }else{
                    a.setContentText("Erro a remover o proposta!");

                }
                a.show();
        });

        voltarFase.setOnAction(actionEvent -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.close();
        });
    }


    /** Insere as propostas presentes num ficheiro
     * @param nomeFich Nome do ficheiro onde estão presentes as propostas
     * @return Valor que indica como a operação correu
     */

    private int inserirPropostas(String nomeFich){
        String delimiter = ",";
        try{
            File ficheiro = new File(nomeFich);
            FileReader fr = new FileReader(ficheiro);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String[] tempArr;
            while((line = br.readLine()) != null){
                tempArr = line.split(delimiter);

                if(tempArr[0].equals("T1")){
                    fsm.adicionaProposta(new T1(tempArr[1], tempArr[2], tempArr[3], tempArr[4]));
                }

                if(tempArr[0].equals("T2")){
                    if(fsm.consultaDocente(tempArr[4]).compareToIgnoreCase("Docente não encontrado\n") == 0){
                        System.out.println("Docente não encontrado proposta não adicionada.");
                        continue;
                    }
                    if(tempArr.length == 6)
                        fsm.adicionaProposta(new T2(tempArr[1], tempArr[2], tempArr[3], tempArr[4], Long.parseLong(tempArr[5])));
                    else
                        fsm.adicionaProposta(new T2(tempArr[1], tempArr[2], tempArr[3], tempArr[4]));
                }

                if(tempArr[0].equals("T3")){
                    if(fsm.consulta(Long.parseLong(tempArr[3])).compareToIgnoreCase("Aluno não encontrado\n") == 0){
                        System.out.println("Numero de aluno não encontrado: " + tempArr[3]);
                        break;
                    }else
                        fsm.adicionaProposta(new T3(tempArr[1], tempArr[2], Long.parseLong(tempArr[3])));
                }

            }
            br.close();

        }catch (IOException ioe){
            ioe.printStackTrace();
            return -1;
        }
        return 0;
    }


}
