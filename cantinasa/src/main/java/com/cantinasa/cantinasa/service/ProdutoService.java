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

/**
 * Camada de serviços responsável por manipular entidades {@link Produto} e
 * realizar regras de negócio relacionadas.
 */
@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    /**
     * Retorna todos os produtos cadastrados.
     *
     * @return lista de produtos
     */
    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    /**
     * Busca produtos pela categoria informada.
     *
     * @param categoria categoria do produto
     * @return lista de produtos da categoria
     */
    public List<Produto> findByCategoria(String categoria) {
        return produtoRepository.findByCategoria(categoria);
    }

    /**
     * Pesquisa produtos pelo nome.
     *
     * @param nome parte do nome para pesquisa
     * @return lista de produtos correspondentes
     */
    public List<Produto> findByNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    /**
     * Obtém um produto pelo identificador.
     *
     * @param id identificador do produto
     * @return entidade encontrada
     */
    public Produto findById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    /**
     * Persiste um novo produto.
     *
     * @param produto entidade a ser salva
     * @return produto salvo
     */
    @Transactional
    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }

    /**
     * Remove um produto pelo id.
     *
     * @param id identificador do produto
     */
    @Transactional
    public void delete(Long id) {
        produtoRepository.deleteById(id);
    }

    /**
     * Realiza busca filtrando por texto e categoria.
     */
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

    /**
     * Atualiza a quantidade em estoque de um produto.
     */
    @Transactional
    public void updateStock(Long id, int quantidade) {
        Produto produto = findById(id);
        produto.setQuantidade(quantidade);
        produtoRepository.save(produto);
    }

    /**
     * Atualiza o preço do produto.
     */
    @Transactional
    public void updatePrice(Long id, double preco) {
        Produto produto = findById(id);
        produto.setPreco(BigDecimal.valueOf(preco));
        produtoRepository.save(produto);
    }

    /**
     * Cadastra um novo produto baseado em um {@link ProdutoDTO}.
     */
    @Transactional
    public ProdutoDTO cadastrarProduto(@Valid ProdutoDTO produtoDTO) {
        produtoDTO.setIdProduto(null);
        Produto produtoSalvo = ProdutoMapper.toEntity(produtoDTO);
        produtoSalvo = produtoRepository.save(produtoSalvo);
        return ProdutoMapper.toDTO(produtoSalvo);
    }

    /**
     * Lista todos os produtos convertidos em DTO.
     */
    public List<ProdutoDTO> listarProdutos() {
        return produtoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca um produto por id retornando um DTO.
     */
    public ProdutoDTO buscarProdutoPorId(Long id) {
        
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));
        return convertToDTO(produto);
    }

    /**
     * Atualiza os dados de um produto existente.
     */
    @Transactional
    public ProdutoDTO atualizarProduto(Long id, @Valid ProdutoDTO produtoDTO) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));

        produto.setNome(produtoDTO.getNome());
        produto.setDescricao(produtoDTO.getDescricao());
        produto.setPreco(java.math.BigDecimal.valueOf(produtoDTO.getPreco()));
        produto.setQuantidade(produtoDTO.getQuantidade_estoque());
        produto.setEstoque_minimo(produtoDTO.getEstoque_minimo());
        produto.setValidade(produtoDTO.getValidade());
        produto.setCategoria(produtoDTO.getCategoria());
        produto.setImagemUrl(produtoDTO.getImagemUrl());
        Produto produtoAtualizado = produtoRepository.save(produto);
        return convertToDTO(produtoAtualizado);
    }

    /**
     * Remove definitivamente um produto do banco de dados.
     */
    @Transactional
    public void removerProduto(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new EntityNotFoundException("Produto não encontrado com ID: " + id);
        }
        produtoRepository.deleteById(id);
    }

    /**
     * Incrementa ou decrementa a quantidade de um produto.
     */
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

    /** Converte uma entidade em DTO. */
    private ProdutoDTO convertToDTO(Produto produto) {
        return ProdutoMapper.toDTO(produto);
    }

    /**
     * Salva um novo produto.
     */
    @Transactional
    public Produto create(Produto produto) {
        return produtoRepository.save(produto);
    }

    /**
     * Atualiza um produto existente.
     */
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
