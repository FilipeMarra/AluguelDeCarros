package com.aluguel.carros.dto;

import java.math.BigDecimal;
import java.util.List;

import com.aluguel.carros.model.Empregador;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SaveClienteRequest {
    private String email;
    private String senha;
    private String nome;
    private String registro;
    private String cpf;
    private String profissao;
    private BigDecimal rendimentoEmpregadores;
    private List<Empregador> empregadores;
}
