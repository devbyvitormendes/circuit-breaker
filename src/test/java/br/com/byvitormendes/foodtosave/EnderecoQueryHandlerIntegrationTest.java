package br.com.byvitormendes.foodtosave;

import br.com.byvitormendes.foodtosave.endereco.dto.EnderecoDto;
import br.com.byvitormendes.foodtosave.endereco.model.BuscaCepQuery;
import br.com.byvitormendes.foodtosave.endereco.query.EnderecoQueryHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
class EnderecoQueryHandlerIntegrationTest {

    private final String CEP_VALIDO = "01312001";
    private final String CEP_INVALIDO = "11111111";

    @Autowired
    private EnderecoQueryHandler enderecoQueryHandler;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Objects.requireNonNull(cacheManager.getCache("enderecoCache")).clear();
    }

    @Test
    void handle_DeveRetornarEnderecoDoCacheQuandoDisponivel() {
        BuscaCepQuery query = new BuscaCepQuery();
        query.setCep(CEP_VALIDO);
        EnderecoDto enderecoDto = mockEndereco();

        Objects.requireNonNull(cacheManager.getCache("enderecoCache")).put(query.getCep(), enderecoDto);

        EnderecoDto resultado = enderecoQueryHandler.handle(query);

        assertEquals(enderecoDto, resultado, "O endereço retornado deve ser o mesmo que foi colocado no cache");
    }

    @Test
    void handle_DeveRetornarEnderecoDoRepositorioQuandoFalhaNaBuscaExterna() {
        BuscaCepQuery query = new BuscaCepQuery();
        query.setCep(CEP_INVALIDO);
        EnderecoDto enderecoDto = mockEndereco();

        EnderecoDto resultado = enderecoQueryHandler.handle(query);

        assertNotNull(resultado, "O endereço retornado não deve ser nulo");
        assertTrue(resultado.isPadrao(), "O endereço retornado deve ser o endereço padrão");
    }

    private EnderecoDto mockEndereco() {
        EnderecoDto dto = new EnderecoDto();
        dto.setCep(CEP_VALIDO);
        dto.setLogradouro("Avenida Nove de Julho");
        dto.setUnidade("1510");
        dto.setBairro("Bela Vista");
        dto.setLocalidade("São Paulo");
        dto.setUf("SP");
        dto.setPadrao(true);
        return dto;

    }

}

