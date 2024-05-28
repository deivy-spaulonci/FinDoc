package com.br.payments.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TipoConta implements Serializable, ITipo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_conta_seq")
    @SequenceGenerator(name = "tipo_conta_seq", sequenceName = "tipo_conta_seq", allocationSize = 1, initialValue = 1 )
    private Long id;

    @Column(length = 255, nullable = false)
    private String nome;

    private Boolean contaCartao;

    private Boolean ativa;
}