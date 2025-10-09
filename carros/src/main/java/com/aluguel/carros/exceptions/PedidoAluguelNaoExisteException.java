package com.aluguel.carros.exceptions;

public class PedidoAluguelNaoExisteException extends RuntimeException{
    public PedidoAluguelNaoExisteException(){
        super("Pedido de aluguel não encontrado.");
    }
}
