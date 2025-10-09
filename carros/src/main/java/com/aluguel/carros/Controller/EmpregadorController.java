package com.aluguel.carros.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aluguel.carros.Service.Impl.EmpregadorServiceImpl;
import com.aluguel.carros.dto.EmpregadorDto;
import com.aluguel.carros.exceptions.EmpregadorJaExisteException;
import com.aluguel.carros.model.Empregador;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/empregador")
@RequiredArgsConstructor
public class EmpregadorController {
    private final EmpregadorServiceImpl service;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody EmpregadorDto request){ 
        try {
            this.service.criar(request);
            return ResponseEntity.ok().build();
        } catch (EmpregadorJaExisteException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Empregador>> listarTodos(){
        var lista = this.service.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Empregador> listarEmpregador(@PathVariable String cpf){
        var empregador = this.service.listarEmpregador(cpf);
        return ResponseEntity.ok(empregador);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Void> atualizar(@RequestBody EmpregadorDto request){
        try {
            this.service.atualizar(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id){
        try {
            this.service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
