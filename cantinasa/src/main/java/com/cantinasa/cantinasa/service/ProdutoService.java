package com.cantinasa.cantinasa.service;

import com.cantinasa.cantinasa.model.Produto;
import com.cantinasa.cantinasa.model.dto.ProdutoDTO;
import com.cantinasa.cantinasa.model.mapper.ProdutoMapper;
import com.cantinasa.cantinasa.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    public List<Produto> findByCategoria(String categoria) {
        return produtoRepository.findByCategoria(categoria);
    }

    public List<Produto> findByNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Produto findById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    @Transactional
    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Transactional
    public void delete(Long id) {
        produtoRepository.deleteById(id);
    }

    public List<Produto> findBySearchAndCategory(String searchText, String category) {
        List<Produto> produtos = produtoRepository.findAll();
        
        return produtos.stream()
            .filter(produto -> {
                boolean matchesSearch = searchText == null || searchText.isEmpty() ||
                    produto.getNome().toLowerCase().contains(searchText.toLowerCase());
                
                boolean matchesCategory = category == null || category.equals("Todos") ||
                    produto.getCategoria().equals(category);
                
                return matchesSearch && matchesCategory;
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateStock(Long id, int quantidade) {
        Produto produto = findById(id);
        produto.setQuantidade(quantidade);
        produtoRepository.save(produto);
    }

    @Transactional
    public void updatePrice(Long id, double preco) {
        Produto produto = findById(id);
        produto.setPreco(BigDecimal.valueOf(preco));
        produtoRepository.save(produto);
    }

    @Transactional
    public ProdutoDTO cadastrarProduto(@Valid ProdutoDTO produtoDTO) {

        Produto produtoSalvo = produtoMapper.toEntity(produtoDTO);
        produtoSalvo = produtoRepository.save(produtoSalvo);
        return convertToDTO(produtoSalvo);
    }

    public List<ProdutoDTO> listarProdutos() {
        return produtoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProdutoDTO buscarProdutoPorId(Long id) {
        
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
        return convertToDTO(produto);
    }

    @Transactional
    public ProdutoDTO atualizarProduto(Long id, @Valid ProdutoDTO produtoDTO) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));

        Produto produtoAtualizado = produtoMapper.toEntity(produtoDTO);

        produtoAtualizado = produtoRepository.save(produtoAtualizado);
        return convertToDTO(produtoAtualizado);
    }

    @Transactional
    public void removerProduto(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new EntityNotFoundException("Produto não encontrado com ID: " + id);
        }
        produtoRepository.deleteById(id);
    }

    @Transactional
    public ProdutoDTO atualizarEstoque(Long id, int quantidade) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));

        int novaQuantidade = produto.getQuantidade() + quantidade;
        if (novaQuantidade < 0) {
            throw new IllegalStateException("Quantidade em estoque não pode ser negativa");
        }

        produto.setQuantidade(novaQuantidade);
        Produto produtoAtualizado = produtoRepository.save(produto);
        return convertToDTO(produtoAtualizado);
    }

    private ProdutoDTO convertToDTO(Produto produto) {
        ProdutoDTO ProdutoDTO = produtoMapper.toDTO(produto);
        return ProdutoDTO;
    }

    @Transactional
    public Produto create(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Transactional
    public Produto update(Long id, Produto produto) {
        Produto existingProduto = findById(id);
        existingProduto.setNome(produto.getNome());
        existingProduto.setDescricao(produto.getDescricao());
        existingProduto.setPreco(produto.getPreco());
        existingProduto.setQuantidade(produto.getQuantidade());
        existingProduto.setEstoque_minimo(produto.getEstoque_minimo());
        existingProduto.setValidade(produto.getValidade());
        existingProduto.setCategoria(produto.getCategoria());
        return produtoRepository.save(existingProduto);
    }

}
