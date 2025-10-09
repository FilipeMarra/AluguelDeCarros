package com.aluguel.carros.Service.Impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aluguel.carros.Repository.AgenteRepository;
import com.aluguel.carros.Service.AgenteService;
import com.aluguel.carros.config.PasswordEncoderConfiguration;
import com.aluguel.carros.dto.SaveAgenteRequest;
import com.aluguel.carros.enums.TipoUsuario;
import com.aluguel.carros.model.Agente;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgenteServiceImpl implements AgenteService{

    private final AgenteRepository repository;
    private final PasswordEncoderConfiguration passwordEncoderConfiguration;

    @Override
    @Transactional
    public void criar(SaveAgenteRequest request) {
        var agentExists = this.repository.findAgenteByEmail(request.getEmail());
        if(agentExists.isPresent()){

        }
        var hash_password = this.passwordEncoderConfiguration.passwordEncoder().encode(request.getSenha());
        var agente = new Agente(
            request.getEmail(),
            hash_password,
            request.getNome(),
            request.getTipoAgente()
        );
        agente.setRole(TipoUsuario.AGENTE);
        this.repository.save(agente);
    }

    @Override
    public List<Agente> listarTodos() {
        return this.repository.findAll();
    }

    @Override
    public Agente listarAgente(String email) {
        return this.repository.findAgenteByEmail(email).orElseThrow();
    }

    @Override
    public void delete(UUID id) {
        this.repository.deleteById(id);
    }

    @Override
    @Transactional
    public void atualizar(SaveAgenteRequest request) {
        var agenteOptional = this.repository.findAgenteByEmail(request.getEmail());
        if (agenteOptional.isEmpty()) {
            throw new RuntimeException("Agente não encontrado para o email informado.");
        }
        Agente agente = agenteOptional.get();

        String novaSenha = request.getSenha();
        if (novaSenha != null && !novaSenha.isBlank()) {
            String hashPassword = passwordEncoderConfiguration.passwordEncoder().encode(novaSenha);
            agente.setSenha(hashPassword);
        }

        agente.setNome(request.getNome());
        agente.setTipoAgente(request.getTipoAgente());

        this.repository.save(agente);
    }
    
}
