package com.aluguel.carros.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aluguel.carros.model.Empregador;

@Repository
public interface EmpregadorRepository extends JpaRepository<Empregador, UUID>{
    Optional<Empregador> findByCpf(String cpf);
}
