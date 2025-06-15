package com.cantinasa.cantinasa.repository;

import com.cantinasa.cantinasa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Usuario, Long> {

}
