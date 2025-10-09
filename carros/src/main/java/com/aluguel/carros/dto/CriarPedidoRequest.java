package com.aluguel.carros.dto;

import java.time.LocalDate;

public record CriarPedidoRequest(
    String placaVeiculo,
    LocalDate dataInicio,
    LocalDate dataFim,
    Double valorMensal,
    String emailCliente
) {}

