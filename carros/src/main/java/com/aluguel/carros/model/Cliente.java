package com.aluguel.carros.model;

import java.util.List;
import com.aluguel.carros.enums.TipoUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tb_usuario_cliente")
@Getter
@Setter
@NoArgsConstructor
public class Cliente extends Usuario {

    @Column(nullable = false, unique = true)
    private String registro;
    
    @Column(nullable = false, unique = true)
    private String cpf;
    
    @Column(nullable = false, length = 30)
    private String nome;

    @Column(nullable = false, length = 30)
    private String profissao;

    @Column(nullable = true)
    @OneToMany(mappedBy = "clienteAssociado")
    private List<Empregador> empregadores;

    @OneToOne(mappedBy = "clienteContratante")
    private ContratoAluguel contratoAluguel;

    public Cliente(String email, String senha, String regis, String cpf, String nome,String profissao, List<Empregador> empregadores) {
        super(TipoUsuario.CLIENTE, email, senha);
        this.registro = regis;
        this.cpf = cpf;
        this.nome = nome;
        this.profissao = profissao;
        this.empregadores = empregadores;
    }
}
