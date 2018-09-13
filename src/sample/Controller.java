package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import javafx.scene.control.ComboBox;

import java.io.IOException;

public class Controller  {
    @FXML
    private FlowPane novaHist;
    private int i = 0;

    public void handleSair(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void handleNovaSprint(MouseEvent event) throws IOException {
        Parent telaNS = FXMLLoader.load(getClass().getResource("novaSprint.fxml"));
        Scene sceneNS = new Scene(telaNS);
        Stage stageNS = (Stage)((Node)event.getSource()).getScene().getWindow();
        stageNS.setScene(sceneNS);
        stageNS.show();
    }

    public void handleVoltar(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void handleSprint(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Sprints.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void handleNovaHistoria(MouseEvent event) throws IOException {
        AnchorPane novaTela = FXMLLoader.load(getClass().getResource("novaHistoria.fxml"));
        novaTela.setId("Hist" + i);
        novaHist.getChildren().add(novaTela);
        ComboBox teste = (ComboBox) novaTela.lookup("#histPts");
        teste.getItems().addAll(novaTela.getId());
        i++;
    }
}