package sample.utils;

import sample.Historias;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoriaDAO {

    private Long idHistoria;

    private Long idSprint;

    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        String sql = "SELECT his.id_historia, his.id_sprint, his.status, his.nome, his.value_business, his.pontos, his.dt_criacao, his.dt_alteracao, his.descricao " +
                "from historia his " +
                "where id_sprint =" + idSprint;

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
            historiaDAO.setPontos(rs.getInt("pontos"));
            historiaDAO.setNome(rs.getString("nome"));
            historiaDAO.setIdSprint(rs.getLong("id_sprint"));
            historiaDAO.setStatus(rs.getString("status"));
            historiaDAOS.add(historiaDAO);
        }
        conexao.Desconectar();
        return historiaDAOS;
    }

    public HistoriaDAO save(Conexao conexao, HistoriaDAO historiaDAO) {

        if (historiaDAO.getIdHistoria() != null) {
            return update(conexao, historiaDAO);
        } else {
            return create(conexao, historiaDAO);
        }
    }

    public HistoriaDAO histBacklog(Conexao conexao, HistoriaDAO historiaDAO) {

        if (historiaDAO.getIdHistoria() != null) {
            return update(conexao, historiaDAO);
        } else {
            return create(conexao, historiaDAO);
        }
    }

    private HistoriaDAO create(Conexao conexao, HistoriaDAO historiaDAO) {
        try {
            conexao.Conectar();

            String sql = "INSERT INTO HISTORIA (ID_SPRINT, STATUS, NOME, PONTOS, VALUE_BUSINESS, DT_CRIACAO, DT_ALTERACAO, DESCRICAO) VALUES ("
                    + historiaDAO.getIdSprint() + ","
                    + "'" + historiaDAO.getStatus() + "'" + ","
                    + "'" + historiaDAO.getNome() + "',"
                    + historiaDAO.getPontos() + ","
                    + historiaDAO.getValueBusiness() + ","
                    + "CURRENT_DATE,"
                    + "CURRENT_DATE,"
                    + "'" + historiaDAO.getDescricao() + "') RETURNING *, id_historia";

            ResultSet rs = conexao.getStmt().executeQuery(sql);

            rs.next();
            historiaDAO = mapper(rs);
            return historiaDAO;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historiaDAO;
    }

    private HistoriaDAO update(Conexao conexao, HistoriaDAO historiaDAO) {
        try {
            conexao.Conectar();

            String sql = "UPDATE HISTORIA SET id_sprint = "
                    + historiaDAO.getIdSprint() + ","
                    + "status = '"
                    + historiaDAO.getStatus() + "',"
                    + "nome = "
                    + "'" + historiaDAO.getNome() + "',"
                    + "pontos = " +
                    + historiaDAO.getPontos() + ","
                    + "value_business = " +
                    + historiaDAO.getValueBusiness() + ","
                    + "dt_alteracao = CURRENT_DATE " + ","
                    + "descricao = "
                    + "'" + historiaDAO.getDescricao() + "'"
                    + " WHERE ID_HISTORIA = " + historiaDAO.getIdHistoria()
                    + " RETURNING *";

            conexao.getStmt().executeQuery(sql);


            return historiaDAO;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historiaDAO;
    }



    public List<String> getnameSprints(Conexao conexao) throws SQLException {
        List<String> list = new ArrayList();

        conexao.Conectar();

        String sql = "select nome from sprint";

        ResultSet rs = conexao.getStmt().executeQuery(sql);

        while (rs.next()) {


            list.add(rs.getString("nome"));
        }
        conexao.Desconectar();

        return list;
    }

    //nao estou usando ainda
    //pegar o nome da sprint atual(estava com problema em usar a função a cima)
    public String getsprintAtual(Conexao conexao, String ID) throws SQLException {

        String sprint = null;

        conexao.Conectar();

        String sql = "select DISTINCT s.nome from sprint s join historia h on s.id_sprint = h.id_sprint where h.id_historia = "+ID;

        ResultSet rs = conexao.getStmt().executeQuery(sql);

        while (rs.next()) {
            sprint = (rs.getString("nome"));
        }
        conexao.Desconectar();

        return sprint;
    }

    public int atualizasprint(Conexao conexao, String sprint, String ID) throws SQLException {

        int idnovasprint = 0;

        conexao.Conectar();

        //pegar id da sprint que "sprint" que sera feito o update
        String sql = "select id_sprint from sprint where nome = "+"'"+sprint+"'";
        ResultSet rs = conexao.getStmt().executeQuery(sql);

        while (rs.next()) {
            idnovasprint = (rs.getInt("id_sprint"));
        }



        //pegar id da sprint que "sprint" que sera feito o update
        //String sql2 = "update historia set id_sprint = "+idnovasprint+" where id_historia = "+ID  + " RETURNING *";;
        //conexao.getStmt().executeQuery(sql2);

        conexao.Desconectar();

        return idnovasprint;
    }

    private HistoriaDAO mapper(ResultSet rs) throws SQLException {
        HistoriaDAO historiaDAO = new HistoriaDAO();

        historiaDAO.setIdHistoria(rs.getLong("id_historia"));
        historiaDAO.setDescricao(rs.getString("descricao"));
        historiaDAO.setDtAlteracao(rs.getDate("dt_alteracao"));
        historiaDAO.setDtCriacao(rs.getDate("dt_criacao"));
        historiaDAO.setDescricao(rs.getString("descricao"));
        historiaDAO.setValueBusiness(rs.getInt("value_business"));
        historiaDAO.setPontos(rs.getInt("pontos"));
        historiaDAO.setNome(rs.getString("nome"));
        historiaDAO.setIdSprint(rs.getLong("id_sprint"));
        historiaDAO.setStatus(rs.getString("status"));

        return historiaDAO;
    }

    public List<Historias> getHistoriainfo(Conexao conexao, String ID) throws SQLException {
        List<Historias> list = new ArrayList();

        conexao.Conectar();

        String sql = "select * from historia where id_historia = "+ID;

        ResultSet rs = conexao.getStmt().executeQuery(sql);

        while (rs.next()) {
            HistoriaDAO historia = new HistoriaDAO();

            historia.setIdHistoria(rs.getLong("id_historia"));
            historia.setIdSprint(rs.getLong("id_sprint"));
            historia.setStatus(rs.getString("status"));
            historia.setNome(rs.getString("nome"));
            historia.setDescricao(rs.getString("descricao"));
            historia.setValueBusiness(rs.getInt("value_business"));
            historia.setPontos(rs.getInt("pontos"));
            historia.setDtCriacao(rs.getDate("dt_criacao"));
            historia.setDtAlteracao(rs.getDate("dt_alteracao"));

            list.add(toInterface(historia));
        }
        conexao.Desconectar();

        return list;
    }

    //arquivar historia pelo id
    public void deleteHistoria(Conexao conexao, long ID) throws SQLException {

        List<Historias> list = new ArrayList();

        conexao.Conectar();

        String sql = "update historia set ativo = 'F' where id_historia = "+ID;
        ResultSet rs = conexao.getStmt().executeQuery(sql);

        conexao.Desconectar();
    }

    //verifica se o nome que esta pra ser gravado ja não exite
    public boolean historiaJaCadastrado(Conexao conexao, String nome, String ID) throws SQLException {
        long i = 0; //variavel auxiliar
        List<Historias> list = new ArrayList();
        conexao.Conectar();


        String sql = "select * from historia where nome = "+"'"+nome+"'"+ " and ativo = 'T'";

        ResultSet rs = conexao.getStmt().executeQuery(sql);

        conexao.Desconectar();

        while (rs.next()) {
            HistoriaDAO historia = new HistoriaDAO();

            historia.setIdHistoria(rs.getLong("id_historia"));
            historia.setIdSprint(rs.getLong("id_sprint"));
            historia.setStatus(rs.getString("status"));
            historia.setNome(rs.getString("nome"));
            historia.setDescricao(rs.getString("descricao"));
            historia.setValueBusiness(rs.getInt("value_business"));
            historia.setPontos(rs.getInt("pontos"));
            historia.setDtCriacao(rs.getDate("dt_criacao"));
            historia.setDtAlteracao(rs.getDate("dt_alteracao"));

            list.add(toInterface(historia));
        }

        i = list.size();


        //nova historia e primeira historia
        //alterar historia
        //adicionra nova com mesmo nome
        //alterar uma ja criada pra uma ja com mesmo nome


        //nova historia logo se i = 1 e duplicado
        if (ID == null || ID.isEmpty()){
            if (i == 1){
                return true;
            }
        }else{
            if (i > 1){
                return true;
            }

            if (i == 1){  //no caso de alterar uma ja criada para um nome duplicado
                long meuid = list.get(0).getIdhistoria();

                //converter
                Long ID2 = Long.parseLong(ID);
                //-------------------------------------
                if (!(meuid == ID2)){
                  return true;
                }

            }
        }

        return false;
    }

    //pegar todas historia do banco
    public List<Historias> getHistoriaBacklog(Conexao conexao) throws SQLException {
        List<Historias> list = new ArrayList();

        conexao.Conectar();

        String sql = "select * from historia where id_sprint = 1 and ativo = 'T'";

        ResultSet rs = conexao.getStmt().executeQuery(sql);

        while (rs.next()) {
            HistoriaDAO historia = new HistoriaDAO();

            historia.setIdHistoria(rs.getLong("id_historia"));
            historia.setIdSprint(rs.getLong("id_sprint"));
            historia.setStatus(rs.getString("status"));
            historia.setNome(rs.getString("nome"));
            historia.setDescricao(rs.getString("descricao"));
            historia.setValueBusiness(rs.getInt("value_business"));
            historia.setPontos(rs.getInt("pontos"));
            historia.setDtCriacao(rs.getDate("dt_criacao"));
            historia.setDtAlteracao(rs.getDate("dt_alteracao"));

            list.add(toInterface(historia));
        }
        conexao.Desconectar();

        return list;
    }

    private Historias toInterface(HistoriaDAO historiaDAO) {
        Historias historia = new Historias();

        historia.setIdsprint(historiaDAO.getIdSprint());
        historia.setIdhistoria(historiaDAO.getIdHistoria());
        historia.setIdstatus(historiaDAO.getStatus());
        historia.setNomehist(historiaDAO.getNome());
        historia.setDescricao(historiaDAO.getDescricao());
        historia.setValuebusiness(historiaDAO.getValueBusiness());
        historia.setPontos(historiaDAO.getPontos());
        historia.setDtcriacao(historiaDAO.getDtCriacao());
        historia.setDtalteracao(historiaDAO.getDtAlteracao());

        return historia;
    }

}


