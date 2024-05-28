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
@Table(name = "FORMA_PAGAMENTO")
@SequenceGenerator(name = "forma_pgto_seq", sequenceName = "forma_pgto_seq", allocationSize = 1, initialValue = 1)
public class FormaPagamento extends Tipo implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forma_pgto_seq")
    @Column(name = "ID")
    private Long id;
}
