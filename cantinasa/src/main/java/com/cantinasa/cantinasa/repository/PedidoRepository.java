package com.cantinasa.cantinasa.repository;

import com.cantinasa.cantinasa.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByDataPedidoBetween(LocalDateTime start, LocalDateTime end);
    List<Pedido> findByStatus(Pedido.Status status);
}
