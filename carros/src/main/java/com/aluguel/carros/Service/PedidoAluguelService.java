package com.aluguel.carros.Service;

import java.util.List;
import java.util.UUID;

import com.aluguel.carros.dto.CriarPedidoRequest;
import com.aluguel.carros.dto.PedidoAluguelDto;
import com.aluguel.carros.model.PedidoAluguel;

public interface PedidoAluguelService {
    void criar(CriarPedidoRequest request);
    void deletar(UUID id);
    List<PedidoAluguelDto> listarTodos();
    PedidoAluguel listarPedido(UUID id);
    List<PedidoAluguel> listarPorStatus(String statusPedido);
}
