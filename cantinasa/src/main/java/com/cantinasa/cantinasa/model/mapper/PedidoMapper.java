package com.cantinasa.cantinasa.model.mapper;

import com.cantinasa.cantinasa.model.Pedido;
import com.cantinasa.cantinasa.model.dto.PedidoDTO;
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

    public PedidoDTO toDTO(Pedido pedido) {
        if (pedido == null) return null;
        
        PedidoDTO dto = new PedidoDTO();
        dto.setIdPedido(pedido.getIdPedido());
        dto.setData_hora(pedido.getData_hora());
        dto.setStatus(pedido.getStatus());
        dto.setUsuarioId(pedido.getUsuario().getIdUsuario());
        
        if (pedido.getItens() != null) {
            dto.setItens(itemPedidoMapper.toDTOList(pedido.getItens()));
        }
        
        if (pedido.getPagamentos() != null) {
            dto.setPagamentos(pagamentoMapper.toDTOList(pedido.getPagamentos()));
        }
        
        return dto;
    }

    public Pedido toEntity(PedidoDTO dto) {
        if (dto == null) return null;
        
        Pedido pedido = new Pedido();
        pedido.setIdPedido(dto.getIdPedido());
        pedido.setData_hora(dto.getData_hora());
        pedido.setStatus(dto.getStatus());
        
        if (dto.getItens() != null) {
            pedido.setItens(itemPedidoMapper.toEntityList(dto.getItens()));
        }
        
        if (dto.getPagamentos() != null) {
            pedido.setPagamentos(pagamentoMapper.toEntityList(dto.getPagamentos()));
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
} 