package sample;

import javafx.beans.property.SimpleStringProperty;

public class Sprint {

    public SimpleStringProperty nome = new SimpleStringProperty();
    public SimpleStringProperty Status = new SimpleStringProperty();

    public String getnome(){
        return nome.get();
    }

    public String Status(){
        return Status.get();
    }

}
