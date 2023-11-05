package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.FaseData;
/** Cria uma classe para implementar a interface ICommand.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */


abstract class CommandAdapter implements ICommand{
    /** Classe que liga a interface com utilizador à lógica.
     */
    protected FaseContext data;
    /** Cria uma classe para implementar a interface ICommand.
     * @param ls Classe que liga a interface com utilizador à lógica.
     */
    protected CommandAdapter(FaseContext ls){
        this.data = ls;
    }

}
