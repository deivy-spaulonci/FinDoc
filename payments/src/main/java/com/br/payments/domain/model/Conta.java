package com.br.payments.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CONTA")
public class Conta extends Pagamento implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conta_seq")
    @SequenceGenerator(name = "conta_seq", sequenceName = "conta_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_TIPO_CONTA")
    private TipoConta tipoConta;

    @Column(length = 60, nullable = false, name = "CODIGO_BARRA")
    private String codigoBarra;

    @Column(nullable = false, columnDefinition = "DATE", name = "EMISSAO")
    private LocalDate emissao;

    @Column(nullable = false, columnDefinition = "DATE", name = "VENCIMENTO")
    private LocalDate vencimento;

    @Column(length = 10, nullable = false, name = "PARCELA")
    private Integer parcela;

    @Column(length = 10, nullable = false, name = "TOTAL_PARCELAS")
    private Integer totalParcelas;

    @Column(precision = 10, scale = 2, nullable = true, name = "MULTA")
    private BigDecimal multa;

    @Column(name = "TITULO")
    private byte[] titulo;

    @Column(name = "COMPROVANTE")
    private byte[] comprovante;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(name = "CONTA_FATURA",
            joinColumns = @JoinColumn(name = "ID_CONTA"),
            inverseJoinColumns = @JoinColumn(name = "ID_FATURA"))
    private List<Fatura> faturas;

}
