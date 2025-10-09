package com.aluguel.carros.dto;

import java.time.LocalDate;

import com.aluguel.carros.enums.StatusPedido;

public record PedidoAluguelDto(
        String id,
        StatusPedido status,
        ClienteDto clienteSolicitante,
        AutomovelDto automovel,
        LocalDate dataInicio,
        LocalDate dataFim,
        double valorMensal,
        boolean creditoAssociado,
        LocalDate dataSolicitacao) {
}
