package pt.isec.pa.apoio_poe.ui.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.data.Docentes;
import pt.isec.pa.apoio_poe.model.data.Propostas;
import pt.isec.pa.apoio_poe.model.fsm.FaseContextManager;
import pt.isec.pa.apoio_poe.model.fsm.FaseState;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Representa a classe fase cinco da máquina de estados.
 * @author Filipe Acurcio
 * @author Renata Pinheiro
 * @version 1.0
 */

public class FaseCincoUI extends BorderPane {
    Button save, btnExportar;
    HBox menuInferior;
    HBox menuCentral;
    FaseContextManager fsm;
    ObservableList<PieChart.Data> pieChartDataProp, pieChartDataRamos ;
    PieChart pieChartPropostas, pieChartRamos;
    Label label;
    XYChart.Series<String, Integer> series, seriesEntidade;
    BarChart<String,Integer> bar, barEntidade;

    /** Cria uma classe fase cinco da máquina de estados.
     * @param fsm Dados da máquina de estado.
     */
    public FaseCincoUI(FaseContextManager fsm){
        this.fsm = fsm;
        menuInferior = new HBox(25);
        menuCentral = new HBox();
        label = new Label();
        btnExportar = new Button("Exportar");
        CategoryAxis xaxis= new CategoryAxis();
        NumberAxis yaxis = new NumberAxis(0,5,1);
        CategoryAxis xaxis1= new CategoryAxis();
        NumberAxis yaxis1 = new NumberAxis(0,5,1);
        bar = new BarChart(xaxis, yaxis);
        barEntidade = new BarChart(xaxis1, yaxis1);
        createViews();
        registerHandlers();
        update();
    }

    /** Cria as vistas.
     */
    private void createViews() {
        this.setStyle("-fx-background-color: #F7DFA6;");
        save = new Button("Save");
        menuInferior.setPadding(new Insets(15, 20, 15, 30));
        menuInferior.getChildren().addAll(save, btnExportar, label);

        pieChartDataProp = FXCollections.observableArrayList(
                new PieChart.Data("Propostas Atribuidas", 0),
                new PieChart.Data("Propostas não atríbuidas", 0)
        );

        pieChartPropostas = new PieChart(pieChartDataProp);

        pieChartDataRamos = FXCollections.observableArrayList(
                new PieChart.Data("DA", 0),
                new PieChart.Data("RAS", 0),
                new PieChart.Data("SI", 0)
        );

        pieChartRamos = new PieChart(pieChartDataRamos);
        series = new XYChart.Series<>();
        seriesEntidade = new XYChart.Series<>();

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        HBox hBox4 = new HBox();
        hbox1.getChildren().add(pieChartPropostas);
        hbox2.getChildren().add(pieChartRamos);
        hbox3.getChildren().add(bar);
        hBox4.getChildren().add(barEntidade);
        menuCentral.getChildren().addAll(hbox1,hbox2,hbox3, hBox4);
        this.setBottom(menuInferior);
        this.setCenter(menuCentral);
    }

    /** Regista os handlers.
     */
    private void registerHandlers() {
        fsm.addPropertyChangeListener(FaseContextManager.PROP_DATA, evt -> updateData());

        btnExportar.setOnAction(actionEvent -> {
            FileChooser fil_chooser = new FileChooser();

            fil_chooser.setTitle("Select File");

            fil_chooser.setInitialDirectory(new File("."));

            File file = fil_chooser.showSaveDialog(this.getScene().getWindow());

            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.INFORMATION);

            fsm.exportarFase4E5(file.getAbsolutePath());
            a.setContentText("Dados exportados com sucesso.");

            a.show();
        });

        fsm.addPropertyChangeListener(FaseContextManager.PROP_STATE, evt -> update());

        save.setOnAction(actionEvent -> fsm.save());
    }

    /** Atualiza as informações.
     */
    private void updateData(){
        pieChartDataProp.clear();
        pieChartDataProp = FXCollections.observableArrayList(
                new PieChart.Data("Propostas\nAtribuidas", fsm.getNAlunosPropAssociados()),
                new PieChart.Data("Propostas\nnão\natríbuidas", fsm.getAlunosSemAtribuicao())
        );
        pieChartPropostas.getData().clear();
        pieChartPropostas.getData().addAll(pieChartDataProp);

        pieChartDataRamos.clear();
        pieChartDataRamos = FXCollections.observableArrayList(
                new PieChart.Data("DA", fsm.getNDAProps()),
                new PieChart.Data("RAS", fsm.getNRASProps()),
                new PieChart.Data("SI", fsm.getNSIProps())
        );
        pieChartRamos.getData().clear();
        pieChartRamos.getData().addAll(pieChartDataRamos);

        int count = 0;
        Map<Docentes, List<Propostas>> docentes = fsm.getDocentesOrdProp();
        for (Map.Entry<Docentes, List<Propostas>> entry : docentes.entrySet()) {
            if(count > 4)
                break;
            Docentes docentes1 = entry.getKey();
            series.getData().add(new XYChart.Data<>(docentes1.getEmail(), entry.getValue().size()));
            count++;
        }
        bar.getData().clear();
        bar.getData().add(series);

        count = 0;
        LinkedHashMap<String, Integer> entidade = fsm.getEntidadeTop();
        for (Map.Entry<String , Integer> entry : entidade.entrySet()) {
            if(count > 4)
                break;
            if(entry.getValue() != 0)
            seriesEntidade.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            count++;
        }
        barEntidade.getData().clear();
        barEntidade.getData().add(seriesEntidade);



    }

    /** Atualiza as informações.
     */
    private void update() {
        this.setVisible(fsm != null && fsm.getState() == FaseState.FASE_CINCO);
        if(fsm.getState() == FaseState.FASE_CINCO){
            Stage stage = (Stage) this.getScene().getWindow();
            stage.setWidth(850);
        }
    }


}
