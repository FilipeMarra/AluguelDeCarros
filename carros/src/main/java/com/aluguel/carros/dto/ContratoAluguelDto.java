package com.aluguel.carros.dto;

import java.time.LocalDate;

public record ContratoAluguelDto(LocalDate dataInicio, LocalDate datafim, String emailCliente, String placa, String bancoFinanciador) {
    
}
