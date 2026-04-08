package br.com.alura.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rua;
    private String logradouro;
    private String complemento;
    private Integer numero;

    Endereco() {

    }
    public Endereco(Long id, String rua, String logradouro, String complemento, Integer numero) {
        this.id = id;
        this.rua = rua;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public String getRua() {
        return rua;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
}
