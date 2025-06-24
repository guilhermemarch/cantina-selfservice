package com.cantinasa.cantinasa.model.mapper;

import com.cantinasa.cantinasa.model.Item_pedido;
import com.cantinasa.cantinasa.model.Produto;
import com.cantinasa.cantinasa.model.dto.ItemPedidoDTO;
import com.cantinasa.cantinasa.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemPedidoMapper {

    @Autowired
    private ProdutoRepository produtoRepository;

    public ItemPedidoDTO toDTO(Item_pedido item) {
        if (item == null) return null;

        ItemPedidoDTO dto = new ItemPedidoDTO();
        dto.setIdItem_pedido(item.getId());
        dto.setProdutoId(item.getProduto().getId());
        dto.setQuantidade(item.getQuantidade());
        dto.setNomeProduto(item.getProduto().getNome());
        dto.setPrecoUnitario(item.getPrecoUnitario().doubleValue());
        dto.setSubtotal(item.getSubtotal().doubleValue());

        return dto;
    }

    public Item_pedido toEntity(ItemPedidoDTO dto) {
        if (dto == null) return null;

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto com ID " + dto.getProdutoId() + " não encontrado"));

        Item_pedido item = new Item_pedido();
        item.setId(dto.getIdItem_pedido());
        item.setProduto(produto);
        item.setQuantidade(dto.getQuantidade());
        item.setPrecoUnitario(produto.getPreco());
        item.calcularSubtotal();

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
