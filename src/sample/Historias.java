package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Historias {


    public SimpleIntegerProperty idhistoria = new SimpleIntegerProperty();
    public SimpleIntegerProperty idsprint = new SimpleIntegerProperty();
    public SimpleStringProperty idstatus = new SimpleStringProperty();
    public SimpleStringProperty nomehist = new SimpleStringProperty();
    public SimpleIntegerProperty valuebusiness = new SimpleIntegerProperty();
    public SimpleObjectProperty dtcriacao = new SimpleObjectProperty<>();
    public SimpleObjectProperty dtalteracao = new SimpleObjectProperty<>();


    public int getIdhistoria() {
        return idhistoria.get();
    }

    public SimpleIntegerProperty idhistoriaProperty() {
        return idhistoria;
    }

    public void setIdhistoria(int idhistoria) {
        this.idhistoria.set(idhistoria);
    }

    public int getIdsprint() {
        return idsprint.get();
    }

    public SimpleIntegerProperty idsprintProperty() {
        return idsprint;
    }

    public void setIdsprint(int idsprint) {
        this.idsprint.set(idsprint);
    }

    public String getIdstatus() {
        return idstatus.get();
    }

    public SimpleStringProperty idstatusProperty() {
        return idstatus;
    }

    public void setIdstatus(String idstatus) {
        this.idstatus.set(idstatus);
    }

    public String getNomehist() {
        return nomehist.get();
    }

    public SimpleStringProperty nomehistProperty() {
        return nomehist;
    }

    public void setNomehist(String nomehist) {
        this.nomehist.set(nomehist);
    }

    public int getValuebusiness() {
        return valuebusiness.get();
    }

    public SimpleIntegerProperty valuebusinessProperty() {
        return valuebusiness;
    }

    public void setValuebusiness(int valuebusiness) {
        this.valuebusiness.set(valuebusiness);
    }

    public Object getDtcriacao() {
        return dtcriacao.get();
    }

    public SimpleObjectProperty dtcriacaoProperty() {
        return dtcriacao;
    }

    public void setDtcriacao(Object dtcriacao) {
        this.dtcriacao.set(dtcriacao);
    }

    public Object getDtalteracao() {
        return dtalteracao.get();
    }

    public SimpleObjectProperty dtalteracaoProperty() {
        return dtalteracao;
    }

    public void setDtalteracao(Object dtalteracao) {
        this.dtalteracao.set(dtalteracao);
    }
}
