package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import sample.utils.Conexao;
import sample.utils.HistoriaDAO;
import sample.utils.SprintDAO;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;


public class BacklogController {

    @FXML
    private Button cancelarBT;


    int i = 0;
    private static SprintDAO sprintDAO;
    private static Conexao conexao = new Conexao();

    @FXML
    private FlowPane toDo;

    ObservableList<Historias> historia;

    public void initialize() {
        historia = FXCollections.observableArrayList();
    }

    public void handleNovaHistoria(MouseEvent event) throws IOException {
        AnchorPane novaHistoria = FXMLLoader.load(getClass().getResource("Historia.fxml"));
        novaHistoria.setId("Hist" + i);

        //-----------------------
        this.sprintDAO = new SprintDAO();



        //-----------------------
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

        ComboBox histPts = (ComboBox) novaHistoria.lookup("#histPts");
        histPts.getItems().addAll(1, 2, 3, 5, 8, 13);
        ComboBox valueBus = (ComboBox) novaHistoria.lookup("#valueBus");
        valueBus.getItems().addAll(1000, 3000, 5000);
        javafx.scene.control.TextField tituloHist = (javafx.scene.control.TextField) novaHistoria.lookup("#tituloHist");
        tituloHist.textProperty().addListener((observable, oldValue, newValue) -> {
            Object business = valueBus.getSelectionModel().getSelectedItem();
            Object pts = histPts.getSelectionModel().getSelectedItem();
            atualizaDadosHistoria(novaHistoria,
                    newValue,
                    business != null ? Integer.valueOf(business.toString()) : null,
                    pts != null ? Integer.valueOf(pts.toString()) : null,
                    null);
        });
        valueBus.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String text = tituloHist.getText();
            Object pts = histPts.getSelectionModel().getSelectedItem();
            atualizaDadosHistoria(novaHistoria,
                    text,
                    newValue != null ? Integer.valueOf(newValue.toString()) : null,
                    pts != null ? Integer.valueOf(pts.toString()) : null,
                    null);
        });
        histPts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String text = tituloHist.getText();
            Object business = valueBus.getSelectionModel().getSelectedItem();
            atualizaDadosHistoria(novaHistoria,
                    text,
                    business != null ? Integer.valueOf(business.toString()) : null,
                    newValue != null ? Integer.valueOf(newValue.toString()) : null,
                    null);
        });
        javafx.scene.control.Button histBtn = (javafx.scene.control.Button) novaHistoria.lookup("#histBtn");
        toDo.getChildren().add(novaHistoria);
        i++;
    }
    public void atualizaDadosHistoria(AnchorPane novaHistoria, String text, Integer business, Integer pts, String descr) {
        HistoriaDAO historiaDAO = new HistoriaDAO();
        AtomicReference<Integer> index = new AtomicReference<>();
        String id = novaHistoria.getId();
        Long idLong = Long.valueOf(id.replaceAll("\\D", ""));
        sprintDAO.getHistorias().forEach(historia -> {
            if (historia.getIdHistoria() == idLong) {
                historiaDAO.setIdHistoria(historia.getIdHistoria());
                historiaDAO.setStatus(historia.getStatus());
                historia.setDescricao(historia.getDescricao());
                index.set(sprintDAO.getHistorias().indexOf(historia));
            }
        });
        historiaDAO.setNome(text);
        historiaDAO.setValueBusiness(business);
        historiaDAO.setPontos(pts);
        if(descr != null) {
            historiaDAO.setDescricao(descr);
        }
        sprintDAO.getHistorias().set(index.get(), historiaDAO);
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