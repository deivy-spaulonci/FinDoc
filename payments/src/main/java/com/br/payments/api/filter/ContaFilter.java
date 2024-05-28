package com.br.payments.api.filter;

import com.br.payments.domain.model.ContaStatus;
import com.br.payments.domain.model.TipoConta;

import java.time.LocalDate;

public record ContaFilter(Long id,
                          TipoConta tipoConta,
                          LocalDate vencimentoInicial,
                          LocalDate vencimentoFinal,
                          LocalDate emissaoInicio,
                          LocalDate emissaoTermino,
                          ContaStatus contaStatus) {

    public ContaFilter(Long id,
                       TipoConta tipoConta,
                       LocalDate vencimentoInicial,
                       LocalDate vencimentoFinal,
                       LocalDate emissaoInicio,
                       LocalDate emissaoTermino,
                       ContaStatus contaStatus){
        this.id = id;
        this.tipoConta = tipoConta;
        this.vencimentoInicial = vencimentoInicial;
        this.vencimentoFinal = vencimentoFinal;
        this.emissaoInicio = emissaoInicio;
        this.emissaoTermino = emissaoTermino;
        this.contaStatus = contaStatus;
    }
}
