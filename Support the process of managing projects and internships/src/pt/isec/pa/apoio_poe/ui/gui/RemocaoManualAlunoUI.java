package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
import pt.isec.pa.apoio_poe.model.fsm.ImageManager;

import java.util.Set;


/** Representa a classe para remover o aluno manualmente.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class RemocaoManualAlunoUI extends BorderPane {
    FaseContextManager fsm;
    HBox menuInferior, menuSuperior;
    TextField alunoAsssociar, propostaAssociar;
    ComboBox<Long> dAluno;
    Label titulo;

    Button btnAssociar, voltarFase;
    /** Cria uma classe para remover o aluno manualmente.
     * @param fsm Dados da máquina de estado.
     */
    public RemocaoManualAlunoUI(FaseContextManager fsm){
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
        Set<Alunos> alunos = fsm.getAlunoAtribuidos();
        for (Alunos aluno : alunos) {
            dAluno.getItems().add(aluno.getNumEstudante());
        }
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
        alunoAsssociar = new TextField();
        alunoAsssociar.setPromptText("Aluno a remover");
        btnAssociar = new Button("Remover");
        voltarFase = new Button("Voltar a fase tres");
        propostaAssociar = new TextField();
        propostaAssociar.setPromptText("Proposta a remover");
        menuInferior.setPadding(new Insets(15, 20, 15, 30));
        menuInferior.getChildren().addAll(voltarFase);
        this.setBottom(menuInferior);

        titulo.setText("Remoção do Aluno");
        menuSuperior.setAlignment(Pos.CENTER);
        menuSuperior.getChildren().addAll(titulo);
        this.setTop(menuSuperior);
        VBox vBox = new VBox();
        alunoAsssociar.setMaxWidth(200);
        vBox.setPadding(new Insets(125, 30, 0, 175));
        vBox.getChildren().addAll(dAluno, btnAssociar);
        this.setCenter(vBox);
    }

    /** Regista os handlers para os controlos.
     */
    private void registerHandlers() {
        fsm.addPropertyChangeListener(FaseContextManager.PROP_DATA, evt -> update());

        btnAssociar.setOnAction(actionEvent -> {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);

            System.out.println(dAluno.getValue());
            if(fsm.isSemAtribuicao(dAluno.getValue())){
                a.setContentText("Aluno sem associação realizada");
                a.show();
            }
            if(fsm.removePropostaDoAluno(dAluno.getValue())){
                a.setContentText("Proposta removida com sucesso");
                a.show();
            }else{
                a.setContentText("Proposta não removida do aluno");
                a.show();
            }


        });

        voltarFase.setOnAction(actionEvent -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.close();
        });

    }

    /** Atualiza os valores.
     */
    private void update() {
        dAluno.getItems().clear();
        Set<Alunos> alunos = fsm.getAlunoAtribuidos();
        for (Alunos aluno : alunos) {
            dAluno.getItems().add(aluno.getNumEstudante());
        }
    }
}
