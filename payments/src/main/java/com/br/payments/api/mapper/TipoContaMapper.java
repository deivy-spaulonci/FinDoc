package com.br.payments.api.mapper;

import com.br.payments.api.dto.TipoContaDTO;
import com.br.payments.domain.model.TipoConta;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TipoContaMapper {
    TipoContaMapper INSTANCE = Mappers.getMapper(TipoContaMapper.class);
    TipoConta toModel(TipoContaDTO tipoContaDTO);
    TipoContaDTO toDTO(TipoConta tipoConta);
    List<TipoContaDTO> toDtoList(List<TipoConta> tipoContas);
}
