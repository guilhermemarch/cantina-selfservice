package com.cantinasa.cantinasa.repository;

import com.cantinasa.cantinasa.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByCategoria(String categoria);
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByPrecoBetween(BigDecimal min, BigDecimal max);
    List<Produto> findByValidadeBefore(LocalDate date);

    List<Produto> findByQuantidadeLessThan(int quantidade);

    @Query("SELECT p FROM Produto p WHERE p.quantidade < :limiteMinimo")
    List<Produto> findByQuantidadeBelow(@Param("limiteMinimo") int limiteMinimo);


    @Query(value = """
    SELECT ID, NOME, VALIDADE 
    FROM PRODUTOS 
    WHERE DATEDIFF('DAY', CURRENT_DATE, VALIDADE) BETWEEN 0 AND :dias
    """, nativeQuery = true)
    List<Object[]> proximoValidade(@Param("dias") int diasParaVencer);
}
