package com.aluguel.carros.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SaveAutomovelRequest {
    private Integer ano;
    private String marca;
    private String modelo;
    private BigDecimal valorMensal;
    private String placa;
    private Long matricula; // Para atualização
}
