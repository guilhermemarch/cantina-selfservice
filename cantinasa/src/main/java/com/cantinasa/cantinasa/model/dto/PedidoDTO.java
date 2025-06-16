package com.cantinasa.cantinasa.model.dto;

import com.cantinasa.cantinasa.model.enums.status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


public class PedidoDTO {
    private Long idPedido;

    @NotNull(message = "Data e hora é obrigatória")
    private LocalDateTime data_hora;

    @NotNull(message = "Status é obrigatório")
    private status status;

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;

    private List<ItemPedidoDTO> itens;
    private List<PagamentoDTO> pagamentos;

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDateTime getData_hora() {
        return data_hora;
    }

    public void setData_hora(LocalDateTime data_hora) {
        this.data_hora = data_hora;
    }

    public status getStatus() {
        return status;
    }

    public void setStatus(status status) {
        this.status = status;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }

    public List<PagamentoDTO> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<PagamentoDTO> pagamentos) {
        this.pagamentos = pagamentos;
    }

    public PedidoDTO() {
    }

    public PedidoDTO(Long idPedido, LocalDateTime data_hora, status status, Long usuarioId, List<ItemPedidoDTO> itens, List<PagamentoDTO> pagamentos) {
        this.idPedido = idPedido;
        this.data_hora = data_hora;
        this.status = status;
        this.usuarioId = usuarioId;
        this.itens = itens;
        this.pagamentos = pagamentos;
    }
}
