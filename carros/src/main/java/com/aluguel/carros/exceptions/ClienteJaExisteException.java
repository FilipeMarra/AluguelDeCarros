package com.aluguel.carros.exceptions;

public class ClienteJaExisteException extends RuntimeException{
    public ClienteJaExisteException(){
        super("O cliente ja existe");
    }
}
