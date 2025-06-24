package com.cantinasa.cantinasa.model.dto;

import com.cantinasa.cantinasa.model.enums.tipoPagamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PagamentoDTO {
    private Long idPagamento;

    @NotNull(message = "Tipo de pagamento é obrigatório")
    private tipoPagamento tipoPagamento;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private double valor;

    private Double troco;

    @NotNull(message = "ID do pedido é obrigatório")
    private Long pedidoId;

    public Long getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(Long idPagamento) {
        this.idPagamento = idPagamento;
    }

    public tipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(tipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Double getTroco() {
        return troco;
    }

    public void setTroco(Double troco) {
        this.troco = troco;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public PagamentoDTO() {
    }
}