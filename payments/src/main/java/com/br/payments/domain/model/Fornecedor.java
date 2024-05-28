package com.br.payments.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Fornecedor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fornecedor_seq")
    @SequenceGenerator(name = "fornecedor_seq", sequenceName = "fornecedor_seq", allocationSize = 1)
    private Long id;

    @Column(length = 255, nullable = false)
    private String nome;

    @Column(length = 255, nullable = false, name = "RAZAO_SOCIAL")
    private String razaoSocial;

    @Pattern(regexp = "[0-9]*")
    @Size(min = 14, max = 14)
    @Column(length = 60, nullable = true)
    private String cnpj;

    @Size(min = 14, max = 14)
    @Column(length = 60, nullable = true)
    private String cpf;

    @Column(length = 255, nullable = false, name = "CIDADE_CODIGO_IBGE")
    private String cidadeCodigoIbge;
}
