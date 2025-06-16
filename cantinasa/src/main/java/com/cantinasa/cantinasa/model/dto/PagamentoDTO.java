package com.cantinasa.cantinasa.model.dto;

import com.cantinasa.cantinasa.model.enums.tipoPagamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
} 