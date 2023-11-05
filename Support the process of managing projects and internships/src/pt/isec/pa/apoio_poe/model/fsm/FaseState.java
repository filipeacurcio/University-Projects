package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.data.FaseData;

/** Enumeração com todos os estados possiveis da máquina de estados.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */
public enum FaseState {
    /** Fase um da máquina de estados.
     */
    FASE_UM,
    /** Fase dois da máquina de estados.
     */
    FASE_DOIS,
    /** Fase três da máquina de estados.
     */
    FASE_TRES,
    /** Fase quatro da máquina de estados.
     */
    FASE_QUATRO,
    /** Fase cinco da máquina de estados.
     */
    FASE_CINCO;

    /** Cria um estado.
     * @param context Máquina de estados.
     * @param data Dados da aplicação.
     * @return Novo estado.
     */
    public IFaseState createState(FaseContext context, FaseData data) {
        return switch (this) {
            case FASE_UM -> new FaseUm(context, data);
            case FASE_DOIS -> new FaseDois(context, data);
            case FASE_TRES -> new FaseTres(context, data);
            case FASE_QUATRO -> new FaseQuatro(context, data);
            case FASE_CINCO -> new FaseCinco(context, data);
            default -> null;
        };
    }
}
