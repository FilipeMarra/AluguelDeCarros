package com.aluguel.carros.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aluguel.carros.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class TokenService {
    
    @Value("${jwt.secret.key}")
    private String secretKey;

    public String gerarToken(Usuario users) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            if (users.getRole() == null) {
                throw new IllegalStateException("Usuário sem role definido!");
            }
            String token = JWT.create()
                    .withClaim("tipo", users.getRole().toString().toUpperCase())
                    .withIssuer("auth0")
                    .withSubject(users.getEmail())
                    .withExpiresAt(gerarExpiracao())
                    .sign(algorithm);
            System.out.println("[TokenService] Token gerado para: " + users.getEmail());
            return token;
        } catch (JWTCreationException exception) {
            System.out.println("[TokenService] Erro ao gerar token: " + exception.getMessage());
            throw new JWTCreationException("Erro na criação do token", exception);
        }
    }

    public String verificarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String subject = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build()
                    .verify(token)
                    .getSubject();
            System.out.println("[TokenService] Token verificado para: " + subject);
            return subject;
        } catch (JWTVerificationException exception) {
            System.out.println("[TokenService] Erro ao verificar token: " + exception.getMessage());
            throw new JWTVerificationException("Erro ao verificar o token", exception);
        }
    }

    public DecodedJWT getDecodedJWT(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secretKey))
                    .withIssuer("auth0")
                    .build()
                    .verify(token);
            System.out.println("[TokenService] Token decodificado para: " + jwt.getSubject());
            return jwt;
        } catch (JWTVerificationException exception) {
            System.out.println("[TokenService] Erro ao decodificar token: " + exception.getMessage());
            throw new JWTVerificationException("Erro ao decodificar o token", exception);
        }
    }

    private Instant gerarExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
