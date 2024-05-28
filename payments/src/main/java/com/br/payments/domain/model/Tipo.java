package com.br.payments.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@MappedSuperclass
public abstract class Tipo implements Serializable {

    @Column(length = 255, nullable = false, name = "NOME")
    private String nome;

    @Column(name = "FILE64")
    private byte[] file64;

    @Getter
    @Transient
    private Long id;

}
