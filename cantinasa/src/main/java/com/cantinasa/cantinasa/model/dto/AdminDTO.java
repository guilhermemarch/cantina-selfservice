package com.cantinasa.cantinasa.model.dto;

import jakarta.validation.constraints.NotNull;

public class AdminDTO {

    @NotNull(message = "ID do usuário é obrigatório")
    private Long userId;

    @NotNull(message = "Senha é obrigatória")
    private String password;

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
}
