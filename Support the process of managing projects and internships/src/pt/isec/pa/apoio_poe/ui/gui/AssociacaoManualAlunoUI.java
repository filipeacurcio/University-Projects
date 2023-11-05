package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.data.Propostas;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
import pt.isec.pa.apoio_poe.model.fsm.ImageManager;

import java.util.Set;

/** Representa a classe de associação manual dos alunos a propostas.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */


public class AssociacaoManualAlunoUI extends BorderPane {
    FaseContextManager fsm;
    HBox menuInferior, menuSuperior;
    TextField alunoAsssociar, propostaAssociar;
    Label titulo;
    ComboBox<Long> dAluno;
    ComboBox<String> dProposta;
    Button btnAssociar, voltarFase;

    /** Cria uma classe de associação manual dos alunos a propostas.
     * @param fsm Dados da máquina de estado.
     */
    public AssociacaoManualAlunoUI(FaseContextManager fsm){
        this.fsm = fsm;
        menuInferior = new HBox(25);
        menuSuperior = new HBox(25);
        titulo = new Label();
        createViews();
        registerHandlers();
        update();

    }

    /** Cria as vistas.
     */
    private void createViews() {
        dAluno = new ComboBox<>();
        Set<Alunos> alunos = fsm.getAlunoNaoAtribuidos();
        for (Alunos aluno : alunos) {
            dAluno.getItems().add(aluno.getNumEstudante());
        }
        dProposta = new ComboBox<>();
        Set<Propostas> props = fsm.getPropNaoAtribuidos();
        for (Propostas prop : props) {
            dProposta.getItems().add(prop.getCodigo());
        }
       // this.setStyle("-fx-background-color: #F7DFA6;");
        Image aux = ImageManager.getImage("fundo.jpeg");

        if(aux != null) {

            this.setBackground(new Background(new BackgroundImage(
                    aux,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(1, 1, true, true, true, true)
            )));
        }
        alunoAsssociar = new TextField();
        alunoAsssociar.setPromptText("Aluno a associar");
        btnAssociar = new Button("Associar");
        voltarFase = new Button("Voltar a fase tres");
        propostaAssociar = new TextField();
        propostaAssociar.setPromptText("Proposta a associar");
        menuInferior.setPadding(new Insets(15, 20, 15, 30));
        menuInferior.getChildren().addAll(voltarFase);
        this.setBottom(menuInferior);

        titulo.setText("Associar Aluno");
        menuSuperior.setAlignment(Pos.CENTER);
        menuSuperior.getChildren().addAll(titulo);
        this.setTop(menuSuperior);
        VBox vBox = new VBox();
        HBox hBox = new HBox(25);
        alunoAsssociar.setMaxWidth(200);
        vBox.setPadding(new Insets(125, 30, 0, 175));
        hBox.getChildren().addAll(dProposta, btnAssociar);
        vBox.getChildren().addAll(dAluno, hBox);
        this.setCenter(vBox);
    }

    /** Regista os handlers.
     */
    private void registerHandlers() {
        fsm.addPropertyChangeListener(FaseContextManager.PROP_DATA, evt -> update());

        btnAssociar.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);
            if(!fsm.isSemAtribuicao(dAluno.getValue())){
                a.setContentText("Aluno com associação já realizada");
                a.show();
            }else{
                    if(!fsm.verificaRamoPropAluno(dAluno.getValue(), dProposta.getValue())){
                    a.setContentText("Proposta não destinada a area do aluno");
                    a.show();
                } else if (!fsm.isPropSemAtribuicao(dProposta.getValue())) {
                    a.setContentText("Proposta já associada");
                    a.show();
                }else{
                       // System.out.println(dAluno.getValue() + dProposta.getValue());
                    if(fsm.atribuicaoManual(dAluno.getValue(),dProposta.getValue())){
                        a.setContentText("Proposta associada com sucesso");
                        a.show();
                    }else{
                        a.setContentText("Erro na associação do aluno");
                        a.show();
                    }
                }
            }

        });

        voltarFase.setOnAction(actionEvent -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.close();
        });

    }
    /** Atualiza as informações.
     */
    private void update() {
        dAluno.getItems().clear();
        Set<Alunos> alunos = fsm.getAlunoNaoAtribuidos();
        for (Alunos aluno : alunos) {
            dAluno.getItems().add(aluno.getNumEstudante());
        }
        dProposta.getItems().clear();
        Set<Propostas> propostas = fsm.getPropNaoAtribuidos();
        for (Propostas prop : propostas) {
            dProposta.getItems().add(prop.getCodigo());
        }
    }
}
