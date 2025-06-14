package com.cantinasa.cantinasa.model.enums;

public enum status {
    PENDENTE("Pendente"),
    EM_PREPARACAO("Em Preparação"),
    PRONTO("Pronto"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado");

    private final String descricao;

    status(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}


