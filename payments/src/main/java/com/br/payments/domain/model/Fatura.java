package com.br.payments.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FATURA")
public class Fatura {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(length = 10, nullable = false, name = "PARCELA")
    private Integer parcela;

    @Column(length = 10, nullable = false, name = "TOTAL_PARCELAS")
    private Integer totalParcelas;

    @Column(nullable = false, columnDefinition = "DATE", name = "DATA_PAGAMENTO")
    private LocalDate dataPagamento;

    @Column(precision = 10, scale = 2, nullable = false, name = "VALOR")
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "ID_FORNECEDOR", nullable = true)
    private Fornecedor fornecedor;
}
