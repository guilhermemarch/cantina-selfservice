package com.cantinasa.cantinasa.model.mapper;

import com.cantinasa.cantinasa.model.Pedido;
import com.cantinasa.cantinasa.model.Usuario;
import com.cantinasa.cantinasa.model.dto.PedidoDTO;
import com.cantinasa.cantinasa.model.enums.status;
import com.cantinasa.cantinasa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    @Autowired
    private ItemPedidoMapper itemPedidoMapper;

    @Autowired
    private PagamentoMapper pagamentoMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public PedidoDTO toDTO(Pedido pedido) {
        if (pedido == null) return null;

        PedidoDTO dto = new PedidoDTO();
        dto.setIdPedido(pedido.getId());
        dto.setDataHora(pedido.getDataPedido());
        dto.setStatus(convertToDTOStatus(pedido.getStatus()));
        dto.setUsuarioId(pedido.getUsuario().getId());

        if (pedido.getItens() != null) {
            dto.setItens(itemPedidoMapper.toDTOList(pedido.getItens()));
        }

        if (pedido.getPagamento() != null) {
            dto.setPagamento(pagamentoMapper.toDTO(pedido.getPagamento()));
        }

        return dto;
    }

    public Pedido toEntity(PedidoDTO dto) {
        if (dto == null) return null;

        Pedido pedido = new Pedido();
        pedido.setId(dto.getIdPedido());
        pedido.setDataPedido(dto.getDataHora());
        pedido.setStatus(convertToEntityStatus(dto.getStatus()));

        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário com ID " + dto.getUsuarioId() + " não encontrado"));
            pedido.setUsuario(usuario);
        }

        if (dto.getItens() != null) {
            var itens = itemPedidoMapper.toEntityList(dto.getItens());
            itens.forEach(item -> item.setPedido(pedido));
            pedido.setItens(itens);
        }

        if (dto.getPagamento() != null) {
            var pagamento = pagamentoMapper.toEntity(dto.getPagamento());
            pagamento.setPedido(pedido);
            pedido.setPagamento(pagamento);
        }

        return pedido;
    }

    public List<PedidoDTO> toDTOList(List<Pedido> pedidos) {
        if (pedidos == null) return null;
        return pedidos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Pedido> toEntityList(List<PedidoDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    private status convertToDTOStatus(Pedido.Status statusEntity) {
        if (statusEntity == null) return null;
        return switch (statusEntity) {
            case PENDENTE -> status.PENDENTE;
            case CONFIRMADO -> status.EM_ANDAMENTO;
            case PREPARANDO -> status.EM_PREPARACAO;
            case PRONTO -> status.EM_ENTREGA;
            case ENTREGUE -> status.FINALIZADO;
            case CANCELADO -> status.CANCELADO;
        };
    }

    private Pedido.Status convertToEntityStatus(status statusDto) {
        if (statusDto == null) return Pedido.Status.PENDENTE;
        switch (statusDto) {
            case PENDENTE:
                return Pedido.Status.PENDENTE;
            case EM_ANDAMENTO:
                return Pedido.Status.CONFIRMADO;
            case EM_PREPARACAO:
                return Pedido.Status.PREPARANDO;
            case EM_ENTREGA:
                return Pedido.Status.PRONTO;
            case FINALIZADO:
                return Pedido.Status.ENTREGUE;
            case CANCELADO:
                return Pedido.Status.CANCELADO;
            default:
                return Pedido.Status.PENDENTE;
        }
    }
}
