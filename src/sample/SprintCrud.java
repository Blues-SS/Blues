package sample;

import javafx.application.Platform;
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
import java.time.LocalDate;
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
    @FXML
    private TextField tituloSprint;
    @FXML
    private DatePicker dataInicio;
    @FXML
    private DatePicker dataFim;

    private int i = 0;
    private double xOffset = 0;
    private double yOffset = 0;

    private Integer idSprintParam;

    public void setIdSprintParam(Integer idSprintParam) {
        this.idSprintParam = idSprintParam;
    }

    //    private HistoriaDAO historiaDAO;
    private SprintDAO sprintDAO = new SprintDAO();
    private static Conexao conexao = new Conexao();
    ObservableList<Historias> historia;

    @FXML private void initialize() {
        historia = FXCollections.observableArrayList();
        Platform.runLater(() -> {

            if (this.idSprintParam == null) {
                System.out.println("DEU RUIM");
            } else {
                try {
                    sprintDAO = sprintDAO.findOne(conexao, idSprintParam);
                    LocalDate localDateInicio = sprintDAO.getDtInicio().toLocalDate();
                    LocalDate localDateFim = sprintDAO.getDtFim().toLocalDate();
                    String dsSprint = sprintDAO.getDsSprint();
                    this.tituloSprint.textProperty().setValue(dsSprint);
                    this.dataInicio.setValue(localDateInicio);
                    this.dataFim.setValue(localDateFim);
                    this.removerTodasTarefas();
                    sprintDAO.getHistorias().forEach(historiaDAO -> {
                        try {
                            novaHistoria(historiaDAO);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void handleSair(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void handleNovaSprint(MouseEvent event) throws IOException {
        Parent telaNS = FXMLLoader.load(getClass().getResource("SprintCrud.fxml"));
        Scene sceneNS = new Scene(telaNS);
        Stage stageNS = (Stage) ((Node) event.getSource()).getScene().getWindow();

        TextField tituloSprint = (TextField) telaNS.lookup("#tituloSprint");
        DatePicker dataInicio = (DatePicker) telaNS.lookup("#dataInicio");
        DatePicker dataFim = (DatePicker) telaNS.lookup("#dataFim");

        tituloSprint.textProperty().addListener((observable, oldValue, newValue) -> {
            java.sql.Date dateInicio = dataInicio.getValue() != null ? java.sql.Date.valueOf(dataInicio.getValue()) : null;
            java.sql.Date dateFim = dataFim.getValue() != null ? java.sql.Date.valueOf(dataFim.getValue()) : null;
            atualizaDadosSPrint(newValue,
                    dateInicio,
                    dateFim);
        });

        dataInicio.valueProperty().addListener((observable, oldValue, newValue) -> {
            String text = tituloSprint.getText();
            java.sql.Date dateFim = dataFim.getValue() != null ? java.sql.Date.valueOf(dataFim.getValue()) : null;
            atualizaDadosSPrint(
                    text,
                    java.sql.Date.valueOf(newValue),
                    dateFim);
        });

        dataFim.valueProperty().addListener((observable, oldValue, newValue) -> {
            String text = tituloSprint.getText();
            java.sql.Date dateInicio = dataInicio.getValue() != null ? java.sql.Date.valueOf(dataInicio.getValue()) : null;
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
        novaHistoria(null);
    }

    public void novaHistoria(HistoriaDAO dao) throws IOException {
        if (dao == null) {
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
            changeStatusHistoria(novaHistoria);

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
            Button histBtn = (Button) novaHistoria.lookup("#histBtn");

            // PARA TELA DE INFORMAÇÕES
            historiaButtonOnAction(novaHistoria, histPts, valueBus, tituloHist, histBtn);
            // ACABOU CÓDIGO DA TELA DE INFO

            toDo.getChildren().add(novaHistoria);
            i++;
        } else {
            AnchorPane novaHistoria = FXMLLoader.load(getClass().getResource("Historia.fxml"));
            novaHistoria.setId("Hist" + dao.getIdHistoria());
            novaHistoria.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                }
            });
            changeStatusHistoria(novaHistoria);

            ComboBox histPts = (ComboBox) novaHistoria.lookup("#histPts");
            histPts.getItems().addAll(1, 2, 3, 5, 8, 13);
            histPts.getSelectionModel().select(dao.getPontos());
            ComboBox valueBus = (ComboBox) novaHistoria.lookup("#valueBus");
            valueBus.getItems().addAll(1000, 3000, 5000);
            valueBus.getSelectionModel().select(dao.getValueBusiness());
            TextField tituloHist = (TextField) novaHistoria.lookup("#tituloHist");
            tituloHist.textProperty().setValue(dao.getNome());
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
            Button histBtn = (Button) novaHistoria.lookup("#histBtn");

            // PARA TELA DE INFORMAÇÕES
            historiaButtonOnAction(novaHistoria, histPts, valueBus, tituloHist, histBtn);
            // ACABOU CÓDIGO DA TELA DE INFO

            switch (dao.getStatus()) {
                case "TODO":
                    toDo.getChildren().add(novaHistoria);
                    break;
                case "DOING":
                    doing.getChildren().add(novaHistoria);
                    break;
                case "DONE":
                    done.getChildren().add(novaHistoria);
                    break;
            }
            i++;
        }
    }

    private void historiaButtonOnAction(AnchorPane novaHistoria, ComboBox histPts, ComboBox valueBus, TextField tituloHist, Button histBtn) {
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
            if (!histPts.getSelectionModel().isEmpty())
                valorPts.setText(histPts.getSelectionModel().getSelectedItem().toString());
            Label valorBus = (Label) infoTela.lookup("#valorBus");
            if (!valueBus.getSelectionModel().isEmpty())
                valorBus.setText(valueBus.getSelectionModel().getSelectedItem().toString());

            TextArea descrHist = (TextArea) infoTela.lookup("#descrHist");
            Button infoSalvar = (Button) infoTela.lookup("#infoSalvar");

            infoSalvar.setOnAction(actionEvent2 -> {
                String text = tituloHist.getText();
                Object business = valueBus.getSelectionModel().getSelectedItem();
                Object pts = histPts.getSelectionModel().getSelectedItem();
                atualizaDadosHistoria(novaHistoria,
                        text,
                        business != null ? Integer.valueOf(business.toString()) : null,
                        pts != null ? Integer.valueOf(pts.toString()) : null,
                        descrHist.getText());
                mainSprint.getChildren().remove(mainSprint.lookup("#infoHistoria"));
                mainSprint.setDisable(true);
                mainSprint.setVisible(false);
            });

            mainSprint.setStyle("-fx-background-color: rgba(128, 128, 128, 0.4)");
            mainSprint.setDisable(false);
            mainSprint.setVisible(true);
            mainSprint.getChildren().addAll(infoTela);

            Button infoCancel = (Button) infoTela.lookup("#infoCancel");
            infoCancel.setOnAction(actionEvent3 -> {
                mainSprint.getChildren().remove(mainSprint.lookup("#infoHistoria"));
                mainSprint.setDisable(true);
                mainSprint.setVisible(false);
            });
        });
    }

    private void removerTodasTarefas() {
        i = 0;
        toDo.getChildren().setAll();
        doing.getChildren().setAll();
        done.getChildren().setAll();
    }

    private void changeStatusHistoria(AnchorPane novaHistoria) {
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
    }

    private void atualizaStatusHistoria(AnchorPane anchorPane, String status) {
        AtomicReference<HistoriaDAO> historiaDAO = new AtomicReference<>(new HistoriaDAO());
        AtomicReference<Integer> index = new AtomicReference<>();
        String id = anchorPane.getId();
        Long idLong = Long.valueOf(id.replaceAll("\\D", ""));
        sprintDAO.getHistorias().forEach(historia -> {
            if (historia.getIdHistoria().equals(idLong)) {
                index.set(sprintDAO.getHistorias().indexOf(historia));
                historiaDAO.set(historia);
            }
        });
        historiaDAO.get().setStatus(status);
    }

    public void salvarNovaSprint(MouseEvent event) throws Exception {
        if (sprintDAO.getIdSprint() == null) {
            sprintDAO = sprintDAO.create(conexao, sprintDAO);
            this.removerTodasTarefas();
            sprintDAO.getHistorias().forEach(historiaDAO -> {
                try {
                    novaHistoria(historiaDAO);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            sprintDAO.update(conexao, sprintDAO);
            this.removerTodasTarefas();
            sprintDAO.getHistorias().forEach(historiaDAO -> {
                try {
                    novaHistoria(historiaDAO);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void atualizaDadosHistoria(AnchorPane novaHistoria, String text, Integer business, Integer pts, String descr) {
        HistoriaDAO historiaDAO = new HistoriaDAO();
        AtomicReference<Integer> index = new AtomicReference<>();
        String id = novaHistoria.getId();
        Long idLong = Long.valueOf(id.replaceAll("\\D", ""));
        sprintDAO.getHistorias().forEach(historia -> {
            if (historia.getIdHistoria().equals(idLong)) {
                historiaDAO.setIdHistoria(historia.getIdHistoria());
                historiaDAO.setIdSprint(historia.getIdSprint());
                historiaDAO.setStatus(historia.getStatus());
                historiaDAO.setDtCriacao(historia.getDtCriacao());
                historiaDAO.setDtAlteracao(historia.getDtAlteracao());
                historiaDAO.setDescricao(historia.getDescricao());
                index.set(sprintDAO.getHistorias().indexOf(historia));
            }
        });
        historiaDAO.setNome(text);
        historiaDAO.setValueBusiness(business);
        historiaDAO.setPontos(pts);
        if (descr != null) {
            historiaDAO.setDescricao(descr);
        }
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