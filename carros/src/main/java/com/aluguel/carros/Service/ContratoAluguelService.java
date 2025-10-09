package com.aluguel.carros.Service;

import java.util.List;

import com.aluguel.carros.dto.ContratoAluguelDto;
import com.aluguel.carros.model.ContratoAluguel;

public interface ContratoAluguelService {
    void criar(ContratoAluguelDto request);
    void atualizar(ContratoAluguelDto request);
    List<ContratoAluguel> listarTodos();
    ContratoAluguel listarContratoCliente(String email);
}
