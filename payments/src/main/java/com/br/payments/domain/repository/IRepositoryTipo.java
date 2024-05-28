package com.br.payments.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;


@NoRepositoryBean
public interface IRepositoryTipo<Tipo> extends JpaRepository<Tipo, Long>, JpaSpecificationExecutor<Tipo> {
    List<Tipo> findTByNomeContainingIgnoreCaseOrderByNome(String nome);
}
