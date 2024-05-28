package com.br.payments.api.restcontroller;

import com.br.payments.api.dto.TipoContaDTO;
import com.br.payments.api.mapper.TipoContaMapper;
import com.br.payments.bussiness.services.TipoService;
import com.br.payments.domain.model.TipoConta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipo-conta")
public class TipoContaRestController {

    @Autowired
    private TipoService<TipoConta> tipoContaService;

    public static final TipoContaMapper tipoContaMapper = TipoContaMapper.INSTANCE;

    @GetMapping
    public List<TipoContaDTO> listar() {
        return tipoContaMapper.toDtoList(tipoContaService.findAll());
    }
}
