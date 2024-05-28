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
@Entity(name = "DESPESA")
public class Despesa extends Pagamento implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "despesa_seq")
    @SequenceGenerator(name = "despesa_seq", sequenceName = "despesa_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_TIPO_DESPESA")
    private TipoDespesa tipoDespesa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_FORNECEDOR")
    private Fornecedor fornecedor;
}
