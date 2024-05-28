package com.br.payments.api.dto;

import com.br.personaladm.domain.model.Fornecedor;
import com.br.personaladm.domain.model.tipo.FormaPagamento;
import com.br.personaladm.domain.model.tipo.TipoDespesa;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DespesaDTO {
    private Long id;

    @NotNull
    private TipoDespesa tipoDespesa;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataPagamento;

    @NotNull
    private BigDecimal valor;

    @NotNull
    private FormaPagamento formaPagamento;

    @NotNull
    private Fornecedor fornecedor;

    private String obs;
}
