package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import sample.utils.Conexao;
import sample.utils.HistoriaDAO;
import sample.utils.SprintDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

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
//    @FXML
//    private TextField sTF;
//    @FXML
//    private TextField statusTF;


    private int i = 0;
    private double xOffset = 0;
    private double yOffset = 0;

    //    private HistoriaDAO historiaDAO;
    private static SprintDAO sprintDAO;
    private static Conexao conexao = new Conexao();
    ObservableList<Historias> historia;

    public void initialize() {
        historia = FXCollections.observableArrayList();
        //pane.setItems(getSprints());
    }

    ;

    public void handleSair(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void handleNovaSprint(MouseEvent event) throws IOException {
        Parent telaNS = FXMLLoader.load(getClass().getResource("SprintCrud.fxml"));
        Scene sceneNS = new Scene(telaNS);
        Stage stageNS = (Stage) ((Node) event.getSource()).getScene().getWindow();

        this.sprintDAO = new SprintDAO();

        TextField tituloSprint = (TextField) telaNS.lookup("#tituloSprint");
        DatePicker dataInicio = (DatePicker) telaNS.lookup("#dataInicio");
        DatePicker dataFim = (DatePicker) telaNS.lookup("#dataFim");

        tituloSprint.textProperty().addListener((observable, oldValue, newValue) -> {
            java.sql.Date dateInicio = dataInicio.getValue() != null ? java.sql.Date.valueOf(dataInicio.getValue()): null;
            java.sql.Date dateFim = dataFim.getValue() != null ? java.sql.Date.valueOf(dataFim.getValue()): null;
            atualizaDadosSPrint(newValue,
                    dateInicio,
                    dateFim);
        });

        dataInicio.valueProperty().addListener((observable, oldValue, newValue) -> {
            String text = tituloSprint.getText();
            java.sql.Date dateFim = dataFim.getValue() != null ? java.sql.Date.valueOf(dataFim.getValue()): null;
            atualizaDadosSPrint(
                    text,
                    java.sql.Date.valueOf(newValue),
                    dateFim);
        });

        dataFim.valueProperty().addListener((observable, oldValue, newValue) -> {
            String text = tituloSprint.getText();
            java.sql.Date dateInicio = dataInicio.getValue() != null ? java.sql.Date.valueOf(dataInicio.getValue()): null;
            atualizaDadosSPrint(
                    text,
                    dateInicio,
                    java.sql.Date.valueOf(newValue));
        });

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

    public void handleBacklog(MouseEvent event) throws IOException {
        Parent telaB = FXMLLoader.load(getClass().getResource("Backlog.fxml"));
        Scene sceneB = new Scene(telaB);
        Stage stageB = (Stage) ((Node) event.getSource()).getScene().getWindow();

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
        Stage stageNS = (Stage) ((Node) event.getSource()).getScene().getWindow();

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
        AnchorPane novaHistoria = FXMLLoader.load(getClass().getResource("Historia.fxml"));
        novaHistoria.setId("Hist" + i);

        HistoriaDAO historiaDAO = new HistoriaDAO();
        historiaDAO.setIdHistoria((long) i);
        historiaDAO.setStatus("TODO");
        this.sprintDAO.getHistorias().add(historiaDAO);

        // Para mover as histórias para outros pane (TO DO, DOING, DONE)
        novaHistoria.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
            }
        });
        novaHistoria.setOnMouseReleased(new EventHandler<MouseEvent>() {
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
        });

        ComboBox histPts = (ComboBox) novaHistoria.lookup("#histPts");
        histPts.getItems().addAll(1, 2, 3, 5, 8, 13);
        ComboBox valueBus = (ComboBox) novaHistoria.lookup("#valueBus");
        valueBus.getItems().addAll(1000, 3000, 5000);
        TextField tituloHist = (TextField) novaHistoria.lookup("#tituloHist");
        tituloHist.textProperty().addListener((observable, oldValue, newValue) -> {
            Object business = valueBus.getSelectionModel().getSelectedItem();
            Object pts = histPts.getSelectionModel().getSelectedItem();
            atualizaDadosHistoria(novaHistoria,
                    newValue,
                    business != null ? Integer.valueOf(business.toString()) : null,
                    pts != null ? Integer.valueOf(pts.toString()) : null);
        });
        valueBus.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String text = tituloHist.getText();
            Object pts = histPts.getSelectionModel().getSelectedItem();
            atualizaDadosHistoria(novaHistoria,
                    text,
                    newValue != null ? Integer.valueOf(newValue.toString()) : null,
                    pts != null ? Integer.valueOf(pts.toString()) : null);
        });
        histPts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String text = tituloHist.getText();
            Object business = valueBus.getSelectionModel().getSelectedItem();
            atualizaDadosHistoria(novaHistoria,
                    text,
                    business != null ? Integer.valueOf(business.toString()) : null,
                    newValue != null ? Integer.valueOf(newValue.toString()) : null);
        });
        Button histBtn = (Button) novaHistoria.lookup("#histBtn");

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
            if (histPts.getSelectionModel().isEmpty() == false)
                valorPts.setText(histPts.getSelectionModel().getSelectedItem().toString());
            Label valorBus = (Label) infoTela.lookup("#valorBus");
            if (valueBus.getSelectionModel().isEmpty() == false)
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

        toDo.getChildren().add(novaHistoria);
        i++;
        System.out.println(i);
    }

    private void atualizaStatusHistoria(AnchorPane anchorPane, String status) {
        AtomicReference<HistoriaDAO> historiaDAO = new AtomicReference<>(new HistoriaDAO());
        AtomicReference<Integer> index = new AtomicReference<>();
        String id = anchorPane.getId();
        Long idLong = Long.valueOf(id.replaceAll("\\D", ""));
        sprintDAO.getHistorias().forEach(historia -> {
            if (historia.getIdHistoria() == idLong) {
                index.set(sprintDAO.getHistorias().indexOf(historia));
                historiaDAO.set(historia);
            }
        });
        historiaDAO.get().setStatus(status);
    }

    public void salvarNovaSprint(MouseEvent event) throws SQLException {
            sprintDAO.create(conexao, sprintDAO);
    }

    public void atualizaDadosHistoria(AnchorPane novaHistoria, String text, Integer business, Integer pts) {
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
        sprintDAO.getHistorias().set(index.get(), historiaDAO);
    }

    public void atualizaDadosSPrint(String titulo, Date dtInicio, Date dtFim) {
        sprintDAO.setDsSprint(titulo);
        if (sprintDAO.getStatus() == null) {
            sprintDAO.setStatus("Em Andamento");
        }
        sprintDAO.setDtInicio(dtInicio != null ? (java.sql.Date) dtInicio : null);
        sprintDAO.setDtFim(dtFim != null ? (java.sql.Date) dtFim : null);
    }
}