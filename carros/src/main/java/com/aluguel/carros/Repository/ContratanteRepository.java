package com.aluguel.carros.Repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.aluguel.carros.model.Contratante;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

@Repository
public class ContratanteRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ContratanteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Contratante> findAll() {
        String sql = "SELECT * FROM tb_contratante"; // Consulta SQL para selecionar todos os contratantes
        return jdbcTemplate.query(sql, new RowMapper<Contratante>() {
            @Override
            public Contratante mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                Contratante contratante = new Contratante();
                contratante.setId(rs.getLong("id"));
                contratante.setNome(rs.getString("nome"));
                contratante.setCpf(rs.getString("cpf"));
                contratante.setEntidade(rs.getString("entidade"));
                contratante.setProfissao(rs.getString("profissao"));
                return contratante;
            }
        });
    }

    public Contratante findById(Long id) {
        String sql = "SELECT * FROM tb_contratante WHERE id = ?"; // Consulta SQL para selecionar um contratante pelo ID
        return jdbcTemplate.queryForObject(sql, new Object[] { id }, new RowMapper<Contratante>() {
            @Override
            public Contratante mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                Contratante contratante = new Contratante();
                contratante.setId(rs.getLong("id"));
                contratante.setNome(rs.getString("nome"));
                contratante.setCpf(rs.getString("cpf"));
                contratante.setEntidade(rs.getString("entidade"));
                contratante.setProfissao(rs.getString("profissao"));
                return contratante;
            }
        });
    }

    public List<Contratante> findByCpf(String cpf) {
        String sql = "SELECT * FROM tb_contratante WHERE cpf = ?"; // Consulta SQL para selecionar contratantes pelo CPF
        return jdbcTemplate.query(sql, new Object[] { cpf }, new RowMapper<Contratante>() {
            @Override
            public Contratante mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
                Contratante contratante = new Contratante();
                contratante.setId(rs.getLong("id"));
                contratante.setNome(rs.getString("nome"));
                contratante.setCpf(rs.getString("cpf"));
                contratante.setEntidade(rs.getString("entidade"));
                contratante.setProfissao(rs.getString("profissao"));
                return contratante;
            }
        });
    }

    public Contratante save(Contratante contratante) {
        String sql = "INSERT INTO tb_contratante (nome, cpf, entidade, profissao) VALUES (?, ?, ?, ?)"; // Consulta SQL
                                                                                                     // para inserir um
                                                                                                     // novo contratante
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, contratante.getNome());
            ps.setString(2, contratante.getCpf());
            ps.setString(3, contratante.getEntidade());
            ps.setString(4, contratante.getProfissao());
            return ps;
        }, keyHolder);
        
        // Set the generated ID back to the contratante object
        if (keyHolder.getKey() != null) {
            contratante.setId(keyHolder.getKey().longValue());
        }
        
        return contratante; // Retorna o contratante inserido com ID gerado
    }

    public void update(Contratante contratante) {
        String sql = "UPDATE tb_contratante SET nome = ?, cpf = ?, entidade = ?, profissao = ? WHERE id = ?"; // Consulta
                                                                                                           // SQL para
                                                                                                           // atualizar
                                                                                                           // um
                                                                                                           // contratante
                                                                                                           // existente
        jdbcTemplate.update(sql, contratante.getNome(), contratante.getCpf(), contratante.getEntidade(),
                contratante.getProfissao(), contratante.getId());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM tb_contratante WHERE id = ?"; // Consulta SQL para excluir um contratante pelo ID
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM tb_contratante WHERE id = ?"; // Consulta SQL para verificar se um contratante existe pelo ID
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0; // Retorna verdadeiro se o contratante existir
    }
}