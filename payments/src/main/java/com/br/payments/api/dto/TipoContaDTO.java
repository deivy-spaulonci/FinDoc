package com.br.payments.api.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoContaDTO {
    private Long id;

    @NotBlank
    @NotNull
    @Size(min = 3, max = 255)
    private String nome;

    private byte[] file64;

    private Boolean contaCartao;

    private Boolean ativa;
}
