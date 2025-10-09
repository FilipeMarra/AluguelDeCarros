package com.aluguel.carros.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aluguel.carros.Service.Impl.AutomovelServiceImpl;
import com.aluguel.carros.dto.AutomovelDto;
import com.aluguel.carros.dto.SaveAutomovelRequest;
import com.aluguel.carros.model.Automovel;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/automovel")
@RequiredArgsConstructor
public class AutomovelController {
    private final AutomovelServiceImpl service;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody SaveAutomovelRequest request) {
        service.criar(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> deletar(@PathVariable Long matricula) {
        service.delete(matricula);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizar(@RequestBody SaveAutomovelRequest request) {
        service.update(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AutomovelDto>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<Automovel> listarAutomovel(@PathVariable Long matricula) {
        return ResponseEntity.ok(service.listarAutomovel(matricula));
    }
}
