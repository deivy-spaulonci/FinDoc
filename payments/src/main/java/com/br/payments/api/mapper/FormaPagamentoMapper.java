package com.br.payments.api.mapper;

import com.br.payments.api.dto.FormaPagamentoDTO;
import com.br.payments.domain.model.FormaPagamento;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FormaPagamentoMapper {
    FormaPagamentoMapper INSTANCE = Mappers.getMapper(FormaPagamentoMapper.class);
    FormaPagamento toModel(FormaPagamentoDTO formaPagamentoDTO);
    FormaPagamentoDTO toDTO(FormaPagamento formaPagamento);
    List<FormaPagamentoDTO> toDtoList(List<FormaPagamento> formasPagamento);
}
