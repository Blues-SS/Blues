package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class SprintController {

    //botton
    @FXML private Button adicionarBT;
    @FXML private Button modificarBT;

    //label
    @FXML private TextField nomeLB;
    @FXML private TextField StatusDB;

    //tabela
    @FXML private TableView<Sprint> tableSprint;
    @FXML private TableColumn tableSprintNome;
    @FXML private TableColumn tableSprintStatus;
    ObservableList<Sprint> sprints;

    private int pocisao;

    @FXML
    public void novo(MouseEvent event) throws IOException{
        Sprint sprint = new Sprint();
        nomeLB.setText("TESTE");
        StatusDB.setText("TESTE");
        sprints.add(sprint);
    }

    private void handleButtonAction(ActionEvent event){
        System.out.println("Hello World");
    }

    public void handleVoltar(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
