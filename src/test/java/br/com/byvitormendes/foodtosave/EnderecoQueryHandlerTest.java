package br.com.byvitormendes.foodtosave;

import br.com.byvitormendes.foodtosave.endereco.dto.EnderecoDto;
import br.com.byvitormendes.foodtosave.endereco.mapstruct.EnderecoMapper;
import br.com.byvitormendes.foodtosave.endereco.model.BuscaCepQuery;
import br.com.byvitormendes.foodtosave.endereco.query.EnderecoQueryHandler;
import br.com.byvitormendes.foodtosave.endereco.repository.EnderecoRepository;
import br.com.byvitormendes.foodtosave.exception.CepException;
import br.com.byvitormendes.foodtosave.exception.CepNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnderecoQueryHandlerTest {

    private final String CEP_VALIDO = "01312001";
    private final String CEP_INVALIDO = "11111111";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private RedisCacheManager redisCacheManager;

    @Mock
    private EnderecoMapper enderecoMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private EnderecoQueryHandler enderecoQueryHandler;

    @BeforeEach
    void setUp() {
        enderecoQueryHandler = new EnderecoQueryHandler(restTemplate, enderecoRepository, redisCacheManager, enderecoMapper);
    }

    @Test
    void handle_DeveRetornarEnderecoQuandoSucesso() throws Exception {
        BuscaCepQuery query = new BuscaCepQuery();
        query.setCep(CEP_VALIDO);
        JsonNode jsonNode = objectMapper.readTree("{\"cep\":\"01312001\"," +
                "\"logradouro\":\"Avenida Nove de Julho\"," +
                "\"complemento\":null," +
                "\"unidade\":\"1510\"," +
                "\"bairro\":\"Bela Vista\"," +
                "\"localidade\":\"São Paulo\"," +
                "\"uf\":\"SP\"," +
                "\"ibge\":null," +
                "\"gia\":null," +
                "\"ddd\":null," +
                "\"siafi\":null," +
                "\"padrao\":true}");

        when(restTemplate.getForObject(anyString(), any())).thenReturn(jsonNode);

        EnderecoDto resultado = enderecoQueryHandler.handle(query);

        assertNotNull(resultado);
        verify(restTemplate).getForObject("https://viacep.com.br/ws/" + CEP_VALIDO + "/json/", Object.class);
    }

    @Test
    void handle_DeveLancarCepExceptionQuandoErroNaBusca() {
        BuscaCepQuery query = new BuscaCepQuery();
        query.setCep(CEP_INVALIDO);
        when(restTemplate.getForObject(anyString(), any())).thenThrow(new RestClientException("Erro na consulta ViaCEP"));

        assertThrows(CepException.class, () -> enderecoQueryHandler.handle(query));
        verify(restTemplate).getForObject("https://viacep.com.br/ws/" + CEP_INVALIDO + "/json/", Object.class);
    }

    @Test
    void handle_DeveLancarCepNotFoundExceptionQuandoRespostaNull() {
        BuscaCepQuery query = new BuscaCepQuery();
        query.setCep(CEP_INVALIDO);
        when(restTemplate.getForObject(anyString(), any())).thenReturn(null);

        assertThrows(CepNotFoundException.class, () -> enderecoQueryHandler.handle(query));
        verify(restTemplate).getForObject("https://viacep.com.br/ws/" + CEP_INVALIDO + "/json/", Object.class);
    }

    @Test
    void handle_DeveLancarCepNotFoundExceptionQuandoJsonNodeTemErro() throws Exception {
        BuscaCepQuery query = new BuscaCepQuery();
        query.setCep(CEP_INVALIDO);
        JsonNode jsonNode = objectMapper.readTree("{\"erro\":\"true\"}");

        when(restTemplate.getForObject(anyString(), any())).thenReturn(jsonNode);

        assertThrows(CepNotFoundException.class, () -> enderecoQueryHandler.handle(query));
        verify(restTemplate).getForObject("https://viacep.com.br/ws/" + CEP_INVALIDO + "/json/", Object.class);
    }

    @Test
    void fallbackHandle_DeveRetornarEnderecoPadraoQuandoFalhaNaBuscaExterna() {
        BuscaCepQuery query = new BuscaCepQuery();
        query.setCep(CEP_VALIDO);
        EnderecoDto enderecoDto = mock(EnderecoDto.class);

        when(enderecoMapper.toDto(any())).thenReturn(enderecoDto);

        EnderecoDto resultado = enderecoQueryHandler.fallbackHandle(query, new Exception());

        assertNotNull(resultado);
        verify(enderecoMapper).toDto(enderecoRepository.findEnderecoPadrao());
    }

    @Test
    void fallbackHandle_DeveLancarCepNotFoundExceptionQuandoFalhaNoMappingDoEnderecoPadrao() {
        BuscaCepQuery query = new BuscaCepQuery();
        query.setCep(CEP_VALIDO);

        when(enderecoMapper.toDto(any())).thenThrow(new RuntimeException());

        assertThrows(CepNotFoundException.class, () -> enderecoQueryHandler.fallbackHandle(query, new RuntimeException()));
        verify(enderecoMapper).toDto(enderecoRepository.findEnderecoPadrao());
    }

}

