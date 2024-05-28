package com.br.payments.api.mapper;

import com.br.personaladm.api.dto.ContaDTO;
import com.br.personaladm.domain.model.Conta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ContaMapper {
    ContaMapper INSTANCE = Mappers.getMapper(ContaMapper.class);
//    @Mapping(target = "titulo", ignore = true)
    Conta toModel(ContaDTO contaDTO);
//    @Mapping(target = "titulo", ignore = true)
    ContaDTO toDTO(Conta conta);
    List<ContaDTO> toDtoList(List<Conta> contas);
}
