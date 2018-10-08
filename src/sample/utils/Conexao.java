package sample.utils;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import sample.Sprint;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Conexao {


    private Connection con = null;
    /**
     * Usada para realizar as instrucoes SQL
     */
    public Statement stmt;
    /**
     * Retorna os dados das tabelas do banco
     */
    public ResultSet rs;

    @FXML
    ObservableList<Sprint> sprints;


    private String endereco;
    private String usuario;
    private String senha;

    public void Conectar() {
        endereco = "jdbc:postgresql://localhost:5432/Blues";
        usuario = "postgres";
        senha = "123";
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(endereco, usuario, senha);
            stmt = con.createStatement();
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar o driver");
            cnfe.printStackTrace();
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "erro na query");
            sqlex.printStackTrace();
        }
    }

    public Connection getCon() {
        return con;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void Desconectar() {
        try {
            con.close();
        } catch (SQLException onConClose) {
            JOptionPane.showMessageDialog(null, "Erro ao desconectar o banco");
            onConClose.printStackTrace();
        }
    }



    @FXML
    public List<Sprint> pesquisa(String nome, String Status, LocalDate datacriacao, LocalDate dataalteracao) throws SQLException {
        List<Sprint> list = new ArrayList();
        Conectar();

        if ((datacriacao != null) && (dataalteracao != null)){
        String sql = "select id_sprint, nome , status, dt_criacao, dt_alteracao from Sprint where dt_criacao >= datacriacao and dt_criacao <= datacriacao" +
                "union" +
                "select id_sprint, nome , status, dt_criacao, dt_alteracao from Sprint where dt_alteracao >= dataalteracao and dt_alteracao <= dataalteracao";

            ResultSet rs = stmt.executeQuery(sql);
        }

        while (rs.next()) {
            Sprint sprint = new Sprint();
            sprint.setId(rs.getInt("id_sprint"));
            sprint.setNome(rs.getString("nome"));
            sprint.setStatus(rs.getString("status"));
            sprint.setDataInicio(rs.getDate("dt_criacao"));
            sprint.setDataFim(rs.getDate("dt_alteracao"));
            System.out.println(""+""+""+"");
            list.add(sprint);
        }
        con.close();
        return  list;
    }

    @FXML
    public List<Sprint> pesquisaNome(String nome) throws SQLException {
        List<Sprint> list = new ArrayList();

        Conectar();
        //String sql = "insert into SPRINT (NOME, STATUS, DT_CRIACAO, DT_ALTERACAO) values ('teste', 'testando' , '1970-01-01', '1970-01-01')";
        /**
         *NAO FAZ O SELECT COM * PORQUE PODE DAR BOSTA, SEMPRE PROCURE OS CAMPOS QUE TU QUER, NEM QUE SEJA TODOS OS DA TABLELA
         * */

        String sql = "select * from Sprint where nome = "+ "'"+nome+"'";

        //stmt.executeUpdate(sql);
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Sprint sprint = new Sprint();
            sprint.setId(rs.getInt("id_sprint"));
            sprint.setNome(rs.getString("nome"));
            sprint.setStatus(rs.getString("status"));
            sprint.setDataInicio(rs.getDate("dt_criacao"));
            sprint.setDataFim(rs.getDate("dt_alteracao"));
            list.add(sprint);
        }
        con.close();
        return list;
    }


    @FXML
    public List<Sprint> pesquisaStatus(String status) throws SQLException {
        List<Sprint> list = new ArrayList();

        Conectar();
        //String sql = "insert into SPRINT (NOME, STATUS, DT_CRIACAO, DT_ALTERACAO) values ('teste', 'testando' , '1970-01-01', '1970-01-01')";

        String sql = "select * from Sprint where status = "+ "'"+status+"'";

        //stmt.executeUpdate(sql);
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Sprint sprint = new Sprint();
            sprint.setId(rs.getInt("id_sprint"));
            sprint.setNome(rs.getString("nome"));
            sprint.setStatus(rs.getString("status"));
            sprint.setDataInicio(rs.getDate("dt_criacao"));
            sprint.setDataFim(rs.getDate("dt_alteracao"));
            list.add(sprint);
        }
        con.close();
        return list;
    }

}