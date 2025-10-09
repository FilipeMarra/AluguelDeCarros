package com.aluguel.carros.model;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tb_contrato_aluguel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratoAluguel {
    @Id
    @UuidGenerator
    private UUID id;
    
    private LocalDate dataInicio;
    
    private LocalDate datafim;

    @OneToOne
    private Cliente clienteContratante;

    @OneToOne
    @JoinColumn(name = "automovel_id")
    private Automovel automovel;

    private String bancoFinanciador;
}
