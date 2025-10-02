package com.aluguel.carros.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aluguel.carros.model.Contratante;
import com.aluguel.carros.Repository.ContratanteRepository;

@Service
public class ContratanteService {
    @Autowired
    private ContratanteRepository contratanteRepository;

    public ContratanteService(ContratanteRepository contratanteRepository) {
        this.contratanteRepository = contratanteRepository;
    }
    
    public List<Contratante> getAllContratantes() {
        return contratanteRepository.findAll();
    }
    
    public Contratante getContratanteById(Long id) {
        return contratanteRepository.findById(id);
    }
    
    public List<Contratante> getContratanteByCpf(String cpf) {
        return contratanteRepository.findByCpf(cpf);
    }
    
    public Contratante createContratante(Contratante contratante) {
        if (contratante.getId() != null && contratante.getId() != 0) {
            throw new IllegalArgumentException("ID deve ser nulo ou zero para criar um novo contratante.");
        }
        var new_contratante = Contratante.builder()
                .nome(contratante.getNome())
                .cpf(contratante.getCpf())
                .profissao(contratante.getProfissao())
                .entidade(contratante.getEntidade())
                .build();
        return contratanteRepository.save(new_contratante);
    }
    
    public Contratante updateContratante(Contratante contratante) {
        if (contratante.getId() == null || contratante.getId() == 0) {
            throw new IllegalArgumentException("ID deve ser fornecido para atualizar um contratante.");
        }
        if (!contratanteRepository.existsById(contratante.getId())) {
            throw new IllegalArgumentException("Contratante não encontrado com ID: " + contratante.getId());
        }
        contratanteRepository.update(contratante);
        return contratante;
    }
    
    public void deleteContratante(Long id) {
        if (!contratanteRepository.existsById(id)) {
            throw new IllegalArgumentException("Contratante não encontrado com ID: " + id);
        }
        contratanteRepository.deleteById(id);
    }
    
    public boolean existsById(Long id) {
        return contratanteRepository.existsById(id);
    }
}