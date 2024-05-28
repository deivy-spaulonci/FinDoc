package com.br.payments.domain.model;

import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

@MappedSuperclass
public abstract class Tipo implements Serializable {

    private String nome;
}
