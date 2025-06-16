package com.cantinasa.cantinasa.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO {
    private Long idItem_pedido;

    @NotNull(message = "ID do pedido é obrigatório")
    private Long pedidoId;

    @NotNull(message = "ID do produto é obrigatório")
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private int quantidade;

    private String nomeProduto;
    private Double precoUnitario;
    private Double subtotal;
} 