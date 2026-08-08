package com.candall.estoque_db.repositories;

import com.candall.estoque_db.models.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {

    boolean existsByCodigoCaixa(String codigoCaixa);
}
