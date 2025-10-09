package com.aluguel.carros.model;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tb_pedido_avaliacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvaliacaoPedido {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @OneToOne
    @JoinColumn(name = "pedido_id")
    private PedidoAluguel pedidoAvaliado;
    
    @ManyToOne
    private Agente agenteAvaliador;
    
    private LocalDate data;
    
    private String parecer;
}
