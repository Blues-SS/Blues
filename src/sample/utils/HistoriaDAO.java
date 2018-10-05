package sample.utils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoriaDAO {

    private Long idHistoria;

    private Long idSprint;

    private Long idStatus;

    private String nome;

    private Integer valueBusiness;
    private Integer pontos;

    private String descricao;

    private Date dtCriacao;

    private Date dtAlteracao;


    public Long getIdHistoria() {
        return idHistoria;
    }

    public void setIdHistoria(Long idHistoria) {
        this.idHistoria = idHistoria;
    }

    public Long getIdSprint() {
        return idSprint;
    }

    public void setIdSprint(Long idSprint) {
        this.idSprint = idSprint;
    }

    public Long getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getValueBusiness() {
        return valueBusiness;
    }

    public void setValueBusiness(Integer valueBusiness) {
        this.valueBusiness = valueBusiness;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    public static List<HistoriaDAO> findByIdSprint(Conexao conexao, Integer idSprint) throws SQLException {
        List<HistoriaDAO> historiaDAOS = new ArrayList<>();
        String sql = "SELECT his.id_historia, his.id_sprint, his.id_status, his.nome, his.value_business, his.dt_criacao, his.dt_alteracao, his.descricao, st.ds_status from historia his" +
                "inner join status st on his.id_status = st.id_status where id_sprint =" + idSprint;

        conexao.Conectar();

        ResultSet rs = conexao.getStmt().executeQuery(sql);

        while (rs.next()) {
            HistoriaDAO historiaDAO = new HistoriaDAO();
            historiaDAO.setIdHistoria(rs.getLong("id_historia"));
            historiaDAO.setDescricao(rs.getString("descricao"));
            historiaDAO.setDtAlteracao(rs.getDate("dt_alteracao"));
            historiaDAO.setDtCriacao(rs.getDate("dt_criacao"));
            historiaDAO.setDescricao(rs.getString("descricao"));
            historiaDAO.setValueBusiness(rs.getInt("value_business"));
            historiaDAO.setNome(rs.getString("nome"));
            historiaDAO.setIdSprint(rs.getLong("id_sprint"));
            historiaDAO.setIdStatus(rs.getLong("id_status"));
            historiaDAOS.add(historiaDAO);
        }
        conexao.Desconectar();
        return historiaDAOS;
    }

    public static HistoriaDAO save(Conexao conexao, HistoriaDAO historiaDAO) {

        if (historiaDAO.getIdHistoria() != null) {
            update(conexao, historiaDAO);
        } else {
            create(conexao, historiaDAO);
        }

        return historiaDAO;
    }

    private static void create(Conexao conexao, HistoriaDAO historiaDAO) {
    String sql = "INSERT INTO HISTORIA (ID_SPRINT, ID_STATUS, NOME, DT_CRIACAO, DT_ALTERACAO, DESCRICAO) VALUES ("
             + historiaDAO.getIdSprint() + ","
             + historiaDAO.getIdStatus() + ","
             + "'" + historiaDAO.getNome() + "',"
             + "'" + historiaDAO.getDtCriacao() + "',"
             + "'" + historiaDAO.getDtAlteracao() + "',"
             + "'" + historiaDAO.getDescricao() + "',";


    }

    private static void update(Conexao conexao, HistoriaDAO historiaDAO) {
    String sql = "UPDATE HISTORIA SET (id_sprint = "
            + historiaDAO.getIdSprint() + ","
            + "id_status = "
            + historiaDAO.getIdStatus() + ","
            + "nome = "
            + "'" + historiaDAO.getNome() + "',"
            + "dt_criacao = "
            + "'" + historiaDAO.getDtCriacao() + "',"
            + "dt_alteracao = "
            + "'" + historiaDAO.getDtAlteracao() + "',"
            + "descricao = "
            + "'" + historiaDAO.getDescricao() + "',"
            + ") WHERE ID_HISTORIA  = " + historiaDAO.getIdHistoria();
    }
}
