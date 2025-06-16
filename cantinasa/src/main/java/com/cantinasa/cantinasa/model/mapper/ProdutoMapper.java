package com.cantinasa.cantinasa.model.mapper;

import com.cantinasa.cantinasa.model.Produto;
import com.cantinasa.cantinasa.model.dto.ProdutoDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdutoMapper {

    public ProdutoDTO toDTO(Produto produto) {
        if (produto == null) return null;
        
        return new ProdutoDTO(
            produto.getIdProduto(),
            produto.getNome(),
            produto.getPreco(),
            produto.getQuantidade_estoque(),
            produto.getEstoque_minimo(),
            produto.getValidade(),
            produto.getCategoria()
        );
    }

    public Produto toEntity(ProdutoDTO dto) {
        if (dto == null) return null;
        
        Produto produto = new Produto();
        produto.setIdProduto(dto.getIdProduto());
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setQuantidade_estoque(dto.getQuantidade_estoque());
        produto.setEstoque_minimo(dto.getEstoque_minimo());
        produto.setValidade(dto.getValidade());
        produto.setCategoria(dto.getCategoria());
        return produto;
    }

    public List<ProdutoDTO> toDTOList(List<Produto> produtos) {
        if (produtos == null) return null;
        return produtos.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<Produto> toEntityList(List<ProdutoDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
    }
}
