package com.aluguel.carros.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors()
                .and()
                .csrf(csrf -> csrf.disable()) // desabilita CSRF
                .formLogin().disable()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth").permitAll()
                        .requestMatchers("/api/v1/pedido/**").permitAll()
                        .requestMatchers("/api/v1/avaliacao/**").permitAll()
                        .requestMatchers("/api/v1/automovel/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/cliente").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/agente").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/cliente").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/agente").hasAuthority("AGENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/cliente").hasAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/agente").hasAuthority("AGENTE")
                        .requestMatchers("/api/v1/empregador/**").hasAuthority("CLIENTE")

                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
