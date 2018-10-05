package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.util.List;

public class SprintCrud {
    @FXML
    private FlowPane toDo;
    @FXML
    private FlowPane doing;
    @FXML
    private FlowPane done;
    @FXML
    private AnchorPane mainSprint;
    @FXML
    private AnchorPane main;
    private int i = 0;
    private double xOffset = 0;
    private double yOffset = 0;


    ObservableList<Historias> historia;

    public void initialize (){
        historia = FXCollections.observableArrayList();
        //pane.setItems(getSprints());
    };

    public void handleSair(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void handleNovaSprint(MouseEvent event) throws IOException {
        Parent telaNS = FXMLLoader.load(getClass().getResource("SprintCrud.fxml"));
        Scene sceneNS = new Scene(telaNS);
        Stage stageNS = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Para deixar a tela draggable
        telaNS.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        telaNS.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stageNS.setX(event.getScreenX() - xOffset);
                stageNS.setY(event.getScreenY() - yOffset);
            }
        });

        stageNS.setScene(sceneNS);
        stageNS.show();
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

    public void handleBacklog(MouseEvent event) throws IOException {
        Parent telaB = FXMLLoader.load(getClass().getResource("Backlog.fxml"));
        Scene sceneB = new Scene(telaB);
        Stage stageB = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Para deixar a tela draggable
        telaB.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        telaB.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stageB.setX(event.getScreenX() - xOffset);
                stageB.setY(event.getScreenY() - yOffset);
            }
        });

        stageB.setScene(sceneB);
        stageB.show();
    }

    public void handleSprint(MouseEvent event) throws IOException {
        Parent telaNS = FXMLLoader.load(getClass().getResource("SprintList.fxml"));
        Scene sceneNS = new Scene(telaNS);
        Stage stageNS = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Para deixar a tela draggable
        telaNS.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        telaNS.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stageNS.setX(event.getScreenX() - xOffset);
                stageNS.setY(event.getScreenY() - yOffset);
            }
        });

        stageNS.setScene(sceneNS);
        stageNS.show();
    }

    public void handleNovaHistoria(MouseEvent event) throws IOException {
        AnchorPane novaTela = FXMLLoader.load(getClass().getResource("Historia.fxml"));
        novaTela.setId("Hist" + i);

        // Para mover as histórias para outros pane (TO DO, DOING, DONE)
        novaTela.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
            }
        });
        novaTela.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getScreenX() >= 754 && event.getScreenX() < 1168) {
                    if(!doing.getChildren().contains(novaTela)) { // se já não estiver na pane DOING, adiciona
                        doing.getChildren().add(novaTela);
                    }
                }
                else if(event.getScreenX() >= 1168) {
                    if(!done.getChildren().contains(novaTela)) { // se já não estiver na pane DONE, adiciona
                        done.getChildren().add(novaTela);
                    }
                }
                else if(event.getScreenX() < 752 && event.getScreenX() > 0) {
                    if(!toDo.getChildren().contains(novaTela)) { // se já não estiver na pane TO DO, adiciona
                        toDo.getChildren().add(novaTela);
                    }
                }
            }
        });

        ComboBox histPts = (ComboBox) novaTela.lookup("#histPts");
        histPts.getItems().addAll(1,2,3,5,8,13);
        ComboBox valueBus = (ComboBox) novaTela.lookup("#valueBus");
        valueBus.getItems().addAll(1000,3000,5000);
        TextField tituloHist = (TextField) novaTela.lookup("#tituloHist");
        Button histBtn = (Button) novaTela.lookup("#histBtn");

        // PARA TELA DE INFORMAÇÕES
        histBtn.setOnAction(actionEvent -> {
            AnchorPane infoTela = null;
            try {
                infoTela = FXMLLoader.load(getClass().getResource("InfoHistoria.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Label valorTit = (Label) infoTela.lookup("#valorTit");
            valorTit.setText(tituloHist.getText());
            Label valorPts = (Label) infoTela.lookup("#valorPts");
            if(histPts.getSelectionModel().isEmpty() == false)
                valorPts.setText(histPts.getSelectionModel().getSelectedItem().toString());
            Label valorBus = (Label) infoTela.lookup("#valorBus");
            if(valueBus.getSelectionModel().isEmpty() == false)
                valorBus.setText(valueBus.getSelectionModel().getSelectedItem().toString());

            mainSprint.setStyle("-fx-background-color: rgba(128, 128, 128, 0.4)");
            mainSprint.setDisable(false);
            mainSprint.setVisible(true);
            mainSprint.getChildren().addAll(infoTela);

            Button infoCancel = (Button) infoTela.lookup("#infoCancel");
            infoCancel.setOnAction(actionEvent2 -> {
                mainSprint.getChildren().remove(mainSprint.lookup("#infoHistoria"));
                mainSprint.setDisable(true);
                mainSprint.setVisible(false);
            });
        });
        // ACABOU CÓDIGO DA TELA DE INFO

        toDo.getChildren().add(novaTela);
        i++;
    }



}