package sample.utils;

import sample.Sprint;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public List<Sprint> findAll(Conexao conexao) throws SQLException {
        List<Sprint> list = new ArrayList();

        conexao.Conectar();

        String sql = "select id_sprint, nome, status, dt_inicio, dt_fim, dt_criacao, dt_alteracao from sprint";

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

        String sql = "select id_sprint, nome, status, dt_inicio, dt_fim, dt_criacao, dt_alteracao from sprint " +
                "where 1=1 ";

        String nome = (String) filters.get("nome");
        if (!nome.isEmpty()) {
            sql = sql +" and nome LIKE '%" + nome + "%'";
        }

/*        String status = (String) filters.get("status");
        if (!status.isEmpty()) {
            sql.concat(" and status = '" + status + "'");
        }

        Date dtInicio = (Date) filters.get("dtInicio");
        Date dtFim = (Date) filters.get("dtFim");

        if (dtInicio != null && dtFim != null) {

            String datas = " and dt_inicio <= dtInicio and dtInicio >= dtFim and dt_fim >= dtFim and dtFim <= dt_inicio";
            datas.replaceAll("dtInicio", String.valueOf(dtInicio)).replaceAll("dtFim", String.valueOf(dtFim));
            sql.concat(datas);
        }
*/
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

    private Sprint toInterface(SprintDAO sprintDAO) {
        Sprint sprint = new Sprint();

        sprint.setId(sprintDAO.getIdSprint());
        sprint.setNome(sprintDAO.getDsSprint());
        sprint.setStatus(sprintDAO.getStatus());
        sprint.setDataInicio(sprintDAO.getDtInicio());
        sprint.setDataFim(sprintDAO.getDtFim());

        return sprint;
    }

}
