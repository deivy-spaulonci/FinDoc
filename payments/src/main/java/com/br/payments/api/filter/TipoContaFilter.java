package com.br.payments.api.filter;

public record TipoContaFilter(Long id, String nome, Boolean contaCartao, Boolean contaAtiva) {
    public TipoContaFilter(Long id, String nome, Boolean contaCartao, Boolean contaAtiva){
        this.id = id;
        this.nome = nome;
        this.contaCartao = contaCartao;
        this.contaAtiva = contaAtiva;
    }
}
