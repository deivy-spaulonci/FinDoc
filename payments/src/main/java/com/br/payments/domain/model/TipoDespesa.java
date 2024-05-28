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
public class TipoDespesa implements Serializable, GenericEntityTipo<TipoDespesa>, ITipo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_despesa_seq")
    @SequenceGenerator(name = "tipo_despesa_seq", sequenceName = "tipo_despesa_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(length = 255, nullable = false)
    private String nome;

    @Override
    public void update(TipoDespesa source) {
        this.nome = source.getNome();
    }

    @Override
    public TipoDespesa createNewInstance() {
        TipoDespesa newInstance = new TipoDespesa();
        newInstance.update(this);
        return newInstance;
    }

    @Override
    public String toString(){
        return getNome();
    }
}
