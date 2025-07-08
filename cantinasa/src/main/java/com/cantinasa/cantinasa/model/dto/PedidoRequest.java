package com.cantinasa.cantinasa.model.dto;

import com.cantinasa.cantinasa.model.enums.tipoPagamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoRequest {
    
    @NotNull(message = "Data e hora é obrigatória")
    private LocalDateTime dataHora;
    
    @NotNull(message = "Status é obrigatório")
    private String status;
    
    @NotNull(message = "ID do usuário é obrigatório")
    @Positive(message = "ID do usuário deve ser positivo")
    private Long usuarioId;
    
    @NotNull(message = "Itens são obrigatórios")
    private List<ItemPedidoRequest> itens;
    
    @NotNull(message = "Pagamento é obrigatório")
    private PagamentoRequest pagamento;




    public PedidoRequest() {}
    
    public PedidoRequest(LocalDateTime dataHora, String status, Long usuarioId, 
                        List<ItemPedidoRequest> itens, PagamentoRequest pagamento) {
        this.dataHora = dataHora;
        this.status = status;
        this.usuarioId = usuarioId;
        this.itens = itens;
        this.pagamento = pagamento;
    }
    
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public List<ItemPedidoRequest> getItens() {
        return itens;
    }
    
    public void setItens(List<ItemPedidoRequest> itens) {
        this.itens = itens;
    }
    
    public PagamentoRequest getPagamento() {
        return pagamento;
    }
    
    public void setPagamento(PagamentoRequest pagamento) {
        this.pagamento = pagamento;
    }
    
    public static class ItemPedidoRequest {
        @NotNull(message = "ID do produto é obrigatório")
        @Positive(message = "ID do produto deve ser positivo")
        private Long produtoId;
        
        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "Quantidade deve ser positiva")
        private Integer quantidade;
        
        public ItemPedidoRequest() {}
        
        public ItemPedidoRequest(Long produtoId, Integer quantidade) {
            this.produtoId = produtoId;
            this.quantidade = quantidade;
        }
        
        public Long getProdutoId() {
            return produtoId;
        }
        
        public void setProdutoId(Long produtoId) {
            this.produtoId = produtoId;
        }
        
        public Integer getQuantidade() {
            return quantidade;
        }
        
        public void setQuantidade(Integer quantidade) {
            this.quantidade = quantidade;
        }
    }
    
    public static class PagamentoRequest {
        @NotNull(message = "Tipo de pagamento é obrigatório")
        private String tipoPagamento;
        
        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        private Double valor;
        
        public PagamentoRequest() {}
        
        public PagamentoRequest(String tipoPagamento, Double valor) {
            this.tipoPagamento = tipoPagamento;
            this.valor = valor;
        }
        
        public String getTipoPagamento() {
            return tipoPagamento;
        }
        
        public void setTipoPagamento(String tipoPagamento) {
            this.tipoPagamento = tipoPagamento;
        }
        
        public Double getValor() {
            return valor;
        }
        
        public void setValor(Double valor) {
            this.valor = valor;
        }
    }
} 