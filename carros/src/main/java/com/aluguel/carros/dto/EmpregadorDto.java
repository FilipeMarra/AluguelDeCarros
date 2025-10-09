package com.aluguel.carros.dto;

import java.math.BigDecimal;

public record EmpregadorDto(String nome, String cpf, String emailCliente, BigDecimal rendimento) {
    
}
