package com.cantinasa.cantinasa.model.dto.relatorioSubDTOs;

import java.time.LocalDate;

public class ProdutoProximoValidadeDTO {

    private Long id;
    private String nome;
    private LocalDate validade;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public ProdutoProximoValidadeDTO() {
    }

    public ProdutoProximoValidadeDTO(Long id, String nome, LocalDate validade) {
        this.id = id;
        this.nome = nome;
        this.validade = validade;
    }
}
