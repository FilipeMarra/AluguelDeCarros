package com.aluguel.carros.Controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aluguel.carros.Service.Impl.PedidoAluguelServiceImpl;
import com.aluguel.carros.dto.CriarPedidoRequest;
import com.aluguel.carros.dto.PedidoAluguelDto;
import com.aluguel.carros.model.PedidoAluguel;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/pedido")
@RequiredArgsConstructor
public class PedidoAluguelController {
    private final PedidoAluguelServiceImpl service;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody CriarPedidoRequest request){
        try {
            this.service.criar(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<PedidoAluguelDto>> listarTodos(){
        try {
            var lista = this.service.listarTodos();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoAluguel> listarPedido(@PathVariable UUID id){
        try {
            var pedido = this.service.listarPedido(id);
            return ResponseEntity.ok(pedido);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/status")
    public ResponseEntity<List<PedidoAluguel>> listarPorStatus(@RequestParam String status){
        try {
            var list = this.service.listarPorStatus(status);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id){
        try {
            this.service.deletar(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
