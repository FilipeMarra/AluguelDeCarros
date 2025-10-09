package com.aluguel.carros.dto;

import com.aluguel.carros.enums.TipoAgente;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SaveAgenteRequest {
    private String email;
    private String senha;
    private String nome;
    private TipoAgente tipoAgente;
}
