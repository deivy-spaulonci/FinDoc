package com.br.payments.bussiness.services;

import com.br.payments.domain.model.Tipo;
import com.br.payments.domain.model.TipoDespesa;
import com.br.payments.domain.repository.IRepositoryTipo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

public class TipoService<T extends Tipo> {

    @Getter
    @Setter
    private IRepositoryTipo<T> iRepositoryTipo;

    public TipoService(IRepositoryTipo<T> iRepositoryTipo) {
        this.iRepositoryTipo = iRepositoryTipo;
    }

    public List<T> findAll(){
        return iRepositoryTipo.findAll(Sort.by(Sort.Order.by("nome")));
    }

    public List<T> findByNome(String nome){
        return iRepositoryTipo.findTByNomeContainingIgnoreCaseOrderByNome(nome);
    }

    public T findById(Long id){
        return  (T) iRepositoryTipo.findById(id).orElseThrow();
    }

    public T update(T t){
        findById(t.getId());
        return (T) iRepositoryTipo.save(t);
    }

    public T save(T t){
        return (T) iRepositoryTipo.save(t);
    }
}
