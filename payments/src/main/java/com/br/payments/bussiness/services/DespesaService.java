package com.br.payments.bussiness.services;


import com.br.payments.api.filter.DespesaFilter;
import com.br.payments.domain.model.Despesa;
import com.br.payments.domain.repository.DespesaRepository;
import com.br.payments.domain.specs.DespesaSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;

    @PersistenceContext
    private EntityManager em;

    public List<Despesa> searchByFilterOrder(DespesaFilter despesaFilter, Sort sort){
        DespesaSpecification despesaSpecification = new DespesaSpecification(despesaFilter);
        return despesaRepository.findAll(despesaSpecification, sort);
    }

    public Page<Despesa> getPageByFilters(DespesaFilter despesaFilter, Pageable pageable){
        DespesaSpecification despesaSpecification = new DespesaSpecification(despesaFilter);
        return despesaRepository.findAll(despesaSpecification, pageable);
    }

    public Optional<Despesa> findById(Long id){
        return despesaRepository.findById(id);
    }

    public Despesa save(Despesa despesa){
        return despesaRepository.save(despesa);
    }

    public Despesa update(Despesa despesa){
        findById(despesa.getId());
        return despesaRepository.save(despesa);
    }

    public void deleteById(Long id){
        findById(id);
        despesaRepository.deleteById(id);
    }

    public BigDecimal getValorTotalDespesa(DespesaFilter despesaFilter) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Object> query = cb.createQuery(Object.class);
        final Root<Despesa> root = query.from(Despesa.class);
        DespesaSpecification despesaSpecification = new DespesaSpecification(despesaFilter);
        query.multiselect(cb.sum(root.get("valor")));
        query.where(despesaSpecification.toPredicate(root, query, cb));
        Query qry = em.createQuery(query);
        return (BigDecimal) qry.getSingleResult();
    }

    public int salvarImportacaoDespesas(List<Despesa> lista){
        return despesaRepository.saveAll(lista).size();
    }

    public List getDespesaPorTipo() {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Object> query = cb.createQuery(Object.class);
        final Root<Despesa> root = query.from(Despesa.class);
        query.multiselect(
                root.get("tipoDespesa"),
                cb.sum(root.get("valor")));
        query.groupBy(root.get("tipoDespesa"));
        Query qry = em.createQuery(query);
        return qry.getResultList();
    }
}
