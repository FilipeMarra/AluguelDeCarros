package com.aluguel.carros.Service;

import java.util.List;

import com.aluguel.carros.dto.AutomovelDto;
import com.aluguel.carros.dto.SaveAutomovelRequest;
import com.aluguel.carros.model.Automovel;

public interface AutomovelService {
    void criar(SaveAutomovelRequest request);
    void delete(Long matricula);
    void update(SaveAutomovelRequest request);
    List<AutomovelDto> listarTodos();
    Automovel listarAutomovel(Long matricula);
}
