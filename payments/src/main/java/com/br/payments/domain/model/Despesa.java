package com.br.payments.domain.model;

import com.br.personaladm.domain.model.tipo.FormaPagamento;
import com.br.personaladm.domain.model.tipo.TipoDespesa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Despesa implements Serializable, IPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "despesa_seq")
    @SequenceGenerator(name = "despesa_seq", sequenceName = "despesa_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_TIPO_DESPESA")
    private TipoDespesa tipoDespesa;

    @Column(nullable = true, columnDefinition = "DATE", name = "data")
    private LocalDate dataPagamento;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_FORMA_PAGAMENTO")
    private FormaPagamento formaPagamento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_FORNECEDOR")
    private Fornecedor fornecedor;

    @Column(length = 255, nullable = true)
    private String obs;

    @Column(name = "DATA_LANCAMENTO", insertable = false, nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime dataLancamento;
}
