package com.br.payments.bussiness.exception;

public class DuplicateCnpjException extends RuntimeException{
    public DuplicateCnpjException(){
        super("CNPJ Duplicado!");
    }
}
