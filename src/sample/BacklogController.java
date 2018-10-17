package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import sample.utils.Conexao;
import sample.utils.HistoriaDAO;
import sample.utils.SprintDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class BacklogController {

    @FXML
    private Button cancelarBT;
    @FXML
    private AnchorPane mainBacklog;
    @FXML
    private FlowPane mainFlowBacklog;

    private double xOffset = 0;
    private double yOffset = 0;
    int i = 0;
    private static SprintDAO sprintDAO;
    private static Conexao conexao = new Conexao();


    private HistoriaDAO historiaDAO = new HistoriaDAO();
    ObservableList<Historias> historia;

    @FXML
    private FlowPane toDo;




    private ObservableList<Historias> getHistoriaBacklog() throws SQLException {
        return convertToObservable(historiaDAO.getHistoriaBacklog(new Conexao()));
    }

    private ObservableList<Historias> convertToObservable(List<Historias> list) {

        ObservableList<Historias> sprints = FXCollections.observableArrayList();
        historia.addAll(list);


        return historia;
    }

    public void initialize() throws SQLException {

        historia = FXCollections.observableArrayList();

        /*getHistoriaBacklog().forEach(teste ->{
            try {
                newHistoria(teste);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/
    }


    public void handleNovaHistoria(MouseEvent event) throws Exception {
        newHistoria(null);
    }


    public void newHistoria(Historias teste) throws Exception {

            AnchorPane novaHistoria = FXMLLoader.load(getClass().getResource("historiaBacklog.fxml"));
            novaHistoria.setId("Hist" + i); // tem que verificar esse ID que vai ficar igual com algumas histórias criadas na Nova Sprint, porque o i reseta o valor

            //-----------------------
            this.sprintDAO = new SprintDAO();
            HistoriaDAO historiaDAO = new HistoriaDAO();
            historiaDAO.setIdHistoria((long) i);
            historiaDAO.setStatus("TODO");
            this.sprintDAO.getHistorias().add(historiaDAO);


            javafx.scene.control.TextField idHistoriaTF = (javafx.scene.control.TextField) novaHistoria.lookup("#idHistoriaTF");
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

            if (teste != null) {
                idHistoriaTF.setText(String.valueOf(teste.idhistoria.getValue()));
                tituloHist.setText(teste.nomehist.getValue());
                histPts.setValue(teste.pontos.getValue());
                valueBus.setValue(teste.valuebusiness.getValue());
            }

            javafx.scene.control.Button histBtn = (javafx.scene.control.Button) novaHistoria.lookup("#histBtn");
            //abrir informaçoes
            histBtn.setOnAction(actionEvent -> {
                try {
                    AnchorPane infoTela = FXMLLoader.load(getClass().getResource("newhistBacklog.fxml"));

                    javafx.scene.control.TextField valorTit = (javafx.scene.control.TextField) infoTela.lookup("#valorTit");
                    valorTit.setText(tituloHist.getText());
                    //instanciar botoes
                    ComboBox statusCB = (ComboBox) infoTela.lookup("#statusCB");
                    statusCB.getItems().addAll("", "Em andamento", "Concluído");
                    ComboBox pontosCB = (ComboBox) infoTela.lookup("#pontosCB");
                    pontosCB.getItems().addAll(1, 2, 3, 5, 8, 13);

                    ComboBox valueCB = (ComboBox) infoTela.lookup("#valueCB");
                    valueCB.getItems().addAll(1000, 3000, 5000);


                    if (!histPts.getSelectionModel().isEmpty()) {
                        pontosCB.setValue(histPts.getSelectionModel().getSelectedItem().toString());
                    }

                    if (!valueBus.getSelectionModel().isEmpty()) {
                        valueCB.setValue(valueBus.getSelectionModel().getSelectedItem().toString());
                    }

                    javafx.scene.control.TextField idHistoriasTF = (javafx.scene.control.TextField) infoTela.lookup("#idHistoriasTF");
                    idHistoriasTF.setText(idHistoriaTF.getText());


                    javafx.scene.control.TextArea descrHist = (javafx.scene.control.TextArea) infoTela.lookup("#descrHist");
                    javafx.scene.control.Button infoSalvar = (javafx.scene.control.Button) infoTela.lookup("#infoSalvar");
                    //---------

                    mainBacklog.setStyle("-fx-background-color: rgba(128, 128, 128, 0.4)");
                    mainBacklog.setDisable(false);
                    mainBacklog.setVisible(true);
                    mainBacklog.getChildren().addAll(infoTela);

                    infoSalvar.setOnAction(actionEvent2 -> {
                        String id = idHistoriasTF.getText();
                        String titulo = valorTit.getText();
                        int business = (int) valueCB.getSelectionModel().getSelectedItem();
                        int pts = (int) pontosCB.getSelectionModel().getSelectedItem();
                        String status = (String) statusCB.getSelectionModel().getSelectedItem();
                        String descricao = descrHist.getText();
                        if (id == null || id.isEmpty()) {
                            historiaDAO.newHistoria(new Conexao(), titulo, business, pts, status, descricao);
                        }

                    /*atualizaDadosHistoria(novaHistoria,
                            titulo,
                            business != null ? Integer.valueOf(business.toString()) : null,
                            pts != null ? Integer.valueOf(pts.toString()) : null,
                            descrHist.getText());*/
                        mainBacklog.getChildren().remove(mainBacklog.lookup("#newhists"));

                        System.out.println(historiaDAO.getIdHistoria());
                        mainBacklog.setDisable(true);
                        mainBacklog.setVisible(false);
                    });


                    Button infoCancel = (Button) infoTela.lookup("#infoCancel");
                    infoCancel.setOnAction(actionEvent3 -> {
                        mainBacklog.getChildren().remove(mainBacklog.lookup("#newhists"));
                        mainBacklog.setDisable(true);
                        mainBacklog.setVisible(false);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            //historiaButtonOnAction(novaHistoria, histPts, valueBus, tituloHist, histBtn);
            //-----------------

            mainFlowBacklog.getChildren().add(novaHistoria);
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