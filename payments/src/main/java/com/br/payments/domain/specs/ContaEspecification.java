package com.br.payments.domain.specs;

import com.br.personaladm.api.filter.ContaFilter;
import com.br.personaladm.domain.model.Conta;
import com.br.personaladm.domain.model.ContaStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContaEspecification implements Specification<Conta> {
    private ContaFilter contaFilter;

    public ContaEspecification(ContaFilter contaFilter) {
        this.contaFilter = contaFilter;
    }

    @Override
    public Predicate toPredicate(Root<Conta> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(contaFilter.id()))
            predicates.add(criteriaBuilder.equal(root.get("id"), contaFilter.id()));
        if (Objects.nonNull(contaFilter.tipoConta()))
            predicates.add(criteriaBuilder.equal(root.get("tipoConta"), contaFilter.tipoConta()));

        if (Objects.nonNull(contaFilter.vencimentoInicial()) && Objects.nonNull(contaFilter.vencimentoFinal()))
            predicates.add(criteriaBuilder.between(root.get("vencimento"), contaFilter.vencimentoInicial(), contaFilter.vencimentoFinal()));
        if (Objects.nonNull(contaFilter.vencimentoInicial()))
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("vencimento"), contaFilter.vencimentoInicial()));
        if (Objects.nonNull(contaFilter.vencimentoFinal()))
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("vencimento"), contaFilter.vencimentoFinal()));

        if (Objects.nonNull(contaFilter.emissaoInicio()) && Objects.nonNull(contaFilter.emissaoTermino()))
            predicates.add(criteriaBuilder.between(root.get("emissao"), contaFilter.emissaoInicio(), contaFilter.emissaoTermino()));
        if (Objects.nonNull(contaFilter.emissaoInicio()))
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("emissao"), contaFilter.emissaoInicio()));
        if (Objects.nonNull(contaFilter.emissaoTermino()))
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("emissao"), contaFilter.emissaoTermino()));

        if(Objects.nonNull(contaFilter.contaStatus())){
            if(contaFilter.contaStatus().equals(ContaStatus.PAGO)){
                predicates.add(criteriaBuilder.isNotNull(root.get("formaPagamento")));
                predicates.add(criteriaBuilder.isNotNull(root.get("dataPagamento")));
            }else{
                predicates.add(criteriaBuilder.isNull(root.get("formaPagamento")));
                switch (contaFilter.contaStatus()){
                    case ContaStatus.ABERTO -> predicates.add(criteriaBuilder.greaterThan(root.get("vencimento"), LocalDate.now()));
                    case ContaStatus.HOJE -> predicates.add(criteriaBuilder.equal(root.get("vencimento"), LocalDate.now()));
                    case ContaStatus.ATRASADO -> predicates.add(criteriaBuilder.lessThan(root.get("vencimento"), LocalDate.now()));
                }
            }
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
