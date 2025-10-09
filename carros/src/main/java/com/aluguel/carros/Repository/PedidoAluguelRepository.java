package com.aluguel.carros.Repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aluguel.carros.dto.PedidoAluguelDto;
import com.aluguel.carros.enums.StatusPedido;
import com.aluguel.carros.model.PedidoAluguel;

@Repository
public interface PedidoAluguelRepository extends JpaRepository<PedidoAluguel, UUID>{
    List<PedidoAluguel> findByStatus(StatusPedido status);
}
