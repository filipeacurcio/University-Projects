import javafx.application.Application;
import pt.isec.pa.apoio_poe.model.fsm.FaseContext;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
import pt.isec.pa.apoio_poe.ui.gui.MainJFX;
import pt.isec.pa.apoio_poe.ui.text.UIText;
/** Representa a classe principal.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class Main {


    /** Primeira função a ser chamada quando o programa é iniciado
     * @param args Argumentos da linha de comnandos
     */
    public static void main(String[] args) {
        /*FaseContextManager fsm = new FaseContextManager(new FaseContext());
        UIText ui = new UIText(fsm);
        ui.start();*/

        Application.launch(MainJFX.class,args);
    }
}
