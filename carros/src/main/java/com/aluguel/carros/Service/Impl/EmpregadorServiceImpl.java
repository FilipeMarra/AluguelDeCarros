package com.aluguel.carros.Service.Impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aluguel.carros.Repository.ClienteRepository;
import com.aluguel.carros.Repository.EmpregadorRepository;
import com.aluguel.carros.Service.EmpregadorService;
import com.aluguel.carros.dto.EmpregadorDto;
import com.aluguel.carros.exceptions.EmpregadorJaExisteException;
import com.aluguel.carros.model.Empregador;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpregadorServiceImpl implements EmpregadorService{

    private final EmpregadorRepository repository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public void criar(EmpregadorDto request) {
        Optional<Empregador> empregadorExist = this.repository.findByCpf(request.cpf());
        var cliente = this.clienteRepository.findByEmail(request.emailCliente()).orElseThrow();
        if(empregadorExist.isPresent()){
            throw new EmpregadorJaExisteException();
        }
        var empregador = Empregador.builder()
            .clienteAssociado(cliente)
            .cpf(request.cpf())
            .nome(request.nome())
            .rendimento(request.rendimento())
            .build();

        this.repository.save(empregador);
    }

    @Override
    public Empregador listarEmpregador(String cpf) {
        return this.repository.findByCpf(cpf).get();
    }

    @Override
    public List<Empregador> listarTodos() {
       return this.repository.findAll();
    }

    @Override
    public void delete(UUID id) {
        var empregadorExists = this.repository.findById(id).orElseThrow();
        this.repository.deleteById(empregadorExists.getId());
    }

    @Override
    @Transactional
    public void atualizar(EmpregadorDto request) {
        Optional<Empregador> empregadorExist = this.repository.findByCpf(request.cpf());
        var cliente = this.clienteRepository.findByEmail(request.emailCliente()).orElseThrow();
        if(empregadorExist.isEmpty()){
            throw new NoSuchElementException("Empregador não encontrado para o CPF informado.");
        }
        Empregador empregador = empregadorExist.get();
        empregador.setNome(request.nome());
        empregador.setRendimento(request.rendimento());
        empregador.setClienteAssociado(cliente);
        this.repository.save(empregador);
    }
    
}
