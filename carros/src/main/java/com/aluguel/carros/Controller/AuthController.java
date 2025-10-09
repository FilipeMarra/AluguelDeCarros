package com.aluguel.carros.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aluguel.carros.Repository.AgenteRepository;
import com.aluguel.carros.Repository.ClienteRepository;
import com.aluguel.carros.config.PasswordEncoderConfiguration;
import com.aluguel.carros.config.TokenService;
import com.aluguel.carros.model.Agente;
import com.aluguel.carros.model.Cliente;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ClienteRepository clienteRepository;
    private final AgenteRepository agenteRepository;
    private final TokenService tokenService;

    // Encoder para senhas criptografadas
    private final PasswordEncoderConfiguration passwordEncoder;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            // Buscar cliente
            Cliente cliente = clienteRepository.findByEmail(request.email()).orElse(null);
            System.out.println("Cliente encontrado: " + cliente);
            System.out.println("Role do cliente: " + (cliente != null ? cliente.getRole() : "null"));
            if (cliente != null && passwordEncoder.passwordEncoder().matches(request.senha(), cliente.getSenha())) {
                String token = tokenService.gerarToken(cliente);
                return ResponseEntity.ok(new LoginResponse(token, "CLIENTE", cliente.getNome()));
            }

            // Buscar agente
            Agente agente = agenteRepository.findAgenteByEmail(request.email()).orElse(null);
            if (agente != null && passwordEncoder.passwordEncoder().matches(request.senha(), agente.getSenha())) {
                String token = tokenService.gerarToken(agente);
                return ResponseEntity.ok(new LoginResponse(token, "AGENTE", agente.getNome()));
            }

            return ResponseEntity.status(401).body(new LoginResponse(null, null, "Credenciais inválidas"));
        } catch (Exception e) {
            e.printStackTrace(); // para log
            return ResponseEntity.status(500).body(new LoginResponse(null, null, "Erro interno do servidor"));
        }
    }

    public record LoginRequest(String email, String senha) {
    }

    public record LoginResponse(String token, String role, String message) {
    }
}
