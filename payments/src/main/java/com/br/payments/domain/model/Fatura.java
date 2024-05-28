package com.br.payments.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Fatura {
    @Id
    private Long id;

    @Column(length = 10, nullable = false)
    private Integer parcela;

    @Column(length = 10, nullable = false)
    private Integer totalParcelas;

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate data;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "ID_FORNECEDOR", nullable = true)
    private Fornecedor fornecedor;
}
