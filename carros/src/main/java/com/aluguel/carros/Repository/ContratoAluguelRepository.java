package com.aluguel.carros.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aluguel.carros.model.ContratoAluguel;

@Repository
public interface ContratoAluguelRepository extends JpaRepository<ContratoAluguel, UUID>{
    
}
