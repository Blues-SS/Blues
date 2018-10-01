package sample;

import javafx.event.EventHandler;
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

public class SprintCrud {
    @FXML
    private FlowPane toDo;
    @FXML
    private FlowPane doing;
    @FXML
    private FlowPane done;
    private int i = 0;
    private double xOffset = 0;
    private double yOffset = 0;

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

        toDo.getChildren().add(novaTela);
        ComboBox teste = (ComboBox) novaTela.lookup("#histPts");
        teste.getItems().addAll(novaTela.getId());
        i++;
    }
}