package com.aluguel.carros.model;

import java.math.BigDecimal;
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

@Entity(name = "tb_empregadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empregador {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    private String nome;
    
    private String cpf;

    @OneToOne
    @JoinColumn(name = "cliente_id")
    private Cliente clienteAssociado;
    
    private BigDecimal rendimento;
}
