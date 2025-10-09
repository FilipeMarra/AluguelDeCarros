package com.aluguel.carros.Service;

import java.util.List;
import java.util.UUID;

import com.aluguel.carros.dto.EmpregadorDto;
import com.aluguel.carros.model.Empregador;

public interface EmpregadorService {
    void criar(EmpregadorDto request);
    Empregador listarEmpregador(String cpf);
    List<Empregador> listarTodos();
    void delete(UUID id);
    void atualizar(EmpregadorDto request);
}
