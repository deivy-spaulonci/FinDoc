package com.br.payments.api.restcontroller;

import com.br.payments.api.dto.TipoDespesaDTO;
import com.br.payments.api.mapper.TipoDespesaMapper;
import com.br.payments.bussiness.services.TipoService;
import com.br.payments.domain.model.TipoDespesa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipo-despesa")
public class TipoDespesaRestController {

    @Autowired
    private TipoService<TipoDespesa> tipoService;

    public static final TipoDespesaMapper tipoDespesaMapper = TipoDespesaMapper.INSTANCE;

    @GetMapping
    public List<TipoDespesaDTO> listar() {
        return tipoDespesaMapper.toDtoList(tipoService.findAll());
    }

}
