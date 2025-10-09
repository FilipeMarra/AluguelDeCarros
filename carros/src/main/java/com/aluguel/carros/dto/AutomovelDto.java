package com.aluguel.carros.dto;

import java.math.BigDecimal;

public record AutomovelDto(
        Long matricula,
        String marca,
        String modelo,
        Integer ano,
        String placa,
        BigDecimal precoMensal) {
}
