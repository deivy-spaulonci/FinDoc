package com.br.personaladm.business.exception;

import com.br.personaladm.api.config.Messages;

public class DuplicateCnpjException extends RuntimeException{
    public DuplicateCnpjException(){
        super(Messages.getMessage("cnpj.duplicado"));
    }
}
