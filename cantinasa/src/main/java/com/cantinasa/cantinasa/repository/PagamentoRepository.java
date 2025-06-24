package com.cantinasa.cantinasa.repository;

import com.cantinasa.cantinasa.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {}
 