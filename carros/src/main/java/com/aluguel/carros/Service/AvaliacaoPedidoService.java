package com.aluguel.carros.Service;

import java.util.List;
import java.util.UUID;

import com.aluguel.carros.dto.AvaliacaPedidoDto;
import com.aluguel.carros.model.AvaliacaoPedido;

public interface AvaliacaoPedidoService {
    void criar(AvaliacaPedidoDto request);
    void atualizar(AvaliacaPedidoDto request);
    List<AvaliacaoPedido> listarTodos();
    AvaliacaoPedido listarAvaliacao(UUID id);
}
