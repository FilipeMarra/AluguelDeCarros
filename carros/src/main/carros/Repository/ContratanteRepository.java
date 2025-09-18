package com.aluguel.carros.Repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import main.java.com.aluguel.carros.model.Contratante;

import org.springframework.jdbc.core.RowMapper;

@Repository
public class ContratanteRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ContratanteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Contratante> findAll() {
        String sql = "SELECT * FROM contratante"; // Consulta SQL para selecionar todos os contratantes
        return jdbcTemplate.query(sql, new RowMapper<Contratante>() {
            @Override
            public Contratante mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                Contratante contratante = new Contratante();
                contratante.setId(rs.getInt("id"));
                contratante.setNome(rs.getString("nome"));
                contratante.setCpf(rs.getString("cpf"));
                contratante.setEntidade(rs.getString("entidade"));
                contratante.setProfissao(rs.getString("profissao"));
                return contratante;
            }
        });
    }

    public Contratante findById(int id) {
        String sql = "SELECT * FROM contratante WHERE id = ?"; // Consulta SQL para selecionar um contratante pelo ID
        return jdbcTemplate.queryForObject(sql, new Object[] { id }, new RowMapper<Contratante>() {
            @Override
            public Contratante mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                Contratante contratante = new Contratante();
                contratante.setId(rs.getInt("id"));
                contratante.setNome(rs.getString("nome"));
                contratante.setCpf(rs.getString("cpf"));
                contratante.setEntidade(rs.getString("entidade"));
                contratante.setProfissao(rs.getString("profissao"));
                return contratante;
            }
        });
    }

    public List<Contratante> findByCpf(String cpf) {
        String sql = "SELECT * FROM contratante WHERE cpf = ?"; // Consulta SQL para selecionar contratantes pelo CPF
        return jdbcTemplate.query(sql, new Object[] { cpf }, new RowMapper<Contratante>() {
            @Override
            public Contratante mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                Contratante contratante = new Contratante();
                contratante.setId(rs.getInt("id"));
                contratante.setNome(rs.getString("nome"));
                contratante.setCpf(rs.getString("cpf"));
                contratante.setEntidade(rs.getString("entidade"));
                contratante.setProfissao(rs.getString("profissao"));
                return contratante;
            }
        });
    }

    public Contratante save(Contratante contratante) {
        String sql = "INSERT INTO contratante (nome, cpf, entidade, profissao) VALUES (?, ?, ?, ?)"; // Consulta SQL
                                                                                                     // para inserir um
                                                                                                     // novo contratante
        jdbcTemplate.update(sql, contratante.getNome(), contratante.getCpf(), contratante.getEntidade(),
                contratante.getProfissao());
        return contratante; // Retorna o contratante inserido
    }

    public void update(Contratante contratante) {
        String sql = "UPDATE contratante SET nome = ?, cpf = ?, entidade = ?, profissao = ? WHERE id = ?"; // Consulta
                                                                                                           // SQL para
                                                                                                           // atualizar
                                                                                                           // um
                                                                                                           // contratante
                                                                                                           // existente
        jdbcTemplate.update(sql, contratante.getNome(), contratante.getCpf(), contratante.getEntidade(),
                contratante.getProfissao(), contratante.getId());
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM contratante WHERE id = ?"; // Consulta SQL para excluir um contratante pelo ID
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(int id) {
        String sql = "SELECT COUNT(*) FROM contratante WHERE id = ?"; // Consulta SQL para verificar se um contratante existe pelo ID
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0; // Retorna verdadeiro se o contratante existir
    }
}