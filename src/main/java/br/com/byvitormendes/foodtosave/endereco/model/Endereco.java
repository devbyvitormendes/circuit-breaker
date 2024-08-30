package br.com.byvitormendes.foodtosave.endereco.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
public class Endereco implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String cep;
    @Column
    private String logradouro;
    @Column
    private String complemento;
    @Column
    private String unidade;
    @Column
    private String bairro;
    @Column
    private String localidade;
    @Column
    private String uf;
    @Column
    private String ibge;
    @Column
    private String gia;
    @Column
    private String ddd;
    @Column
    private String siafi;
    @Column
    private boolean padrao;
}
