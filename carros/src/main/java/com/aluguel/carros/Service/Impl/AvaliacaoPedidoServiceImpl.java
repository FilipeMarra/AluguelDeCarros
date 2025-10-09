package com.aluguel.carros.Service.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aluguel.carros.Repository.AgenteRepository;
import com.aluguel.carros.Repository.AvaliacaoPedidoRepository;
import com.aluguel.carros.Repository.PedidoAluguelRepository;
import com.aluguel.carros.Service.AvaliacaoPedidoService;
import com.aluguel.carros.dto.AvaliacaPedidoDto;
import com.aluguel.carros.enums.StatusPedido;
import com.aluguel.carros.model.AvaliacaoPedido;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliacaoPedidoServiceImpl implements AvaliacaoPedidoService{

    private final AvaliacaoPedidoRepository avaliacaoRepository;
    private final PedidoAluguelRepository pedidoRepository;
    private final AgenteRepository agenteRepository;

    @Override
    @Transactional
    public void criar(AvaliacaPedidoDto request) {
        var pedido = pedidoRepository.findById(request.idPedido()).orElseThrow();
        var agente = agenteRepository.findAgenteByEmail(request.emailAgente()).orElseThrow();
        var avaliacao = AvaliacaoPedido.builder()
            .agenteAvaliador(agente)
            .pedidoAvaliado(pedido)
            .parecer(request.parecer())
            .data(LocalDate.now())
            .build();
        pedido.setAvaliacao(avaliacao);
        pedido.setStatus(StatusPedido.valueOf(request.parecer()));
        this.avaliacaoRepository.save(avaliacao);
    }

    @Override
    @Transactional
    public void atualizar(AvaliacaPedidoDto request) {
        var pedido = this.pedidoRepository.findById(request.idPedido()).orElseThrow();
        var avaliacao = this.avaliacaoRepository.findById(request.idAvaliacao()).orElseThrow();
        avaliacao.setParecer(request.parecer());
        pedido.setStatus(StatusPedido.valueOf(avaliacao.getParecer()));
    }

    @Override
    public List<AvaliacaoPedido> listarTodos() {
        return this.avaliacaoRepository.findAll();
    }

    @Override
    public AvaliacaoPedido listarAvaliacao(UUID id) {
        return this.avaliacaoRepository.findById(id).orElseThrow();
    }
    
}
