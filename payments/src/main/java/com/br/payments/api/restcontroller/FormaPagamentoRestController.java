package com.br.payments.api.restcontroller;

import com.br.payments.api.dto.FormaPagamentoDTO;
import com.br.payments.api.mapper.FormaPagamentoMapper;
import com.br.payments.bussiness.services.TipoService;
import com.br.payments.domain.model.FormaPagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forma-pagamento")
public class FormaPagamentoRestController {

    @Autowired
    private TipoService<FormaPagamento> formaPagamentoTipoService;

    public static final FormaPagamentoMapper formaPagamentoMapper = FormaPagamentoMapper.INSTANCE;

    @GetMapping
    public List<FormaPagamentoDTO> listar() {
        return formaPagamentoMapper.toDtoList(formaPagamentoTipoService.findAll());
    }
}
