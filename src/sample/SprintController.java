package sample;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SprintController implements Initializable {

    // DECLARAÇÃO DOS COMPONENTES DA TELA
    @FXML private Button AdicionarBT;
    @FXML private Button ModificarBT;
    @FXML private Button RemoverBT;
    @FXML private Button NovoBT;


    @FXML private TextField nomeTF;
    @FXML private TextField datainicioTF;
    @FXML private TextField datafimTF;
    @FXML private TextField statusTF;


    @FXML private TableView<Sprint> tableSprint;
    @FXML private TableColumn nomeCL;
    @FXML private TableColumn datainicioCL;
    @FXML private TableColumn datafimCL;
    @FXML private TableColumn statusCL;
    ObservableList<Sprint> sprints;

    private int posicionPersonaEnTabla;

    @FXML private void Novo(ActionEvent event) {
        nomeTF.setText("");
        datainicioTF.setText("");
        datafimTF.setText("");
        statusTF.setText("");
        ModificarBT.setDisable(true);
        RemoverBT.setDisable(true);
        AdicionarBT.setDisable(false);
    }

    @FXML private void Adicionar(ActionEvent event) {
        Sprint minhaSprint = new Sprint();
        minhaSprint.nome.set(nomeTF.getText());
        minhaSprint.datainicio.set(datainicioTF.getText());
        minhaSprint.datafim.set(datafimTF.getText());
        minhaSprint.status.set(statusTF.getText());
        sprints.add(minhaSprint);
    }

    @FXML private void Modificar(ActionEvent event) {
        Sprint persona = new Sprint();
        persona.nome.set(nomeTF.getText());
        persona.datainicio.set(datainicioTF.getText());
        persona.datafim.set(datafimTF.getText());
        persona.status.set(statusTF.getText());
        sprints.set(posicionPersonaEnTabla, persona);
    }

    @FXML private void Remover(ActionEvent event) {
        sprints.remove(posicionPersonaEnTabla);
    }

    //detectar mudanças para apos clicar em algum dado carregar os mesmo nos TF
    private final ListChangeListener<Sprint> selectorTablaPersonas =
            new ListChangeListener<Sprint>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends Sprint> c) {
                    carregadadosTF();
                }
            };


    public Sprint getTablaPersonasSeleccionada() {
        if (tableSprint != null) {
            List<Sprint> tabla = tableSprint.getSelectionModel().getSelectedItems();
            if (tabla.size() == 1) {
                final Sprint competicionSeleccionada = tabla.get(0);
                return competicionSeleccionada;
            }
        }
        return null;
    }

    private void carregadadosTF() {
        final Sprint persona = getTablaPersonasSeleccionada();
        posicionPersonaEnTabla = sprints.indexOf(persona);

        if (persona != null) {

            nomeTF.setText(persona.getNome());
            datainicioTF.setText(persona.getDatainicio());
            datafimTF.setText(persona.getDatafim());
            statusTF.setText(persona.getStatus());


            ModificarBT.setDisable(false);
            RemoverBT.setDisable(false);
            AdicionarBT.setDisable(true);

        }
    }

    private void inicializarTablaPersonas() {
        nomeCL.setCellValueFactory(new PropertyValueFactory<Sprint, String>("nome"));
        datainicioCL.setCellValueFactory(new PropertyValueFactory<Sprint, String>("datainicio"));
        datafimCL.setCellValueFactory(new PropertyValueFactory<Sprint, String>("datafim"));
        statusCL.setCellValueFactory(new PropertyValueFactory<Sprint, String>("status"));

        sprints = FXCollections.observableArrayList();
        tableSprint.setItems(sprints);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //iniciar tabela
        this.inicializarTablaPersonas();

        ModificarBT.setDisable(true);
        RemoverBT.setDisable(true);

        final ObservableList<Sprint> tablaPersonaSel = tableSprint.getSelectionModel().getSelectedItems();
        tablaPersonaSel.addListener(selectorTablaPersonas);

        //Exemplo para carregar tabela
        //for (int i = 0; i < 5; i++) {
        //    Sprint p1 = new Sprint();
        //    p1.nome.set("1.0 SP " + i);
        //    p1.datainicio.set("13/09/2018");
        //    p1.datafim.set("28/09/2018");
        //    p1.status.set("Em progresso");
        //    sprints.add(p1);
        //}
    }

    public void handleVoltarmenu(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}