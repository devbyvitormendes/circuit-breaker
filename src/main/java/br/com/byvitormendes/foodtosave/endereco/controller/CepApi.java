package br.com.byvitormendes.foodtosave.endereco.controller;

import br.com.byvitormendes.foodtosave.endereco.dto.EnderecoDto;
import br.com.byvitormendes.foodtosave.endereco.model.CriaEnderecoCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "CEP", description = "Operações relacionadas ao CEP")
public interface CepApi {

    @Operation(summary = "Busca endereço por CEP", description = "Busca um endereço pelo CEP informado.")
    EnderecoDto buscaEnderecoPorCep(String cep);

    @Operation(summary = "Cria endereço", description = "Cria um novo endereço.")
    @PostMapping()
    void criarEndereco(@RequestBody CriaEnderecoCommand command);
}
