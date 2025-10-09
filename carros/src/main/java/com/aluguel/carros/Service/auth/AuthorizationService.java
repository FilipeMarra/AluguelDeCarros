package com.aluguel.carros.Service.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import com.aluguel.carros.Repository.AgenteRepository;
import com.aluguel.carros.Repository.ClienteRepository;
import com.aluguel.carros.model.Usuario;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService{

    private final ClienteRepository clienteRepository;
    private final AgenteRepository agenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.clienteRepository.findByEmail(username)
        .map(this::mapToUserDetails)
        .or(() -> this.agenteRepository.findAgenteByEmail(username).map(this::mapToUserDetails))
        .orElseThrow();
    }

    private UserDetails mapToUserDetails(Usuario user) {
        return User
                .withUsername(user.getEmail())
                .password(user.getSenha())
                .roles(user.getRole().name())
                .build();
    }
}
