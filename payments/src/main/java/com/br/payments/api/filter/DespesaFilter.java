package com.br.payments.api.filter;

import com.br.personaladm.domain.model.Fornecedor;
import com.br.personaladm.domain.model.tipo.FormaPagamento;
import com.br.personaladm.domain.model.tipo.TipoDespesa;

import java.time.LocalDate;

public record DespesaFilter(Long id,
                            TipoDespesa tipoDespesa,
                            LocalDate dataInicial,
                            LocalDate dataFinal,
                            Fornecedor fornecedor,
                            FormaPagamento formaPagamento) {
    public DespesaFilter(Long id,
                         TipoDespesa tipoDespesa,
                         LocalDate dataInicial,
                         LocalDate dataFinal,
                         Fornecedor fornecedor,
                         FormaPagamento formaPagamento){
        this.id = id;
        this.tipoDespesa = tipoDespesa;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.fornecedor = fornecedor;
        this.formaPagamento = formaPagamento;
    }
}
