package com.aluguel.carros.Service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aluguel.carros.Repository.AutomovelRepository;
import com.aluguel.carros.Service.AutomovelService;
import com.aluguel.carros.dto.AutomovelDto;
import com.aluguel.carros.dto.SaveAutomovelRequest;
import com.aluguel.carros.exceptions.AutomovelJaExisteException;
import com.aluguel.carros.model.Automovel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutomovelServiceImpl implements AutomovelService{

    private final AutomovelRepository automovelRepository;

    @Override
    public void criar(SaveAutomovelRequest request) {
        var automovelExists = this.automovelRepository.findByPlaca(request.getPlaca());
        if(automovelExists.isPresent()){
            throw new AutomovelJaExisteException();
        }
        var automovel = Automovel.builder()
        .ano(request.getAno())
        .marca(request.getMarca())
        .modelo(request.getModelo())
        .placa(request.getPlaca())
        .valorMensal(request.getValorMensal())
        .propetario(null) // Será definido posteriormente
        .contratoAluguel(null) // Será definido posteriormente
        .build();
        this.automovelRepository.save(automovel);
    }

    @Override
    public void delete(Long matricula) {
        this.automovelRepository.deleteById(matricula);
    }

    @Override
    public void update(SaveAutomovelRequest request) {
        if (request.getMatricula() == null) {
            throw new RuntimeException("Matrícula é obrigatória para atualização.");
        }
        
        var automovelOptional = this.automovelRepository.findById(request.getMatricula());
        if (automovelOptional.isEmpty()) {
            throw new RuntimeException("Automóvel não encontrado para a matrícula informada.");
        }
        Automovel automovel = automovelOptional.get();

        if (request.getAno() != null) {
            automovel.setAno(request.getAno());
        }
        if (request.getMarca() != null) {
            automovel.setMarca(request.getMarca());
        }
        if (request.getModelo() != null) {
            automovel.setModelo(request.getModelo());
        }
        if (request.getPlaca() != null) {
            automovel.setPlaca(request.getPlaca());
        }

        this.automovelRepository.save(automovel);
    }

    @Override
    public List<AutomovelDto> listarTodos() {
        return this.automovelRepository.findAllDTO();
    }

    @Override
    public Automovel listarAutomovel(Long matricula) {
        return this.automovelRepository.findById(matricula)
            .orElseThrow(() -> new RuntimeException("Automóvel não encontrado"));
    }
    
}
