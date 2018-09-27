package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.utils.Conexao;
import sample.utils.SprintDAO;

public class SprintController implements Initializable {

    // DECLARAÇÃO DOS COMPONENTES DA TELA
    @FXML
    private Button AdicionarBT;
    @FXML
    private Button ModificarBT;
    @FXML
    private Button RemoverBT;
    @FXML
    private Button NovoBT;


    @FXML
    private TextField nomeTF;
    @FXML
    private TextField datainicioTF;
    @FXML
    private TextField statusTF;


    @FXML
    private TableView<Sprint> tableSprint;
    @FXML
    private TableColumn nomeCL;
    @FXML
    private TableColumn datainicioCL;
    @FXML
    private TableColumn datafimCL;
    @FXML
    private TableColumn statusCL;

    @FXML
    private DatePicker datainicioDT;
    @FXML
    private DatePicker datafimDT;

    private SprintDAO sprintDAO = new SprintDAO();


    ObservableList<Sprint> sprints;

    private int pocisaoselecionada;

    @FXML
    private void Novo(ActionEvent event) {
        nomeTF.setText("");
        datainicioDT.setDayCellFactory(null);
        datafimDT.setDayCellFactory(null);
        statusTF.setText("");
        ModificarBT.setDisable(true);
        RemoverBT.setDisable(true);
        AdicionarBT.setDisable(false);
    }

    private void PesquisaNome() throws SQLException {
//        Date datainicio;
//        Date datafim;
//        //Date dataCriacao
//        //Date dataAlteracao
//        String nome;
//        String status;
//
//
        String nome = nomeTF.getText();
        String status = statusTF.getText();
        LocalDate dtInicio = datainicioDT.getValue();
        LocalDate dtFim = datafimDT.getValue();
//
//        //datacriacao = datacriacaoDT.getValue();
//        //dataalteracao = dataalteracaoDT.getValue();
//
//
//        System.out.println(nome);
//        System.out.println(status);
//
//
//        if (nome != null && !nome.isEmpty()) {
//            System.out.printf("teste");
//            sprints = FXCollections.observableArrayList();
//            tableSprint.setItems(convertToObservable(getSprintNome(nome)));
//        }
//        if (status != null && (status.isEmpty())) {
//            System.out.printf("TESTE222");
//            sprints = FXCollections.observableArrayList();
//            tableSprint.setItems(convertToObservable(getSprintStatus(status)));
//        }
        Map<String, Object> map = new HashMap<>();
        map.put("nome", nome);
        map.put("status", status);
        map.put("dtInicio", dtInicio);
        map.put("dtFim", dtFim);

        sprintDAO.findAllFilter(new Conexao(), map);
    }


    @FXML
    private void Pesquisar() throws SQLException {

        //limpar tabela antes de carregar os novos dados

        //pegar dados dos filtro
        LocalDate datainicio;
        LocalDate datafim;
        String nome;
        String status;

        datainicio = datainicioDT.getValue();
        datafim = datafimDT.getValue();


        Conexao banco = new Conexao();

        sprints = FXCollections.observableArrayList();
        tableSprint.setItems(convertToObservable(banco.pesquisa("", "", datainicio, datafim)));
    }


    private ObservableList<Sprint> getSprints() throws SQLException {
        return convertToObservable(sprintDAO.findAll(new Conexao()));
    }

    private ObservableList<Sprint> convertToObservable(List<Sprint> list) {

        ObservableList<Sprint> sprints = FXCollections.observableArrayList();
        sprints.addAll(list);

        return sprints;
    }

//    private ObservableList<Sprint> convertsdsdsdsdToObservable(List<SprintDAO> list) {
//
//        ObservableList<Sprint> sprints = FXCollections.observableArrayList();
//        for (SprintDAO sprint: list) {
//            Sprint s = new Sprint();
//            s.setId(sprint.getId());
//            sprints.add(s);
//        }
//
//        return sprints;
//    }

    @FXML
    private void Modificar(ActionEvent event) {
        Sprint minhaSprint = new Sprint();
        minhaSprint.nome.set(nomeTF.getText());
        minhaSprint.dataInicio.set(datainicioDT.getValue());
        minhaSprint.dataFim.set(datafimDT.getValue());
        minhaSprint.status.set(statusTF.getText());
        sprints.set(pocisaoselecionada, minhaSprint);
    }

    @FXML
    private void Remover(ActionEvent event) {
        sprints.remove(pocisaoselecionada);
    }

    @FXML
    private void inicializarTabela() throws SQLException {
        nomeCL.setCellValueFactory(new PropertyValueFactory<Sprint, String>("nome"));
        datainicioCL.setCellValueFactory(new PropertyValueFactory<Sprint, String>("dataInicio"));
        datafimCL.setCellValueFactory(new PropertyValueFactory<Sprint, String>("dataFim"));
        statusCL.setCellValueFactory(new PropertyValueFactory<Sprint, String>("status"));

        sprints = FXCollections.observableArrayList();
        tableSprint.setItems(getSprints());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //iniciar tabela
        try {
            this.inicializarTabela();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ModificarBT.setDisable(true);
        RemoverBT.setDisable(true);

    }

    public void handleVoltarmenu(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
