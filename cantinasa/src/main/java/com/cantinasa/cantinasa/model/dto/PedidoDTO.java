package com.cantinasa.cantinasa.model.dto;

import com.cantinasa.cantinasa.model.enums.status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
