package pt.isec.pa.apoio_poe.ui.gui;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
import pt.isec.pa.apoio_poe.model.fsm.FaseState;

/** Representa a classe listar informação de apoio.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class MenuListAux extends ListView<String> {
    FaseContextManager fsm;
    /** Cria uma classe para listar informação de apoio.
     * @param fsm Dados da máquina de estado.
     */
    public MenuListAux(FaseContextManager fsm){
        this.fsm = fsm;
        createViews();
        registerHandlers();
        update();
    }

    /** Cria as vistas.
     */
    private void createViews() {

    }

    /** Regista os handlers para os controlos.
    */
    private void registerHandlers() {
        fsm.addPropertyChangeListener(FaseContextManager.PROP_DATA, evt -> update());
        fsm.addPropertyChangeListener(FaseContextManager.PROP_STATE, evt -> updateState());

    }

    private void updateState() {
        if(fsm.getState() == FaseState.FASE_CINCO){
            Stage stage = (Stage) this.getScene().getWindow();
            stage.close();
        }else
            this.setVisible(true);

    }
    /** Atualiza os valores.
     */
    private void update() {

        this.getItems().clear();
        this.getItems().addAll(
                "Numero de alunos: " + fsm.getNAlunos(),
                "Numero de Docentes: " + fsm.getNDocentes(),
                "Numero Propostas: " + fsm.getNPropostas(),
                "Numero de Alunos com Candidaturas: " + fsm.getNCandidaturas(),
                "Numero de Alunos com Propostas Atribuidas: " + fsm.getNAlunosPropAssociados(),
                "Numero de Docentes com Propostas Atribuidas: " + fsm.getNDocentesPropAssociados()
               // " " + fsm.listaPropsSemDocente(),
               // fsm.listarAlunos()

        );
    }

}
