package com.aluguel.carros.enums;

public enum StatusPedido {
    PENDENTE ("pendente"),
    ANALISE ("em_analise"),
    APROVADO ("aprovado"),
    REJEITADO ("rejeitado"),
    CANCELADO ("cancelado");

    private String status;

    StatusPedido(String status){
        this.status = status;
    }

    public String getStatus(){
        return this.status;
    }
}
