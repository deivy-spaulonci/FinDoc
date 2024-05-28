package com.br.payments.bussiness.exception;

public class TipoDespesaNotFound extends RuntimeException{
    public TipoDespesaNotFound(){
        super("Tipo de despesa inexistente");
    }
}
