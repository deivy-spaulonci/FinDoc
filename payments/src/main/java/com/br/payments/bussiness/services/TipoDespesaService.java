package com.br.payments.bussiness.services;

import com.br.payments.domain.model.TipoDespesa;
import com.br.payments.domain.repository.TipoDespesaRepository;
import org.springframework.stereotype.Service;

@Service
public class TipoDespesaService extends TipoService<TipoDespesa> {

    public TipoDespesaService(TipoDespesaRepository tipoDespesaRepository) {
        super(tipoDespesaRepository);
    }
}
