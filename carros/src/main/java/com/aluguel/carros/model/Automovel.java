package com.aluguel.carros.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tb_automovel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Automovel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long matricula;
    
    private Integer ano;
    
    private String marca;
    
    private String modelo;
    
    private String placa;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario propetario;

    @OneToOne(mappedBy = "automovel", fetch = FetchType.LAZY)
    private ContratoAluguel contratoAluguel;

    private BigDecimal valorMensal;
}
