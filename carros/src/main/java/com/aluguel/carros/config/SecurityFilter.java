package com.aluguel.carros.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    
    private final TokenService tokenService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("[SecurityFilter] Rota interceptada: " + request.getRequestURI());

        // Lista de rotas públicas que não precisam de token
        List<String> publicRoutes = List.of(
                "/auth",
                "/api/v1/pedido",
                "/api/v1/pedido/", // garante POST /api/v1/pedido/funcao
                "/api/v1/automovel",
                "/api/v1/automovel/",
                "/api/v1/avaliacao",
                "/api/v1/avaliacao/",
                "/api/v1/cliente",
                "/api/v1/empregador",
                "/api/v1/empregador/",
                "/api/v1/agente",
                "/api/v1/agente/"
                );

        // Verifica se a rota atual é pública
        String requestURI = request.getRequestURI();
        System.out.println("[SecurityFilter] Verificando rota: " + requestURI);
        
        boolean isPublicRoute = publicRoutes.stream().anyMatch(route -> {
            boolean matches = requestURI.startsWith(route);
            System.out.println("[SecurityFilter] Testando rota pública '" + route + "' com '" + requestURI + "': " + matches);
            return matches;
        });

        // Se for rota pública, permite a requisição sem token
        if (isPublicRoute) {
            System.out.println("[SecurityFilter] Rota pública detectada: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        String token = recuperarToken(request);
        System.out.println("[SecurityFilter] Token recebido: " + token);

        if (token != null) {
            try {
                DecodedJWT jwt = tokenService.getDecodedJWT(token);
                String email = jwt.getSubject();
                String tipo = jwt.getClaim("tipo").asString();

                if (email != null && tipo != null) {
                    var authority = new SimpleGrantedAuthority("ROLE_" + tipo.toUpperCase());
                    var user = new User(email, "", List.of(authority));
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("[SecurityFilter] Autenticado como: ROLE_" + tipo.toUpperCase());
                }
            } catch (Exception ex) {
                System.out.println("[SecurityFilter] Erro ao processar token: " + ex.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Token inválido ou expirado. Por favor, faça login novamente.");
                return;
            }
        } else {
            System.out.println("[SecurityFilter] Token não encontrado para rota protegida: " + request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Token não fornecido. Por favor, faça login novamente.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("[SecurityFilter] Header de autorização não encontrado ou inválido");
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }

}

