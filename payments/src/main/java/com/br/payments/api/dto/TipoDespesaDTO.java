package com.br.payments.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDespesaDTO {

    private Long id;

    @NotBlank
    @NotNull
    @Size(min = 3, max = 255)
    private String nome;

    private String file64;
}
