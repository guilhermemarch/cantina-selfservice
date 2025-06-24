package com.cantinasa.cantinasa.model;

import com.cantinasa.cantinasa.model.enums.categoria;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "precisa colocar nome")
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @NotNull(message = "precisa obrigatoriamente de preço")
    @Positive(message = "Preço must be positive")
    @Column(nullable = false)
    private BigDecimal preco;

    @NotNull(message = "Quantidade em estoque precisa")
    @Min(value = 0, message = "Quantidade em estoque nao pode ser negativa")
    @Column(name = "quantidade_estoque", nullable = false)
    private int quantidade;

    @NotNull(message = "Estoque mínimo is required")
    @Min(value = 0, message = "Estoque mínimo cannot be negative")
    @Column(name = "estoque_minimo", nullable = false)
    private int estoque_minimo;

    @NotNull(message = "precisa ter validade")
    @Column(nullable = false)
    private LocalDate validade;

    @NotNull(message = "categoria nao pode ser nula")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private categoria categoria;

    @Column
    private String imagem;

    @Column
    private String imagemUrl;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Item_pedido> itens;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getEstoque_minimo() {
        return estoque_minimo;
    }

    public void setEstoque_minimo(int estoque_minimo) {
        this.estoque_minimo = estoque_minimo;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(categoria categoria) {
        this.categoria = categoria;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getImagemUrl() {
        return imagemUrl != null ? imagemUrl : "/images/default-product.png";
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public List<Item_pedido> getItens() {
        return itens;
    }

    public void setItens(List<Item_pedido> itens) {
        this.itens = itens;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Produto() {
    }
}
