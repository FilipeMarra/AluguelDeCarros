package com.aluguel.carros.model;

import com.aluguel.carros.enums.TipoAgente;
import com.aluguel.carros.enums.TipoUsuario;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tb_usuario_agente")
@Getter
@Setter
@NoArgsConstructor
public class Agente extends Usuario{

    private String nome;
    private TipoAgente tipoAgente;

    public Agente(String email, String senha, String nome, TipoAgente tipoAgente) {
        super(TipoUsuario.AGENTE, email, senha);
        this.nome = nome;
        this.tipoAgente = tipoAgente;
    }
    
}
