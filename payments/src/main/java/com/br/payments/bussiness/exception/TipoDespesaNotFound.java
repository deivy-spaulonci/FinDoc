package com.br.payments.bussiness.exception;

public class TipoDespesaNotFound extends RuntimeException{
    public TipoDespesaNotFound(Long id){
        super("Tipo de despesa inexistente id [%s]".formatted(id));
    }
}
