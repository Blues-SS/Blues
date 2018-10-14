package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    @FXML
    private AnchorPane mainSprint;
    @FXML
    private AnchorPane main;

    private double xOffset = 0;
    private double yOffset = 0;
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
        AnchorPane novaHistoria = FXMLLoader.load(getClass().getResource("historiaBacklog.fxml"));
        novaHistoria.setId("Hist" + i);

        //-----------------------
        this.sprintDAO = new SprintDAO();



        //-----------------------
        HistoriaDAO historiaDAO = new HistoriaDAO();


        historiaDAO.setIdHistoria((long) i);
        historiaDAO.setStatus("TODO");
        this.sprintDAO.getHistorias().add(historiaDAO);

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

        javafx.scene.control.Button histBtn = (javafx.scene.control.Button) novaHistoria.lookup("#histBtn");

        //abrir informaçoes
        histBtn.setOnAction(actionEvent -> {
            AnchorPane infoTela = null;
            try {
                infoTela = FXMLLoader.load(getClass().getResource("newhistBacklog.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            ComboBox pontosCB = (ComboBox) infoTela.lookup("#pontosCB");
            pontosCB.getItems().addAll(1, 2, 3, 5, 8, 13);
            ComboBox valueCB = (ComboBox) infoTela.lookup("#valueCB");
            valueCB.getItems().addAll(1000, 3000, 5000);
            javafx.scene.control.TextField tituloTF = (javafx.scene.control.TextField) infoTela.lookup("#tituloTF");

            tituloTF.textProperty().addListener((observable, oldValue, newValue) -> {
                Object business = valueCB.getSelectionModel().getSelectedItem();
                Object pts = pontosCB.getSelectionModel().getSelectedItem();
                atualizaDadosHistoria(novaHistoria,
                        newValue,
                        business != null ? Integer.valueOf(business.toString()) : null,
                        pts != null ? Integer.valueOf(pts.toString()) : null,
                        null);
            });
            valueCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                String text = tituloTF.getText();
                Object pts = pontosCB.getSelectionModel().getSelectedItem();
                atualizaDadosHistoria(novaHistoria,
                        text,
                        newValue != null ? Integer.valueOf(newValue.toString()) : null,
                        pts != null ? Integer.valueOf(pts.toString()) : null,
                        null);
            });
            pontosCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                String text = tituloTF.getText();
                Object business = valueCB.getSelectionModel().getSelectedItem();
                atualizaDadosHistoria(novaHistoria,
                        text,
                        business != null ? Integer.valueOf(business.toString()) : null,
                        newValue != null ? Integer.valueOf(newValue.toString()) : null,
                        null);
            });



            javafx.scene.control.TextArea descrHist = (javafx.scene.control.TextArea) infoTela.lookup("#descrHist");
            javafx.scene.control.Button infoSalvar = (javafx.scene.control.Button) infoTela.lookup("#infoSalvar");
            infoSalvar.setOnAction(actionEvent2 -> {
                String text = tituloHist.getText();
                Object business = valueBus.getSelectionModel().getSelectedItem();
                Object pts = histPts.getSelectionModel().getSelectedItem();
                atualizaDadosHistoria(novaHistoria,
                        text,
                        business != null ? Integer.valueOf(business.toString()) : null,
                        pts != null ? Integer.valueOf(pts.toString()) : null,
                        descrHist.getText());
                mainSprint.getChildren().remove(mainSprint.lookup("#newhistBacklog"));
                mainSprint.setDisable(true);
                mainSprint.setVisible(false);
            });

            mainSprint.setStyle("-fx-background-color: rgba(128, 128, 128, 0.4)");
            mainSprint.setDisable(false);
            mainSprint.setVisible(true);
            mainSprint.getChildren().addAll(infoTela);

            javafx.scene.control.Button infoCancel = (javafx.scene.control.Button) infoTela.lookup("#infoCancel");
            infoCancel.setOnAction(actionEvent3 -> {
                mainSprint.getChildren().remove(mainSprint.lookup("#newhistBacklog"));
                mainSprint.setDisable(true);
                mainSprint.setVisible(false);
            });
        });

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
    public void voltar(MouseEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

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

    public void cancelar(InputEvent e) {
        //fechar tela somente
        final Node source = (Node) e.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}