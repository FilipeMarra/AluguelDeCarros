package com.aluguel.carros.Service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aluguel.carros.Repository.AutomovelRepository;
import com.aluguel.carros.Repository.ClienteRepository;
import com.aluguel.carros.Repository.ContratoAluguelRepository;
import com.aluguel.carros.Service.ContratoAluguelService;
import com.aluguel.carros.dto.ContratoAluguelDto;
import com.aluguel.carros.model.ContratoAluguel;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContratoAluguelServiceImpl implements ContratoAluguelService{
    
    private final ContratoAluguelRepository contratoRepository;
    private final AutomovelRepository automovelRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public void criar(ContratoAluguelDto request) {
       var automovel = this.automovelRepository.findByPlaca(request.placa()).orElseThrow();
       var cliente = this.clienteRepository.findByEmail(request.emailCliente()).orElseThrow();
       var contrato = ContratoAluguel.builder()
        .automovel(automovel)
        .clienteContratante(cliente)
        .dataInicio(LocalDate.now())
        .datafim(request.datafim())
        .bancoFinanciador(request.bancoFinanciador())
        .build();

       this.contratoRepository.save(contrato);
    }

    @Override
    @Transactional
    public void atualizar(ContratoAluguelDto request) {
        var cliente = this.clienteRepository.findByEmail(request.emailCliente()).orElseThrow();
        var contrato = cliente.getContratoAluguel();
        if (contrato == null) {
            throw new RuntimeException("Contrato de aluguel não encontrado para o cliente informado.");
        }

        if (request.placa() != null && !request.placa().isBlank()) {
            var automovel = this.automovelRepository.findByPlaca(request.placa()).orElseThrow();
            contrato.setAutomovel(automovel);
        }

        if (request.datafim() != null) {
            contrato.setDatafim(request.datafim());
        }

        if (request.bancoFinanciador() != null && !request.bancoFinanciador().isBlank()) {
            contrato.setBancoFinanciador(request.bancoFinanciador());
        }

        this.contratoRepository.save(contrato);
    }

    @Override
    public List<ContratoAluguel> listarTodos() {
        return this.contratoRepository.findAll();
    }

    @Override
    public ContratoAluguel listarContratoCliente(String email) {
        var cliente = this.clienteRepository.findByEmail(email).orElseThrow();
        return cliente.getContratoAluguel();
    }

}
