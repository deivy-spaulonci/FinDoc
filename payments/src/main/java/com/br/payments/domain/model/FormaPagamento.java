package com.br.payments.domain.model;

import jakarta.persistence.*;
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
public class FormaPagamento implements Serializable, GenericEntityTipo<FormaPagamento>, ITipo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forma_pgto_seq")
    @SequenceGenerator(name = "forma_pgto_seq", sequenceName = "forma_pgto_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(length = 255, nullable = false)
    private String nome;

    @Override
    public void update(FormaPagamento source) {
        this.nome = source.getNome();
    }

    @Override
    public FormaPagamento createNewInstance() {
        FormaPagamento newInstance = new FormaPagamento();
        newInstance.update(this);
        return newInstance;
    }
}
