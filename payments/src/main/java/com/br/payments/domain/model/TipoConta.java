package com.br.payments.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TIPO_CONTA")
@SequenceGenerator(name = "tipo_conta_seq", sequenceName = "tipo_conta_seq", allocationSize = 1, initialValue = 1 )
public class TipoConta extends Tipo implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_conta_seq")
    @Column(name = "ID")
    private Long id;
    @Column(name = "CONTA_CARTAO")
    private Boolean contaCartao;
    @Column(name = "ATIVA")
    private Boolean ativa;
}