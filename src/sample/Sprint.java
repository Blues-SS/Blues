package sample;

import javafx.beans.property.SimpleStringProperty;


public class Sprint {

    public SimpleStringProperty nome = new SimpleStringProperty();
    public SimpleStringProperty datainicio = new SimpleStringProperty();
    public SimpleStringProperty datafim = new SimpleStringProperty();
    public SimpleStringProperty status = new SimpleStringProperty();


    public String getNome(){
        return nome.get();
    }

    public String getDatainicio(){
        return datainicio.get();
    }

    public String getDatafim(){
        return datafim.get();
    }

    public String getStatus(){
        return status.get();
    }

}