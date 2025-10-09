package com.aluguel.carros.Controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aluguel.carros.Service.Impl.AvaliacaoPedidoServiceImpl;
import com.aluguel.carros.dto.AvaliacaPedidoDto;
import com.aluguel.carros.model.AvaliacaoPedido;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/avaliacao")
@RequiredArgsConstructor
public class AvaliacaoPedidoController {

    private final AvaliacaoPedidoServiceImpl service;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody AvaliacaPedidoDto request){
        try {
            this.service.criar(request);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AvaliacaoPedido>> listarTodos(){
        var lista = this.service.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoPedido> listar(@PathVariable UUID id){
        try {
            var avaliacao = this.service.listarAvaliacao(id);
            return ResponseEntity.ok(avaliacao);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Void> atualizar(@RequestBody AvaliacaPedidoDto request){
        try {
            this.service.atualizar(request);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
