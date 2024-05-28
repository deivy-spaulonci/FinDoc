package com.br.payments.bussiness.exception;

public class ContaNotFoundException extends RuntimeException{
    public ContaNotFoundException(){
        super("Conta não encontrada!");
    }
}
