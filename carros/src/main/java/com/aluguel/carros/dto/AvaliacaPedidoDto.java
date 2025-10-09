package com.aluguel.carros.dto;

import java.time.LocalDate;
import java.util.UUID;

public record AvaliacaPedidoDto(UUID idAvaliacao,UUID idPedido, String emailAgente, LocalDate data, String parecer) {
    
}