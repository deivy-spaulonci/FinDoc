package com.br.payments.bussiness.services;


import com.br.payments.bussiness.exception.DuplicateCnpjException;
import com.br.payments.domain.model.Fornecedor;
import com.br.payments.domain.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * classe service de fornecedores
 */
@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    public List<Fornecedor> findAll(){
        return fornecedorRepository.findAll();
    }

    public Page<Fornecedor> findAll(Pageable pageable){
        return fornecedorRepository.findAll(pageable);
    }

    public List<Fornecedor> listFornecedorByNomeOrRazaoOrCNPJ(String valor){
        return fornecedorRepository.findFornecedoresByNomeOrRazaoSocialrCnpj(valor);
    }

    public Page<Fornecedor> getPageByNomeOrRazaoOrCNPJ(String valor, Pageable pageable){
        return fornecedorRepository.findFornecedoresByNomeOrRazaoSocialrCnpjPage(valor, pageable);
    }

    public Optional<Fornecedor> findFornecedorByCNPJ(String cnpj){
        return fornecedorRepository.findFornecedorByCnpj(cnpj);
    }

    public Optional<Fornecedor> findById(Long id){
        return fornecedorRepository.findById(id);
    }

    public Fornecedor update(Fornecedor fornecedor){
        findById(fornecedor.getId());
        return fornecedorRepository.save(fornecedor);
    }

    public Fornecedor save(Fornecedor fornecedor){
        if(fornecedorRepository.findFornecedorByCnpj(fornecedor.getCnpj()).isEmpty())
            return fornecedorRepository.save(fornecedor);
        throw new DuplicateCnpjException();
    }

    public void delete(Long id){
        findById(id);
        fornecedorRepository.deleteById(id);
    }
}
