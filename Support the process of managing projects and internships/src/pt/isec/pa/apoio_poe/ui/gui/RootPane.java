package pt.isec.pa.apoio_poe.ui.gui;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
import pt.isec.pa.apoio_poe.model.fsm.ImageManager;

/** Representa a classe base da interface gráfica.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class RootPane extends BorderPane {
    FaseContextManager fsm;
    /** Cria uma classe base.
     * @param fsm Dados da máquina de estado
     */
    public RootPane(FaseContextManager fsm) {
        this.fsm = fsm;
        createViews();
    }

    /** Cria as vistas.
     */
    private void createViews() {
        StackPane stackPane = new StackPane(
                new FaseUmUI(fsm),
                new FaseDoisUI(fsm), new FaseTresUI(fsm),
                new FaseQuatroUI(fsm), new FaseCincoUI(fsm));
        Image aux = ImageManager.getImage("fundo.jpeg");

        if(aux != null){

            stackPane.setBackground(new Background(new BackgroundImage(
                    aux,
                    BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(1,1,true,true,true,true)
            )));

        }

        this.setCenter(stackPane);

        this.setTop(new TopToolBar(fsm));

    }


}
