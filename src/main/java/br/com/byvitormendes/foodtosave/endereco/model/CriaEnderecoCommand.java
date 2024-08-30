package br.com.byvitormendes.foodtosave.endereco.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriaEnderecoCommand {

    private String cep;
    private String logradouro;
    private String complemento;
    private String unidade;
    private String bairro;
    private String localidade;
    private String uf;

}
