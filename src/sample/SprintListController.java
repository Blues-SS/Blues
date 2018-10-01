package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.utils.Conexao;
import sample.utils.SprintDAO;

public class SprintListController implements Initializable {

    // DECLARAÇÃO DOS COMPONENTES DA TELA
    @FXML
    private Button AdicionarBT;
    @FXML
    private Button setFilterBT;
    @FXML
    private Button NovoBT;
    @FXML
    private Button EditarBT;



    @FXML
    private TextField nomeTF;
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
    private TableColumn IDCL;

    @FXML
    private DatePicker datainicioDT;
    @FXML
    private DatePicker datafimDT;

    private double xOffset = 0;
    private double yOffset = 0;
    private SprintDAO sprintDAO = new SprintDAO();
    ObservableList<Sprint> sprints;


    @FXML
    private ComboBox<sprintStatusCB> CBstatus;



    ObservableList<sprintStatusCB> obsStatus;

    @FXML
    private void carregarComboBox() {

        List<sprintStatusCB> listsprintStatus = new ArrayList<>();

        sprintStatusCB status1 = new sprintStatusCB(1, "");
        sprintStatusCB status2 = new sprintStatusCB(2, "Em andamento");
        sprintStatusCB status3 = new sprintStatusCB(3, "Concluído");

        listsprintStatus.add(status1);
        listsprintStatus.add(status2);
        listsprintStatus.add(status3);

        obsStatus =  FXCollections.observableArrayList(listsprintStatus);

        CBstatus.setItems(obsStatus);
        CBstatus.setValue(status1);
    }



    @FXML
    private void setFilter(ActionEvent event) throws SQLException {

        sprintStatusCB sprintStatusCB = CBstatus.getSelectionModel().getSelectedItem();

        String nome = nomeTF.getText();
        String status = sprintStatusCB.getNome();
        LocalDate dtInicio = datainicioDT.getValue();
        LocalDate dtFim = datafimDT.getValue();



        Map<String, Object> map = new HashMap<>();
        map.put("nome", nome);
        map.put("status", status);
        map.put("dtInicio", dtInicio);
        map.put("dtFim", dtFim);


        tableSprint.setItems(convertToObservable(sprintDAO.findAllFilter(new Conexao(), map)));
    }

    @FXML
    private void limpaFilter(ActionEvent event){
        sprintStatusCB sprintStatusCB = CBstatus.getSelectionModel().getSelectedItem();

        nomeTF.setText("");
        carregarComboBox();
        datafimDT.setValue(null);
        datainicioDT.setValue(null);

    }

    private ObservableList<Sprint> getSprints() throws SQLException {
        return convertToObservable(sprintDAO.findAll(new Conexao()));
    }

    private ObservableList<Sprint> convertToObservable(List<Sprint> list) {

        ObservableList<Sprint> sprints = FXCollections.observableArrayList();
        sprints.addAll(list);


        return sprints;
    }



    @FXML
    private void inicializarTabela() throws SQLException {
        nomeCL.setCellValueFactory(new PropertyValueFactory<Sprint, String>("nome"));
        datainicioCL.setCellValueFactory(new PropertyValueFactory<Sprint, String>("dataInicio"));
        datafimCL.setCellValueFactory(new PropertyValueFactory<Sprint, String>("dataFim"));
        statusCL.setCellValueFactory(new PropertyValueFactory<Sprint, String>("status"));

        sprints = FXCollections.observableArrayList();
        tableSprint.setItems(getSprints());

        //EditarBT.setDisable(true);
        carregarComboBox();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //iniciar tabela
        try {
            this.inicializarTabela();
        } catch (SQLException e) {
            e.printStackTrace();
        }




    }

    @FXML
    public int getidlinhaselecionada() {
        if (tableSprint != null) {
            List<Sprint> tabela = tableSprint.getSelectionModel().getSelectedItems();
            if (tabela.size() == 1) {
                final Sprint competicionSeleccionada = tabela.get(0);
                int IDSprint = competicionSeleccionada.getId();

                //Sprint.setIDsprint(IDSprint);
                //System.out.println(Sprint.getIDSprint());

                return IDSprint;
            }
        }
        return 0;
    }


        @FXML
    private void Editar(ActionEvent event) throws IOException {

        //ID ppara carregar os historias referente a ele
        final int idsprint = getidlinhaselecionada();


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

    public void handleVoltarmenu(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void Novo(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SprintCrud.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
