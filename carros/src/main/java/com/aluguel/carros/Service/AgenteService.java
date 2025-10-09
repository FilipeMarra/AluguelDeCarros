package com.aluguel.carros.Service;

import java.util.List;
import java.util.UUID;

import com.aluguel.carros.dto.SaveAgenteRequest;
import com.aluguel.carros.model.Agente;

public interface AgenteService {
    void criar(SaveAgenteRequest request);
    List<Agente> listarTodos();
    Agente listarAgente(String email);
    void delete(UUID id);
    void atualizar(SaveAgenteRequest request);
}
