package com.br.payments.domain.specs;

import com.br.personaladm.api.filter.DespesaFilter;
import com.br.personaladm.domain.model.Despesa;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DespesaSpecification implements Specification<Despesa> {
    private DespesaFilter despesaFilter;

    public DespesaSpecification(DespesaFilter despesaFilter) {
        this.despesaFilter = despesaFilter;
    }

    @Override
    public Predicate toPredicate(Root<Despesa> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(despesaFilter.id()))
            predicates.add(criteriaBuilder.equal(root.get("id"), despesaFilter.id()));
        if (Objects.nonNull(despesaFilter.tipoDespesa()))
            predicates.add(criteriaBuilder.equal(root.get("tipoDespesa"), despesaFilter.tipoDespesa()));
        if (Objects.nonNull(despesaFilter.fornecedor()))
            predicates.add(criteriaBuilder.equal(root.get("fornecedor"), despesaFilter.fornecedor()));
        if (Objects.nonNull(despesaFilter.dataInicial()) && Objects.nonNull(despesaFilter.dataFinal()))
            predicates.add(criteriaBuilder.between(root.get("data"), despesaFilter.dataInicial(), despesaFilter.dataFinal()));
        if (Objects.nonNull(despesaFilter.dataInicial()))
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("data"), despesaFilter.dataInicial()));
        if (Objects.nonNull(despesaFilter.dataFinal()))
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("data"), despesaFilter.dataFinal()));
        if (Objects.nonNull(despesaFilter.formaPagamento()))
            predicates.add(criteriaBuilder.equal(root.get("formaPagamento"), despesaFilter.formaPagamento()));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
