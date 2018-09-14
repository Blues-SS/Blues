package sample.utils;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {

    private Connection con = null;

    public Conexao() {
        this.Connect();
    }

    private void Connect() {
        try {
            Class.forName("org.postgres.Driver");

            String password = "root";
            String user = "bluess";
            String database = "http://localhost:5432/blues";
            con = DriverManager.getConnection(database, user, password);

            Statement stm = con.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar o driver");
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao desconectar o driver");
            e.printStackTrace();
        }
    }
}
