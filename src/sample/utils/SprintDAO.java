package sample.utils;

import sample.Sprint;

import javax.net.ssl.HostnameVerifier;
import javax.swing.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SprintDAO {

    private Integer idSprint;

    private String dsSprint;

    private String status;

    private Date dtCriacao;

    private Date dtAlteracao;

    private Date dtInicio;

    private Date dtFim;

    private List<HistoriaDAO> historias = new ArrayList<>();

    public Integer getIdSprint() {
        return idSprint;
    }

    public void setIdSprint(Integer idSprint) {
        this.idSprint = idSprint;
    }

    public String getDsSprint() {
        return dsSprint;
    }

    public void setDsSprint(String dsSprint) {
        this.dsSprint = dsSprint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtAlteracao() {
        return dtAlteracao;
    }

    public void setDtAlteracao(Date dtAlteracao) {
        this.dtAlteracao = dtAlteracao;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public Date getDtFim() {
        return dtFim;
    }

    public void setDtFim(Date dtFim) {
        this.dtFim = dtFim;
    }

    public List<HistoriaDAO> getHistorias() {
        return historias;
    }

    public void setHistorias(List<HistoriaDAO> historias) {
        this.historias = historias;
    }

    public List<Sprint> findAll(Conexao conexao) throws SQLException {
        List<Sprint> list = new ArrayList();

        conexao.Conectar();

        String sql = "select id_sprint, nome, status, dt_inicio, dt_fim, dt_criacao, dt_alteracao from sprint where id_sprint > 1";

        ResultSet rs = conexao.getStmt().executeQuery(sql);

        while (rs.next()) {
            SprintDAO sprint = new SprintDAO();
            sprint.setIdSprint(rs.getInt("id_sprint"));
            sprint.setDsSprint(rs.getString("nome"));
            sprint.setStatus(rs.getString("status"));
            sprint.setDtInicio(rs.getDate("dt_inicio"));
            sprint.setDtFim(rs.getDate("dt_inicio"));
            sprint.setDtCriacao(rs.getDate("dt_criacao"));
            sprint.setDtAlteracao(rs.getDate("dt_alteracao"));

            list.add(toInterface(sprint));
        }
        conexao.Desconectar();

        return list;
    }

    public List<Sprint> findAllFilter(Conexao conexao, Map<String, Object> filters) throws SQLException {
        List<Sprint> list = new ArrayList();

        conexao.Conectar();

        String sql2 = "";
        String sql = "select id_sprint, nome, status, dt_inicio, dt_fim, dt_criacao, dt_alteracao from sprint " +
                "where 1=1 ";

        String nome = (String) filters.get("nome");
        String status = (String) filters.get("status");
        LocalDate dtInicio = (LocalDate) filters.get("dtInicio");
        LocalDate dtFim = (LocalDate) filters.get("dtFim");


        if (!nome.isEmpty()) {
            sql2 = " and nome LIKE '%" + nome + "%'";
        }

        if (!status.isEmpty()) {
            sql2 = sql2 + " and status = '" + status + "'";
        }

        if (dtInicio != null && dtFim != null) {
            sql2 = sql2 + " and dt_inicio >=  " + "'" + dtInicio + "' " + "and dt_Inicio <= " + "'" + dtFim + "'" + " or dt_fim >= " + "'" + dtFim + "'" + " and dt_fim <= " + "'" + dtFim + "'";
        }
        sql = sql + sql2;
        ResultSet rs = conexao.getStmt().executeQuery(sql);

        while (rs.next()) {
            SprintDAO sprint = new SprintDAO();
            sprint.setIdSprint(rs.getInt("id_sprint"));
            sprint.setDsSprint(rs.getString("nome"));
            sprint.setStatus(rs.getString("status"));
            sprint.setDtInicio(rs.getDate("dt_inicio"));
            sprint.setDtFim(rs.getDate("dt_fim"));
            sprint.setDtCriacao(rs.getDate("dt_criacao"));
            sprint.setDtAlteracao(rs.getDate("dt_alteracao"));

            list.add(toInterface(sprint));
        }
        conexao.Desconectar();


        if (list.size() == 0) {
            JOptionPane.showMessageDialog(null, "Nenhum dado encontrada para o filtro em questão!");
        }

        return list;
    }

    private Sprint toInterface(SprintDAO sprintDAO) {
        Sprint sprint = new Sprint();

        sprint.setId(sprintDAO.getIdSprint());
        sprint.setNome(sprintDAO.getDsSprint());
        sprint.setStatus(sprintDAO.getStatus());
        sprint.setDataInicio(sprintDAO.getDtInicio());
        sprint.setDataFim(sprintDAO.getDtFim());

        return sprint;
    }


    public SprintDAO findOne(Conexao conexao, Integer idSprint) throws SQLException {
        SprintDAO sprint = new SprintDAO();
        conexao.Conectar();

        String sql = "select id_sprint, nome, status, dt_inicio, dt_fim, dt_criacao, dt_alteracao from sprint " +
                "where id_sprint = " + idSprint;

        ResultSet rs = conexao.getStmt().executeQuery(sql);
        rs.next();
        sprint = mapper(rs, sprint.getHistorias());
        sprint.setHistorias(HistoriaDAO.findByIdSprint(conexao, idSprint));
        conexao.Desconectar();

        return sprint;
    }

    public SprintDAO create(Conexao conexao, SprintDAO sprintDAO) {
        try {

            conexao.Conectar();
            System.out.println(sprintDAO.getDtInicio());

            if (sprintDAO.getDtInicio() == null || sprintDAO.getDtFim() == null) {
                JOptionPane.showMessageDialog(null, "Data não selecionada!");
            } else {

                String sql = "INSERT INTO SPRINT (nome, status, dt_inicio, dt_fim, dt_criacao, dt_alteracao) VALUES ('"
                        + sprintDAO.getDsSprint() + "', '"
                        + sprintDAO.getStatus() + "', '"
                        + sprintDAO.getDtInicio() + "', '"
                        + sprintDAO.getDtFim() + "',"
                        + "current_timestamp, "
                        + "current_timestamp ) RETURNING *, id_sprint";

                ResultSet resultSet = conexao.getStmt().executeQuery(sql);

                resultSet.next();
                sprintDAO = mapper(resultSet, sprintDAO.getHistorias());

                List<HistoriaDAO> newList = new ArrayList<>();
                saveHistorias(conexao, sprintDAO, newList);

                sprintDAO.setHistorias(newList);
                return sprintDAO;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sprintDAO;
    }

    public SprintDAO update(Conexao conexao, SprintDAO sprintDAO) {
        try {

            conexao.Conectar();

            if (sprintDAO.getDtInicio() == null || sprintDAO.getDtFim() == null) {
                JOptionPane.showMessageDialog(null, "Data não selecionada!");
            } else {

                String sql = "UPDATE SPRINT SET " +
                        "id_sprint = " + sprintDAO.getIdSprint() + ","
                        + "status = '" + sprintDAO.getStatus() + "',"
                        + "nome = '" + sprintDAO.getDsSprint() + "',"
                        + "dt_alteracao = '" + sprintDAO.getDtAlteracao() + "',"
                        + "dt_fim = '" + sprintDAO.getDtFim() + "',"
                        + "dt_inicio = '" + sprintDAO.getDtInicio() + "'"
                        + " WHERE ID_SPRINT = " + sprintDAO.getIdSprint()
                        + " RETURNING *";

                conexao.getStmt().executeQuery(sql);

                List<HistoriaDAO> newList = new ArrayList<>();
                saveHistorias(conexao, sprintDAO, newList);

                sprintDAO.setHistorias(newList);
                return sprintDAO;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sprintDAO;
    }

    private void saveHistorias(Conexao conexao, SprintDAO sprintDAO, List<HistoriaDAO> newList) {
        sprintDAO.getHistorias().forEach(historiaDAO -> {
            historiaDAO.setIdSprint(Long.valueOf(sprintDAO.getIdSprint()));
            if (historiaDAO.getIdHistoria() < 1000) {
                historiaDAO.setIdHistoria(null);
            }
            newList.add(historiaDAO.save(conexao, historiaDAO));
        });
    }

    private SprintDAO mapper(ResultSet rs, List<HistoriaDAO> historias) throws SQLException {
        SprintDAO sprint = new SprintDAO();

        sprint.setIdSprint(rs.getInt("id_sprint"));
        sprint.setDsSprint(rs.getString("nome"));
        sprint.setStatus(rs.getString("status"));
        sprint.setDtInicio(rs.getDate("dt_inicio"));
        sprint.setDtFim(rs.getDate("dt_fim"));
        sprint.setDtCriacao(rs.getDate("dt_criacao"));
        sprint.setDtAlteracao(rs.getDate("dt_alteracao"));
        sprint.setHistorias(historias);

        return sprint;
    }
}




