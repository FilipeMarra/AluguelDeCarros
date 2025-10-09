package com.aluguel.carros.Controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aluguel.carros.Service.Impl.ContratoAluguelServiceImpl;
import com.aluguel.carros.dto.ContratoAluguelDto;
import com.aluguel.carros.model.ContratoAluguel;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/contrato")
@RequiredArgsConstructor
public class ContratoAluguelController {
    
    private final ContratoAluguelServiceImpl service;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody ContratoAluguelDto request){
        try {
            this.service.criar(request);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/cliente")
    public ResponseEntity<ContratoAluguel> listarContratoPeloCliente(@RequestBody String emailCliente){
        try {
            var contrato = this.service.listarContratoCliente(emailCliente);
            return ResponseEntity.ok(contrato);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ContratoAluguel>> listarTodos(){
        var lista = this.service.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Void> atualizar(@RequestBody ContratoAluguelDto request){
        try {
            this.service.atualizar(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
