package com.aluguel.carros.Controller;

import java.util.List;

import com.aluguel.carros.Service.ContratanteService;
import com.aluguel.carros.model.Contratante;
import com.aluguel.carros.dto.ContratanteRequestDTO;
import com.aluguel.carros.dto.ContratanteResponseDTO;
import com.aluguel.carros.dto.ContratanteUpdateDTO;
import com.aluguel.carros.dto.ContratanteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("/contratante")
@RestController
public class ContratanteController {
    @Autowired
    private ContratanteService contratanteService;

    public ContratanteController(ContratanteService contratanteService) {
        this.contratanteService = contratanteService;
    }
    
    @GetMapping()
    public ResponseEntity<List<ContratanteResponseDTO>> getAllContratantes() {
        try {
            List<Contratante> contratantes = contratanteService.getAllContratantes();
            List<ContratanteResponseDTO> response = ContratanteMapper.toResponseDTOList(contratantes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ContratanteResponseDTO> getContratanteById(@PathVariable Long id) {
        try {
            Contratante contratante = contratanteService.getContratanteById(id);
            if (contratante == null) {
                return ResponseEntity.notFound().build();
            }
            ContratanteResponseDTO response = ContratanteMapper.toResponseDTO(contratante);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/cpf")
    public ResponseEntity<List<ContratanteResponseDTO>> getContratanteByCpf(@RequestParam String cpf) {
        try {
            List<Contratante> contratantes = contratanteService.getContratanteByCpf(cpf);
            List<ContratanteResponseDTO> response = ContratanteMapper.toResponseDTOList(contratantes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> createContratante(@RequestBody ContratanteRequestDTO requestDTO) {
        try {
            Contratante contratante = ContratanteMapper.toEntity(requestDTO);
            Contratante savedContratante = contratanteService.createContratante(contratante);
            ContratanteResponseDTO response = ContratanteMapper.toResponseDTO(savedContratante);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateContratante(@PathVariable Long id, @RequestBody ContratanteUpdateDTO updateDTO) {
        try {
            updateDTO.setId(id);
            Contratante contratante = ContratanteMapper.toEntity(updateDTO);
            Contratante updatedContratante = contratanteService.updateContratante(contratante);
            ContratanteResponseDTO response = ContratanteMapper.toResponseDTO(updatedContratante);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContratante(@PathVariable Long id) {
        try {
            contratanteService.deleteContratante(id);
            return ResponseEntity.ok().body("Contratante deletado com sucesso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }
}