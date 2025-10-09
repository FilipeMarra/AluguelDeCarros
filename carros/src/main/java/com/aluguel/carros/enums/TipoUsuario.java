package com.aluguel.carros.enums;

public enum TipoUsuario {
    CLIENTE ("CLIENTE"),
    AGENTE ("AGENTE");

    public String role;

    TipoUsuario(String role){
        this.role = role;
    }
}
