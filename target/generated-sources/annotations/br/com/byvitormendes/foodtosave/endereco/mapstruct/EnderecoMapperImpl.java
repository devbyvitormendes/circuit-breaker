package br.com.byvitormendes.foodtosave.endereco.mapstruct;

import br.com.byvitormendes.foodtosave.endereco.dto.EnderecoDto;
import br.com.byvitormendes.foodtosave.endereco.model.Endereco;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-29T08:52:34-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class EnderecoMapperImpl extends EnderecoMapper {

    @Override
    public EnderecoDto toDto(Endereco entity) {
        if ( entity == null ) {
            return null;
        }

        EnderecoDto enderecoDto = new EnderecoDto();

        enderecoDto.setCep( entity.getCep() );
        enderecoDto.setLogradouro( entity.getLogradouro() );
        enderecoDto.setComplemento( entity.getComplemento() );
        enderecoDto.setUnidade( entity.getUnidade() );
        enderecoDto.setBairro( entity.getBairro() );
        enderecoDto.setLocalidade( entity.getLocalidade() );
        enderecoDto.setUf( entity.getUf() );
        enderecoDto.setIbge( entity.getIbge() );
        enderecoDto.setGia( entity.getGia() );
        enderecoDto.setDdd( entity.getDdd() );
        enderecoDto.setSiafi( entity.getSiafi() );
        enderecoDto.setPadrao( entity.isPadrao() );

        return enderecoDto;
    }

    @Override
    public Endereco toEntity(EnderecoDto dto) {
        if ( dto == null ) {
            return null;
        }

        Endereco endereco = new Endereco();

        endereco.setCep( dto.getCep() );
        endereco.setLogradouro( dto.getLogradouro() );
        endereco.setComplemento( dto.getComplemento() );
        endereco.setUnidade( dto.getUnidade() );
        endereco.setBairro( dto.getBairro() );
        endereco.setLocalidade( dto.getLocalidade() );
        endereco.setUf( dto.getUf() );
        endereco.setIbge( dto.getIbge() );
        endereco.setGia( dto.getGia() );
        endereco.setDdd( dto.getDdd() );
        endereco.setSiafi( dto.getSiafi() );
        endereco.setPadrao( dto.isPadrao() );

        return endereco;
    }
}
