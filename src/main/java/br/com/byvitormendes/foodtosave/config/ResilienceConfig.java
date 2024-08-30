package br.com.byvitormendes.foodtosave.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResilienceConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(2) // Tamanho da janela deslizante
                .minimumNumberOfCalls(4) // Número mínimo de chamadas para ativar o circuito
                .failureRateThreshold(100) // Definir a taxa de falha para 100% para falhas consecutivas
                .waitDurationInOpenState(Duration.ofSeconds(20)) // Tempo de espera antes de tentar fechar o circuito
                .build();

        return CircuitBreakerRegistry.of(circuitBreakerConfig);
    }
}
