package com.aluguel.carros.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ContratanteRequestDTO {
    private String nome;
    private String cpf;
    private String profissao;
    private String entidade;

    public ContratanteRequestDTO() {}

    public ContratanteRequestDTO(String nome, String cpf, String profissao, String entidade) {
        this.nome = nome;
        this.cpf = cpf;
        this.profissao = profissao;
        this.entidade = entidade;
    }


}