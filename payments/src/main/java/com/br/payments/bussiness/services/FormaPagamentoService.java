package com.br.payments.bussiness.services;

import com.br.payments.domain.model.FormaPagamento;
import com.br.payments.domain.repository.FormaPagamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class FormaPagamentoService extends TipoService<FormaPagamento> {
    public FormaPagamentoService(FormaPagamentoRepository formaPagamentoRepository) {
        super(formaPagamentoRepository);
    }
}
