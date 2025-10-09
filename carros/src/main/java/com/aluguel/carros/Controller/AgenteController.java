package com.aluguel.carros.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aluguel.carros.Service.Impl.AgenteServiceImpl;
import com.aluguel.carros.dto.SaveAgenteRequest;
import com.aluguel.carros.model.Agente;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/agente")
@RequiredArgsConstructor
public class AgenteController {
    
    private final AgenteServiceImpl agenteService;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody SaveAgenteRequest request) {
        try {
            agenteService.criar(request);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Agente>> listarTodos() {
        return ResponseEntity.ok(agenteService.listarTodos());
    }

    @GetMapping("/{email}")
    public ResponseEntity<Agente> listarAgente(@PathVariable String email) {
        try {
            return ResponseEntity.ok(agenteService.listarAgente(email));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable java.util.UUID id) {
        try {
            agenteService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/atualizar")
    public ResponseEntity<Void> atualizar(@RequestBody SaveAgenteRequest request) {
        try {
            agenteService.atualizar(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
