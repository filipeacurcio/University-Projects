package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.fsm.FaseContext;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;

/** Representa a classe que inicia a interface gráfica.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

    public class MainJFX extends Application {

        FaseContextManager fsm;

        /** Cria a classe inicial e a máquina de estados
         */
        public MainJFX(){
            this.fsm = new FaseContextManager(new FaseContext());
        }

        /** Representa o inicio da interface gráfica
         * @param stage Stage onde a janela será criada
         */
        @Override
        public void start(Stage stage) throws Exception {
            fsm.start();
            configureStage(stage, -1, -1);
            configureStage1(new Stage(), stage.getX() + stage.getWidth(), stage.getY());


        }
        /** Cria a janela para a gestão de todas as informações
            * @param stage Stage onde a janela será criada
            * @param x Valor da posição X da janela
            * @param y Valor da posição Y da janela
         */
        public void configureStage(Stage stage, double x, double y){
            RootPane root = new RootPane(fsm);
            Scene scene = new Scene(root,600,400);
            stage.setScene(scene);
            stage.setTitle("Gestão de Projetos e Estágios");
            stage.setMinWidth(400);
            stage.show();
            if(x >= 0 && y >= 0){
                stage.setX(x);
                stage.setY(y);
            }
            stage.show();
        }

        /** Cria a janela auxiliar de informação
         * @param stage Stage onde a janela será criada
         * @param x Valor da posição X da janela
         * @param y Valor da posição Y da janela
         */
        public void configureStage1(Stage stage, double x, double y){
            MenuListAux root = new MenuListAux(fsm);
            Scene scene = new Scene(root,300,400);
            stage.setScene(scene);
            stage.setTitle("Gestão de Projetos e Estágios");
            stage.setMinWidth(400);
            stage.show();
            if(x >= 0 && y >= 0){
                stage.setX(x);
                stage.setY(y);
            }
            stage.show();
        }
    }
