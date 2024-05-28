package com.br.payments.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class Pagamento implements Serializable {

    @Column(precision = 10, scale = 2, nullable = false, name = "VALOR")
    private BigDecimal valor;

    @ManyToOne(optional = true)
    @JoinColumn(name = "ID_FORMA_PAGAMENTO")
    private FormaPagamento formaPagamento;

    @Column(nullable = true, columnDefinition = "DATE", name = "DATA_PAGAMENTO")
    private LocalDate dataPagamento;

    @Column(name = "DATA_LANCAMENTO", insertable = false, nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime dataLancamento;

    @Column(length = 255, nullable = true, name = "OBS")
    private String obs;
}
