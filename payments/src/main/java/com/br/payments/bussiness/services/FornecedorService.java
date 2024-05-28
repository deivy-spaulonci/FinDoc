package com.br.payments.bussiness.services;

import com.br.personaladm.business.exception.DuplicateCnpjException;
import com.br.personaladm.business.exception.FornecedorNotFound;
import com.br.personaladm.domain.model.Fornecedor;
import com.br.personaladm.domain.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    /**
     * busca fornecedor
     * @return List<Fornecedor>
     */
    public List<Fornecedor> findAll(){
        return fornecedorRepository.findAll();
    }
    /**
     * busca fornecedor
     * @return Page<Fornecedor>
     */
    public Page<Fornecedor> findAll(Pageable pageable){
        return fornecedorRepository.findAll(pageable);
    }
    /**
     * busca qualquer fornecedor com valor para cnpj ou razao ou nome
     * @param valor
     * @return
     */
    public List<Fornecedor> listFornecedorByNomeOrRazaoOrCNPJ(String valor){
        return fornecedorRepository.findFornecedoresByNomeOrRazaoSocialrCnpj(valor);
    }
    /**
     * lista fornecedores com filtros e paginado
     * @param valor
     * @param pageable
     * @return Page<Fornecedor>
     */
    public Page<Fornecedor> getPageByNomeOrRazaoOrCNPJ(String valor, Pageable pageable){
        return fornecedorRepository.findFornecedoresByNomeOrRazaoSocialrCnpjPage(valor, pageable);
    }
    /**
     * fornecedor pelo cnpj
     * @param cnpj
     * @return Optional<Fornecedor>
     */
    public Optional<Fornecedor> findFornecedorByCNPJ(String cnpj){
        return fornecedorRepository.findFornecedorByCnpj(cnpj);
    }
    /**
     * busca fornecedor pelo id
     * @param id
     * @return Optional<Fornecedor>
     * @throws FornecedorNotFound
     */
    public Optional<Fornecedor> findById(Long id){
        return fornecedorRepository.findById(id);
    }
    /**
     * atualiza o fornecedor
     * @param fornecedor
     * @return Fonecedor
     * @throws FornecedorNotFound
     */
    public Fornecedor update(Fornecedor fornecedor){
        findById(fornecedor.getId());
        return fornecedorRepository.save(fornecedor);
    }
    /**
     * @param fornecedor
     * @return Fornecedor
     * @throws DuplicateCnpjException
     */
    public Fornecedor save(Fornecedor fornecedor){
        if(fornecedorRepository.findFornecedorByCnpj(fornecedor.getCnpj()).isEmpty())
            return fornecedorRepository.save(fornecedor);
        throw new DuplicateCnpjException();
    }
    /**
     * deletar fornecedor
     * @param id
     * @throws FornecedorNotFound
     */
    public void delete(Long id){
        findById(id);
        fornecedorRepository.deleteById(id);
    }
}
