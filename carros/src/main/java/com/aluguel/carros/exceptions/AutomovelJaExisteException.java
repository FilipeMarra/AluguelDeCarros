package com.aluguel.carros.exceptions;

public class AutomovelJaExisteException extends RuntimeException{
    public AutomovelJaExisteException(){
        super("O automovel ja existe");
    }
}
