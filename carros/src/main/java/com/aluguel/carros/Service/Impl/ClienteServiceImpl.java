package com.aluguel.carros.Service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aluguel.carros.Repository.ClienteRepository;
import com.aluguel.carros.Service.ClienteService;
import com.aluguel.carros.config.PasswordEncoderConfiguration;
import com.aluguel.carros.dto.SaveClienteRequest;
import com.aluguel.carros.enums.TipoUsuario;
import com.aluguel.carros.exceptions.ClienteJaExisteException;
import com.aluguel.carros.exceptions.ClienteNaoExisteException;
import com.aluguel.carros.model.Cliente;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository repository;
    private final PasswordEncoderConfiguration passwordEncoder;

    @Override
    @Transactional
    public void criar(SaveClienteRequest request) {
        var clienteExists = this.repository.findClienteByCpf(request.getCpf());
        if(clienteExists.isPresent()){
            throw new ClienteJaExisteException();
        }
        var hash_password = passwordEncoder.passwordEncoder().encode(request.getSenha());
        Cliente cliente = new Cliente(
            request.getEmail(), 
            hash_password, 
            request.getRegistro(), 
            request.getCpf(), 
            request.getNome(), 
            request.getProfissao(), 
            request.getEmpregadores()
        );
        cliente.setRole(TipoUsuario.CLIENTE);
        this.repository.save(cliente);
    }

    @Override
    public Cliente listarCliente(String cpf) {
        Optional<Cliente> cliente = this.repository.findClienteByCpf(cpf);
        return cliente.get();
    }

    @Override
    public List<Cliente> listarTodos() {
        return this.repository.findAll();
    }

    @Override
    @Transactional
    public void atualizar(SaveClienteRequest request) {
        var clienteExists = this.repository.findClienteByCpf(request.getCpf());
        if(clienteExists.isEmpty()){
            throw new ClienteNaoExisteException();
        }
        Cliente cliente = clienteExists.get();
        cliente.setEmail(request.getEmail());
        String novaSenha = request.getSenha();
        if (novaSenha != null && !novaSenha.isBlank()) {
            String hash_password = passwordEncoder.passwordEncoder().encode(novaSenha);
            cliente.setSenha(hash_password);
        }
        cliente.setNome(request.getNome());
        cliente.setRegistro(request.getRegistro());
        cliente.setCpf(request.getCpf());
        cliente.setProfissao(request.getProfissao());
        cliente.setEmpregadores(request.getEmpregadores());
        this.repository.save(cliente);
    }

    @Override
    public void deletar(UUID id) {
        var clienteExists = this.repository.findById(id);
        if(clienteExists.isEmpty()){
            throw new ClienteNaoExisteException();
        }
        this.repository.deleteById(id);
    }
    
}
