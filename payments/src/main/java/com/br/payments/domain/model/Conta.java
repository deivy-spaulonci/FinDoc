package com.br.personaladm.domain.model;

import com.br.personaladm.domain.model.tipo.FormaPagamento;
import com.br.personaladm.domain.model.tipo.TipoConta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Conta implements Serializable, IPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conta_seq")
    @SequenceGenerator(name = "conta_seq", sequenceName = "conta_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_TIPO_CONTA")
    private TipoConta tipoConta;

    @Column(length = 60, nullable = false)
    private String codigoBarra;

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate emissao;

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate vencimento;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(length = 10, nullable = false)
    private Integer parcela;

    @Column(length = 10, nullable = false)
    private Integer totalParcelas;

    @ManyToOne(optional = true)
    @JoinColumn(name = "ID_FORMA_PAGAMENTO")
    private FormaPagamento formaPagamento;

    @Column(nullable = true, columnDefinition = "DATE")
    private LocalDate dataPagamento;

    @Column(precision = 10, scale = 2, nullable = true)
    private BigDecimal multa;

    @Column(length = 255, nullable = true)
    private String obs;

    private String titulo;

    private String comprovante;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(name = "CONTA_FATURA",
            joinColumns = @JoinColumn(name = "ID_CONTA"),
            inverseJoinColumns = @JoinColumn(name = "ID_FATURA"))
    private List<Fatura> faturas;

    @Column(name = "DATA_LANCAMENTO", insertable = false, nullable = true, columnDefinition = "TIMESTAMP")
    private LocalDateTime dataLancamento;
}
