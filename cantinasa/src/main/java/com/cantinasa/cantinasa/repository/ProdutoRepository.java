package com.cantinasa.cantinasa.repository;

import com.cantinasa.cantinasa.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByCategoria(String categoria);
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByPrecoBetween(BigDecimal min, BigDecimal max);

    List<Produto> findByValidadeBefore(LocalDate date);

    List<Produto> findByQuantidadeLessThan(int quantidade);
}
