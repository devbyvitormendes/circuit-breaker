package br.com.byvitormendes.foodtosave.exception;

public class CepException extends RuntimeException {
  public CepException(String message) {
    super(message);
  }

  public CepException() {
    super("Erro ao buscar CEP.");
  }
}
