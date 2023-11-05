package pt.isec.pa.apoio_poe.ui.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
/** Representa a classe para mostrar a fase atual.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class TopToolBar extends HBox {
    FaseContextManager fsm;
    Label titulo;

    /** Cria um classe para mostra a fase atual.
     * @param fsm Dados da máquina de estado.
     */
    public TopToolBar(FaseContextManager fsm){
        this.fsm = fsm;
        titulo = new Label();
        registerHandlers();
        createViews();
        update();
    }
    /** Regista os handlers para os controlos.
     */
    private void registerHandlers() {
        fsm.addPropertyChangeListener(FaseContextManager.PROP_STATE, evt -> update());
    }
    /** Atualiza os valores.
     */
    private void update() {
        titulo.setText(fsm.getState().toString());
    }

    /** Cria as vistas.
     */
    private void createViews() {
        this.setAlignment(Pos.CENTER);
        this.setBackground(new Background(new BackgroundFill(Color.rgb(131,131,131),null,null)));
        this.getChildren().add(titulo);
    }
}
