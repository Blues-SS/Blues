package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MetricasController implements  Initializable{
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private LineChart<?, ?> Burndown;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @Override
    public void initialize(URL url,ResourceBundle rb) {
        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data("1", 23));
        series.getData().add(new XYChart.Data("2", 13));
        series.getData().add(new XYChart.Data("3", 20));
        series.getData().add(new XYChart.Data("4", 25));
        series.getData().add(new XYChart.Data("5", 10));
        series.getData().add(new XYChart.Data("6", 11));

        Burndown.getData().addAll(series);
    }

    public void handleVoltar(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Para deixar a tela draggable
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        stage.setScene(scene);
        stage.show();
    }

}
