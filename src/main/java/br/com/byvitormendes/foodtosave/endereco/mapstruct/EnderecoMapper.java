package br.com.byvitormendes.foodtosave.endereco.mapstruct;

import br.com.byvitormendes.foodtosave.endereco.dto.EnderecoDto;
import br.com.byvitormendes.foodtosave.endereco.model.Endereco;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class EnderecoMapper {

    public abstract EnderecoDto toDto(Endereco entity);

    public abstract Endereco toEntity(EnderecoDto dto);
}
