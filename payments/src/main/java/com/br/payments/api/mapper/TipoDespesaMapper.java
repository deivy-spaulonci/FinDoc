package com.br.payments.api.mapper;

import com.br.payments.api.dto.TipoDespesaDTO;
import com.br.payments.domain.model.TipoDespesa;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TipoDespesaMapper {
    TipoDespesaMapper INSTANCE = Mappers.getMapper(TipoDespesaMapper.class);
    TipoDespesa toModel(TipoDespesaDTO tipoDespesaDTO);
    TipoDespesaDTO toDTO(TipoDespesa tipoDespesa);
    List<TipoDespesaDTO> toDtoList(List<TipoDespesa> tiposDespesa);
}