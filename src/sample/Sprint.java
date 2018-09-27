package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;


public class Sprint {

    public SimpleIntegerProperty id = new SimpleIntegerProperty();
    public SimpleStringProperty nome = new SimpleStringProperty();
    //public SimpleStringProperty dataInicio = new SimpleStringProperty();
    public SimpleObjectProperty dataInicio = new SimpleObjectProperty<>();
    public SimpleObjectProperty dataFim = new SimpleObjectProperty<>();

    public SimpleStringProperty status = new SimpleStringProperty();

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNome() {
        return nome.get();
    }

    public SimpleStringProperty nomeProperty() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public Object getDataInicio() {
        return dataInicio.get();
    }

    public SimpleObjectProperty dataInicioProperty() {
        return dataInicio;
    }

    public void setDataInicio(Object dataInicio) {
        this.dataInicio.set(dataInicio);
    }

    public Object getDataFim() {
        return dataFim.get();
    }

    public SimpleObjectProperty dataFimProperty() {
        return dataFim;
    }

    public void setDataFim(Object dataFim) {
        this.dataFim.set(dataFim);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}