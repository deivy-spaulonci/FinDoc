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
@Table(name = "TIPO_DESPESA")
@SequenceGenerator(name = "tipo_despesa_seq", sequenceName = "tipo_despesa_seq", allocationSize = 1, initialValue = 1)
public class TipoDespesa extends Tipo implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_despesa_seq")
    @Column(name = "ID")
    private Long id;
}
