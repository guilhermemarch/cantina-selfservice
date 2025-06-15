package com.cantinasa.cantinasa.model;

import com.cantinasa.cantinasa.model.enums.categoria;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduto;

    @NotBlank(message = "precisa colocar nome")
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "precisa obrigatoriamente de preço")
    @Positive(message = "Preço must be positive")
    @Column(nullable = false)
    private Double preco;

    @NotNull(message = "Quantidade em estoque precisa")
    @Min(value = 0, message = "Quantidade em estoque nao pode ser negativa")
    @Column(name = "quantidade_estoque", nullable = false)
    private int quantidade_estoque;

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

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

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
}
