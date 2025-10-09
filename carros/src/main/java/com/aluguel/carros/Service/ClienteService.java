package com.aluguel.carros.Service;

import java.util.List;
import java.util.UUID;

import com.aluguel.carros.dto.SaveClienteRequest;
import com.aluguel.carros.model.Cliente;

public interface ClienteService {
    void criar(SaveClienteRequest request);
    Cliente listarCliente(String cpf);
    List<Cliente> listarTodos();
    void atualizar(SaveClienteRequest request);
    void deletar(UUID id);
}
