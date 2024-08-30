package br.com.byvitormendes.foodtosave.endereco.repository;

import br.com.byvitormendes.foodtosave.endereco.model.CriaEnderecoCommand;
import br.com.byvitormendes.foodtosave.endereco.model.Endereco;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EnderecoRepository {

    private final JdbcTemplate jdbcTemplate;

    public void save(CriaEnderecoCommand command) {
        jdbcTemplate.update("INSERT INTO ENDERECO " +
                        "(CEP, LOGRADOURO, COMPLEMENTO, UNIDADE, BAIRRO, LOCALIDADE, UF) VALUES (?, ?, ?, ?, ?, ?) ",
                command.getCep(), command.getLogradouro(), command.getComplemento(), command.getUnidade(),
                command.getBairro(), command.getLocalidade(), command.getUf());
    }

    public Endereco findEnderecoPadrao() {
        return jdbcTemplate.queryForObject("select * from endereco where padrao is true",
                new BeanPropertyRowMapper<>(Endereco.class));
    }

}
