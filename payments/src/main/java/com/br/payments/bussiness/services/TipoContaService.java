package com.br.payments.bussiness.services;

import com.br.payments.domain.model.TipoConta;
import com.br.payments.domain.repository.TipoContaRepository;
import org.springframework.stereotype.Service;

@Service
public class TipoContaService extends TipoService<TipoConta> {
    public TipoContaService(TipoContaRepository tipoContaRepository) {
        super(tipoContaRepository);
    }
}
