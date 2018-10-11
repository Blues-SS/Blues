package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.utils.Conexao;
import sample.utils.HistoriaDAO;
import sample.utils.SprintDAO;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;


public class BacklogController {

    @FXML
    private Button cancelarBT;

    int i = 0;
    private static SprintDAO sprintDAO;
    private static Conexao conexao = new Conexao();




    @FXML
    public void handleNovaHistoria(MouseEvent event) throws IOException {
        AnchorPane novaHistoria = FXMLLoader.load(getClass().getResource("Historia.fxml"));
        novaHistoria.setId("Hist" + i);

        HistoriaDAO historiaDAO = new HistoriaDAO();
        historiaDAO.setIdHistoria((long) i);
        historiaDAO.setStatus("TODO");
        this.sprintDAO.getHistorias().add(historiaDAO);

        /*novaHistoria.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getScreenX() >= 754 && event.getScreenX() < 1168) {
                    if (!doing.getChildren().contains(novaHistoria)) { // se já não estiver na pane DOING, adiciona
                        doing.getChildren().add(novaHistoria);
                        atualizaStatusHistoria(novaHistoria, "DOING");
                    }
                } else if (event.getScreenX() >= 1168) {
                    if (!done.getChildren().contains(novaHistoria)) { // se já não estiver na pane DONE, adiciona
                        done.getChildren().add(novaHistoria);
                        atualizaStatusHistoria(novaHistoria, "DONE");
                    }
                } else if (event.getScreenX() < 752 && event.getScreenX() > 0) {
                    if (!toDo.getChildren().contains(novaHistoria)) { // se já não estiver na pane TO DO, adiciona
                        toDo.getChildren().add(novaHistoria);
                        atualizaStatusHistoria(novaHistoria, "TODO");
                    }
                }
            }
        });*/
    }


    public void salvarHist(MouseEvent event) throws SQLException {
        //gravarNovaHistoria //atualizarNovaHist
    }

    @FXML
    public void cancelar(InputEvent e) {
        //fechar tela somente
        final Node source = (Node) e.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}