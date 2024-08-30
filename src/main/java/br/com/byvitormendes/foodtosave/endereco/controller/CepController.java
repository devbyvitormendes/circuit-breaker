package br.com.byvitormendes.foodtosave.endereco.controller;

import br.com.byvitormendes.foodtosave.endereco.command.EnderecoCommandHandler;
import br.com.byvitormendes.foodtosave.endereco.dto.EnderecoDto;
import br.com.byvitormendes.foodtosave.endereco.model.CriaEnderecoCommand;
import br.com.byvitormendes.foodtosave.endereco.model.BuscaCepQuery;
import br.com.byvitormendes.foodtosave.endereco.query.EnderecoQueryHandler;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/v1/cep")
public class CepController implements CepApi {

    private final EnderecoCommandHandler commandHandler;
    private final EnderecoQueryHandler queryHandler;

    @Override
    @GetMapping("/{cep}")
    public EnderecoDto buscaEnderecoPorCep(@PathVariable @Min(8) String cep) {
        BuscaCepQuery cepQuery = new BuscaCepQuery();
        cepQuery.setCep(cep);
        return queryHandler.handle(cepQuery);
    }

    @Override
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void criarEndereco(@RequestBody CriaEnderecoCommand command) {
        commandHandler.handle(command);
    }
}
