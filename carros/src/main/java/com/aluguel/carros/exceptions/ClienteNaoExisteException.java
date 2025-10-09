package com.aluguel.carros.exceptions;

public class ClienteNaoExisteException extends RuntimeException{
    public ClienteNaoExisteException(){
        super("O cliente nao existe");
    }
}
