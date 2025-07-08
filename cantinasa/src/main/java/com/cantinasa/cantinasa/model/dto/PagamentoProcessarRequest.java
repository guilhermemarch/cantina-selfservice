package com.cantinasa.cantinasa.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PagamentoProcessarRequest {
    
    @NotNull(message = "Pedido é obrigatório")
    private PedidoRequest pedido;
    
    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private Double valor;
    
    @NotNull(message = "Método é obrigatório")
    private String metodo;
    
    private String codigo_pix;
    
    private Double troco;
    
    public PagamentoProcessarRequest() {}
    
    public PagamentoProcessarRequest(PedidoRequest pedido, Double valor, String metodo, 
                                   String codigo_pix, Double troco) {
        this.pedido = pedido;
        this.valor = valor;
        this.metodo = metodo;
        this.codigo_pix = codigo_pix;
        this.troco = troco;
    }
    
    public PedidoRequest getPedido() {
        return pedido;
    }
    
    public void setPedido(PedidoRequest pedido) {
        this.pedido = pedido;
    }
    
    public Double getValor() {
        return valor;
    }
    
    public void setValor(Double valor) {
        this.valor = valor;
    }
    
    public String getMetodo() {
        return metodo;
    }
    
    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
    
    public String getCodigo_pix() {
        return codigo_pix;
    }
    
    public void setCodigo_pix(String codigo_pix) {
        this.codigo_pix = codigo_pix;
    }
    
    public Double getTroco() {
        return troco;
    }
    
    public void setTroco(Double troco) {
        this.troco = troco;
    }
    
    public static class PedidoRequest {
        @NotNull(message = "ID do pedido é obrigatório")
        @Positive(message = "ID do pedido deve ser positivo")
        private Long id;
        
        public PedidoRequest() {}
        
        public PedidoRequest(Long id) {
            this.id = id;
        }
        
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
    }
} 