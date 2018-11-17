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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import sample.utils.Conexao;
import sample.utils.HistoriaDAO;
import sample.utils.SprintDAO;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
    long IDHistoriaGravar;
    int IDsprintBacklog = 6;

    private HistoriaDAO historiaDAO = new HistoriaDAO();
    ObservableList<Historias> historia;
    ObservableList<Historias> historiaInfo;


    HistoriaDAO infohistoriaDAO = new HistoriaDAO();


    private ObservableList<Historias> gethistoriainfo(String ID) throws SQLException {
        return convertToObservableinfo(infohistoriaDAO.getHistoriainfo(new Conexao(), ID));
    }

    private ObservableList<Historias> convertToObservableinfo(List<Historias> list) {

        ObservableList<Historias> sprints = FXCollections.observableArrayList();
        historiaInfo.addAll(list);


        return historiaInfo;
    }


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
        carregarHistorias();

    }

    public void carregarHistorias() throws SQLException {
        getHistoriaBacklog().forEach(teste -> {
            try {
                newHistoria(teste);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
        //historiaDAO.setStatus("TODO");
        this.sprintDAO.getHistorias().add(historiaDAO);


        TextField idHistoriaTF = (TextField) novaHistoria.lookup("#idHistoriaTF");
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

        if (teste != null) {
            idHistoriaTF.setText(String.valueOf(teste.idhistoria.getValue()));
            tituloHist.setText(teste.nomehist.getValue());
            histPts.setValue(teste.pontos.getValue());
            valueBus.setValue(teste.valuebusiness.getValue());
        }

        Button histBtn = (Button) novaHistoria.lookup("#histBtn");
        //abrir informaçoes
        historia = null;
        histBtn.setOnAction(actionEvent -> {
            try {
                AnchorPane infoTela = null;
                infoTela = (AnchorPane) FXMLLoader.load(getClass().getResource("newhistBacklog.fxml"));


                TextArea descrHist = (TextArea) infoTela.lookup("#descrHist");
                TextField idHistoriasTF = (TextField) infoTela.lookup("#idHistoriasTF");
                idHistoriasTF.setText(idHistoriaTF.getText());//REMOVER
                TextField valorTit = (TextField) infoTela.lookup("#valorTit");
                valorTit.setText(tituloHist.getText());

                //pegar o id da historia e apos o nome da sprint que esta alocado
                String ID = idHistoriasTF.getText();

                //String sprint;
                //sprint = historiaDAO.getsprintAtual(new Conexao(), ID);

                //instanciar botoes
                ComboBox statusCB = (ComboBox) infoTela.lookup("#statusCB");
                statusCB.getItems().addAll("", "Em andamento", "Concluído");

                ComboBox pontosCB = (ComboBox) infoTela.lookup("#pontosCB");
                pontosCB.getItems().addAll(1, 2, 3, 5, 8, 13);

                ComboBox valueCB = (ComboBox) infoTela.lookup("#valueCB");
                valueCB.getItems().addAll(1000, 3000, 5000);


                ComboBox sprintCB = (ComboBox) infoTela.lookup("#sprintCB");

                //pegar todas Sprints criadas e adicionar no comboBox
                List<String> list = new ArrayList();
                int i, qtd;
                list = historiaDAO.getnameSprints(new Conexao());
                qtd = (list.size());

                for (i = 0; qtd > i; i++){
                    sprintCB.getItems().add(list.get(i));
                    sprintCB.setValue("BackLog");
                }

                //exemplo pegar todos dados referente a historia

                if (!((ID.isEmpty()) || (ID == null))) {
                    historiaDAO.getHistoriainfo(new Conexao(), ID).forEach(historia -> {
                        pontosCB.setValue(historia.getPontos());
                        valueCB.setValue(historia.getValuebusiness());
                        idHistoriasTF.setText(historia.getDescriscao());
                        statusCB.setValue(historia.getIdstatus());
                        descrHist.setText(historia.getDescriscao());

                        String nomeSpint;
                        //nomeSprint =
                        //sprintCB.setText(nomeSprint);
                    });
                }

                Button infoSalvar = (Button) infoTela.lookup("#infoSalvar");
                Button infoCancel = (Button) infoTela.lookup("#infoCancel");
                Button infodeletar = (Button) infoTela.lookup("#deletarBT");
                //---------

                mainBacklog.setStyle("-fx-background-color: rgba(128, 128, 128, 0.4)");
                mainBacklog.setDisable(false);
                mainBacklog.setVisible(true);
                mainBacklog.getChildren().addAll(infoTela);

                infoSalvar.setOnAction(actionEvent2 -> {
                    String titulo = valorTit.getText();
                    Object status = statusCB.getValue();
                    Object business = valueCB.getValue();
                    int pts = (int) pontosCB.getValue();
                    String descricao = descrHist.getText();
                    Object sprint = sprintCB.getValue();

                    if  (!(sprint == "BackLog")){
                        //atualizar no banco
                        try {
                            int idsprint = historiaDAO.atualizasprint(new Conexao(), (String) sprint, ID);
                            historiaDAO.setIdSprint((long) idsprint);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else{
                        historiaDAO.setIdSprint((long) IDsprintBacklog);
                    }

                    if (!(ID.isEmpty())) {//se tiver tiver id senao ira ser null e não entra aqui
                        String meuid = ID;
                        Long meuidnumber = Long.parseLong(meuid);
                        historiaDAO.setIdHistoria(meuidnumber);
                    } else {
                        historiaDAO.setIdHistoria(null);
                    }
                    historiaDAO.setNome(titulo);
                    historiaDAO.setStatus((String) status);
                    historiaDAO.setValueBusiness((Integer) business);
                    historiaDAO.setPontos(pts);
                    historiaDAO.setDescricao((String) descricao);


                    historiaDAO.save(new Conexao(), historiaDAO);

                    removerTodasTarefas();

                    //regaregar as historia (esta dando erro no historia.addall(list));

                    try {
                        this.carregarHistorias();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    mainBacklog.getChildren().remove(mainBacklog.lookup("#newhists"));
                    mainBacklog.setDisable(true);
                    mainBacklog.setVisible(false);
                });



                infoCancel.setOnAction(actionEvent3 -> {
                    mainBacklog.getChildren().remove(mainBacklog.lookup("#newhists"));
                    mainBacklog.setDisable(true);
                    mainBacklog.setVisible(false);
                });

                infodeletar.setOnAction(actionEvent3 -> {


                    JOptionPane.showMessageDialog(null, "Deseja realmente deletar esta historia ?");



                    mainBacklog.getChildren().remove(mainBacklog.lookup("#newhists"));
                    mainBacklog.setDisable(true);
                    mainBacklog.setVisible(false);

                });



            } catch (SQLException e) {
                e.printStackTrace();
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
        if (descr != null) {
            historiaDAO.setDescricao(descr);
        }
        sprintDAO.getHistorias().set(index.get(), historiaDAO);
    }


    public void salvarHist(MouseEvent event) throws SQLException {
        //gravarNovaHistoria //atualizarNovaHist
    }

    private void removerTodasTarefas() {
        i = 0;
        historia = FXCollections.observableArrayList();
        mainFlowBacklog.getChildren().setAll();
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