package com.br.payments.api.mapper;

import com.br.payments.api.dto.FornecedorDTO;
import com.br.payments.domain.model.Fornecedor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FornecedorMapper {
    FornecedorMapper INSTANCE = Mappers.getMapper(FornecedorMapper.class);
    Fornecedor toModel(FornecedorDTO fornecedorDTO);
    FornecedorDTO toDTO(Fornecedor fornecedor);
    List<FornecedorDTO> toDtoList(List<Fornecedor> fornecedores);
}
