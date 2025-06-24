package com.cantinasa.cantinasa.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class ProdutoRequest {
    @NotNull(message = "ID do usuário é obrigatório")
    private Long userId;

    @NotNull(message = "Senha é obrigatória")
    private String password;

    @Valid
    @NotNull(message = "Dados do produto são obrigatórios")
    private ProdutoDTO produto;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
    }
} 