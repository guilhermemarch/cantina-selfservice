package com.cantinasa.cantinasa.repository;

import com.cantinasa.cantinasa.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Repositório de pedidos com consultas customizadas.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByDataPedidoBetween(LocalDateTime start, LocalDateTime end);
    List<Pedido> findByStatus(Pedido.Status status);

    @Query(value = """
    SELECT DATE_TRUNC('hour', i.created_at) AS hora, 
           SUM(i.subtotal) AS total_vendido
    FROM itens_pedido i
    WHERE CAST(i.created_at AS DATE) = :data
    GROUP BY hora
    ORDER BY total_vendido DESC
    LIMIT 1
""", nativeQuery = true)
    Collection<Object[]> horarioPico(@Param("data") LocalDate data);

}
