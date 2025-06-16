package com.cantinasa.cantinasa.model.mapper;

import com.cantinasa.cantinasa.model.Item_pedido;
import com.cantinasa.cantinasa.model.dto.ItemPedidoDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemPedidoMapper {

    public ItemPedidoDTO toDTO(Item_pedido item) {
        if (item == null) return null;
        
        ItemPedidoDTO dto = new ItemPedidoDTO();
        dto.setIdItem_pedido(item.getIdItem_pedido());
        dto.setPedidoId(item.getPedido().getIdPedido());
        dto.setProdutoId(item.getProduto().getIdProduto());
        dto.setQuantidade(item.getQuantidade());
        
        dto.setNomeProduto(item.getProduto().getNome());
        dto.setPrecoUnitario(item.getProduto().getPreco());
        dto.setSubtotal(item.getProduto().getPreco() * item.getQuantidade());
        
        return dto;
    }

    public Item_pedido toEntity(ItemPedidoDTO dto) {
        if (dto == null) return null;
        
        Item_pedido item = new Item_pedido();
        item.setIdItem_pedido(dto.getIdItem_pedido());
        item.setQuantidade(dto.getQuantidade());
        return item;
    }

    public List<ItemPedidoDTO> toDTOList(List<Item_pedido> itens) {
        if (itens == null) return null;
        return itens.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<Item_pedido> toEntityList(List<ItemPedidoDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
    }
} 