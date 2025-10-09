package com.aluguel.carros.Service.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aluguel.carros.Repository.AutomovelRepository;
import com.aluguel.carros.Repository.ClienteRepository;
import com.aluguel.carros.Repository.PedidoAluguelRepository;
import com.aluguel.carros.Service.PedidoAluguelService;
import com.aluguel.carros.dto.CriarPedidoRequest;
import com.aluguel.carros.dto.PedidoAluguelDto;
import com.aluguel.carros.enums.StatusPedido;
import com.aluguel.carros.model.PedidoAluguel;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoAluguelServiceImpl implements PedidoAluguelService{
    private final PedidoAluguelRepository repository;
    private final AutomovelRepository automovelRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public void criar(CriarPedidoRequest request) {
        var automovel = this.automovelRepository.findByPlaca(request.placaVeiculo()).orElseThrow();
        var cliente = this.clienteRepository.findByEmail(request.emailCliente()).orElseThrow();

        var pedido = PedidoAluguel.builder()
            .automovelDesejado(automovel) 
            .dataFim(request.dataFim())
            .dataInicio(request.dataInicio())
            .avaliacao(null)
            .clienteSolicitante(cliente)
            .dataSolicitacao(LocalDate.now())
            .status(StatusPedido.ANALISE)
            .build();
        
        this.repository.save(pedido);
    }

    @Override
    public void deletar(UUID id) {
        this.repository.deleteById(id);
    }

    @Override
    public List<PedidoAluguelDto> listarTodos() {
        var lista = toDtoList(this.repository.findAll());
        return lista;
    }

    @Override
    public PedidoAluguel listarPedido(UUID id) {
        return this.repository.findById(id).orElseThrow();
    }

    @Override
    public List<PedidoAluguel> listarPorStatus(String statusPedido) {
        StatusPedido status = StatusPedido.valueOf(statusPedido.toUpperCase());
        return this.repository.findByStatus(status);
    }

    private List<PedidoAluguelDto> toDtoList(List<PedidoAluguel> pedidos) {
        if (pedidos == null) return java.util.Collections.emptyList();
        return pedidos.stream()
            .map(this::toDto)
            .toList();
    }
    private PedidoAluguelDto toDto(PedidoAluguel pedido) {
        if (pedido == null) return null;

        // Converter Cliente para ClienteDto
        var cliente = pedido.getClienteSolicitante();
        var clienteDto = cliente == null ? null :
            new com.aluguel.carros.dto.ClienteDto(
                cliente.getEmail(),
                cliente.getCpf(),
                cliente.getNome()
            );

        // Converter Automovel para AutomovelDto
        var automovel = pedido.getAutomovelDesejado();
        var automovelDto = automovel == null ? null :
            new com.aluguel.carros.dto.AutomovelDto(
                automovel.getMatricula(),
                automovel.getMarca(),
                automovel.getModelo(),
                automovel.getAno(),
                automovel.getPlaca(),
                automovel.getValorMensal()
            );

        //      Long matricula,
        // String marca,
        // String modelo,
        // Integer ano,
        // String placa,
        // Double precoMensal

        // Valor mensal e crédito associado não existem diretamente em PedidoAluguel,
        // então você pode ajustar conforme sua modelagem.
        double valorMensal = 0.0;
        boolean creditoAssociado = false;

        return new PedidoAluguelDto(
            pedido.getId() != null ? pedido.getId().toString() : null,
            pedido.getStatus(),
            clienteDto,
            automovelDto,
            pedido.getDataInicio(),
            pedido.getDataFim(),
            valorMensal,
            creditoAssociado,
            pedido.getDataSolicitacao()
        );
    }
}
