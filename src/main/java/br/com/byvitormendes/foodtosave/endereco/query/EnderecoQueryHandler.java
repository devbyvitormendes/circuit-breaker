package br.com.byvitormendes.foodtosave.endereco.query;

import br.com.byvitormendes.foodtosave.endereco.dto.EnderecoDto;
import br.com.byvitormendes.foodtosave.endereco.mapstruct.EnderecoMapper;
import br.com.byvitormendes.foodtosave.endereco.model.BuscaCepQuery;
import br.com.byvitormendes.foodtosave.endereco.repository.EnderecoRepository;
import br.com.byvitormendes.foodtosave.exception.CepException;
import br.com.byvitormendes.foodtosave.exception.CepNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnderecoQueryHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final RestTemplate restTemplate;
    private final EnderecoRepository repository;
    private final RedisCacheManager redisCacheManager;
    private final EnderecoMapper enderecoMapper;

    @Autowired
    public EnderecoQueryHandler(RestTemplateBuilder restTemplateBuilder, EnderecoRepository repository,
                                RedisCacheManager redisCacheManager, EnderecoMapper enderecoMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.repository = repository;
        this.redisCacheManager = redisCacheManager;
        this.enderecoMapper = enderecoMapper;
    }

    @Cacheable(value = "enderecoCache", key = "#query.cep")
    @CircuitBreaker(name = "endereco", fallbackMethod = "fallbackHandle")
    public EnderecoDto handle(BuscaCepQuery query) {
        log.info("Não encontrado no cache, iniciando busca externa de CEP");

        Object response = new Object();
        try {
            response = restTemplate.getForObject("https://viacep.com.br/ws/" + query.getCep() + "/json/", Object.class);
        } catch (Exception e) {
            log.error("Erro: {}", e.getMessage());
            throw new CepException();
        }

        if (Objects.nonNull(response)) {
            JsonNode jsonNode = objectMapper.convertValue(response, JsonNode.class);
            if (Objects.nonNull(jsonNode) && !jsonNode.has("erro")) {
                log.info("Endereço encontrado na busca externa de CEP: {}", jsonNode);
                return objectMapper.convertValue(jsonNode, EnderecoDto.class);
            } else {
                log.error("Erro: nenhum endereço retornado pelo ViaCEP");
                throw new CepNotFoundException();
            }
        } else {
            log.error("Erro: sem resposta da busca externa de CEP");
            throw new CepNotFoundException();
        }
    }

    public EnderecoDto fallbackHandle(BuscaCepQuery query, Throwable t) {
        log.warn("Iniciando fallback para busca de CEP: {}. Erro: {}", query.getCep(), t.getMessage());
        try {
            return enderecoMapper.toDto(repository.findEnderecoPadrao());
        } catch (Exception e) {
            throw new CepNotFoundException(e.getMessage());
        }
    }
}
