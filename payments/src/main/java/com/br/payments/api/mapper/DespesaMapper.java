package com.br.payments.api.mapper;

import com.br.payments.api.dto.DespesaDTO;
import com.br.payments.domain.model.Despesa;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DespesaMapper {
    DespesaMapper INSTANCE = Mappers.getMapper(DespesaMapper.class);
    Despesa toModel(DespesaDTO despesaDTO);
    DespesaDTO toDTO(Despesa despesa);
    List<DespesaDTO> toDtoList(List<Despesa> despesas);
}
