package br.com.byvitormendes.foodtosave.endereco.command;

import br.com.byvitormendes.foodtosave.endereco.model.CriaEnderecoCommand;
import br.com.byvitormendes.foodtosave.endereco.repository.EnderecoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnderecoCommandHandler {

    private final EnderecoRepository repository;

    @Transactional
    public void handle(CriaEnderecoCommand command) {
        repository.save(command);
    }

}
