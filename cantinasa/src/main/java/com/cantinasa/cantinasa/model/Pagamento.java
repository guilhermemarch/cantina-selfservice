package com.cantinasa.cantinasa.model;

import com.cantinasa.cantinasa.model.enums.tipoPagamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")

public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPagamento;

    @NotNull(message = "Tipo de pagamento nao pode ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pagamento", nullable = false)
    private tipoPagamento tipoPagamento;

    @NotNull(message = "Valor nao pode ser nulo")
    @Positive(message = "Valor precisa ser positivo")
    @Column(nullable = false)
    private double valor;

    @Column
    private Double troco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Pagamento(Long idPagamento, tipoPagamento tipoPagamento, double valor, Double troco, Pedido pedido, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idPagamento = idPagamento;
        this.tipoPagamento = tipoPagamento;
        this.valor = valor;
        this.troco = troco;
        this.pedido = pedido;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Pagamento() {
    }
}
