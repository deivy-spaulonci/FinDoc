package com.br.payments.bussiness.services;

import com.br.payments.bussiness.exception.TipoDespesaNotFound;
import com.br.payments.domain.model.Tipo;
import com.br.payments.domain.model.TipoDespesa;
import com.br.payments.domain.repository.IRepositoryTipo;
import com.br.payments.domain.repository.TipoContaRepository;
import com.br.payments.domain.repository.TipoDespesaRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoService<T extends Tipo> {

    @Getter
    @Setter
    private IRepositoryTipo iRepositoryTipo;

    public TipoService(){
        if(iRepositoryTipo == null){
            iRepositoryTipo = getIRepositoryTipo();
        }
    }

    public List<T> findAll(){
        return iRepositoryTipo.findAll(Sort.by(Sort.Order.by("nome")));
    }

    public List<T> findByNome(String nome){
        return iRepositoryTipo.findTByNomeContainingIgnoreCaseOrderByNome(nome);
    }

    public T findById(Long id){
        return iRepositoryTipo.findById(id).orElseThrow(TipoDespesaNotFound::new);
    }

    public T update(T t){
        findById(t.getId());
        return iRepositoryTipo.save(t);
    }

    public TipoDespesa save(TipoDespesa tipoDespesa){
        return iRepositoryTipo.save(tipoDespesa);
    }
}
