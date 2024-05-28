package com.br.payments.api.mapper;

import com.br.payments.api.dto.ContaDTO;
import com.br.payments.domain.model.Conta;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ContaMapper {
    ContaMapper INSTANCE = Mappers.getMapper(ContaMapper.class);
    Conta toModel(ContaDTO contaDTO);
    ContaDTO toDTO(Conta conta);
    List<ContaDTO> toDtoList(List<Conta> contas);
}
