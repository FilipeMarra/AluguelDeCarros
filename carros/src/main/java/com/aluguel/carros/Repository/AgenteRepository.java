package com.aluguel.carros.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aluguel.carros.model.Agente;

@Repository
public interface AgenteRepository extends JpaRepository<Agente, UUID>{
    Optional<Agente> findAgenteByEmail(String email);
}
