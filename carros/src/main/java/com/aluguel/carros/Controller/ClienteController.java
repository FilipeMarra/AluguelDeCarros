package com.aluguel.carros.Controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aluguel.carros.Service.Impl.ClienteServiceImpl;
import com.aluguel.carros.dto.SaveClienteRequest;
import com.aluguel.carros.exceptions.ClienteJaExisteException;
import com.aluguel.carros.model.Cliente;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/cliente")
@RequiredArgsConstructor
public class ClienteController {
    
    private final ClienteServiceImpl service;

    @PostMapping()
    public ResponseEntity<Map<String, String>> criar(@RequestBody SaveClienteRequest request) {
        try {
            this.service.criar(request);
            return ResponseEntity.ok(Map.of("message", "Usuário criado com sucesso"));
        } catch (ClienteJaExisteException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "CPF já cadastrado"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage() != null ? e.getMessage() : "Erro ao registrar usuário"));
        }
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes(){
        return ResponseEntity.ok(this.service.listarTodos());
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Cliente> listarCliente(@PathVariable String cpf){
        return ResponseEntity.ok(this.service.listarCliente(cpf));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id){
        try {
            this.service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/atualizar")
    public ResponseEntity<Void> atualizar(@RequestBody SaveClienteRequest request){
        try {
            this.service.atualizar(request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
