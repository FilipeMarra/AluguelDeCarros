package com.aluguel.carros.model;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.aluguel.carros.enums.StatusPedido;

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

@Entity(name = "tb_pedido_aluguel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoAluguel {
    
    @Id
    @UuidGenerator
    private UUID id;

    private LocalDate dataSolicitacao;
    
    private StatusPedido status;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente clienteSolicitante;

    @OneToOne
    private Automovel automovelDesejado;

    @OneToOne(mappedBy = "pedidoAvaliado")
    private AvaliacaoPedido avaliacao;
    
}
