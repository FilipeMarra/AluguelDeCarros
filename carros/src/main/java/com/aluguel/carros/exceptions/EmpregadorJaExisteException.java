package com.aluguel.carros.exceptions;

public class EmpregadorJaExisteException extends RuntimeException{
    public EmpregadorJaExisteException(){
        super("O empregador ja existe");
    }
}
