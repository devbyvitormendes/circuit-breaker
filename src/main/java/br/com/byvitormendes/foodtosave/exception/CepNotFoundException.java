package br.com.byvitormendes.foodtosave.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CepNotFoundException extends RuntimeException {
    public CepNotFoundException(String message) {
        super(message);
    }

    public CepNotFoundException() {
        super("CEP não encontrado.");
    }
}
