package com.candall.estoque_db.repositories;

import com.candall.estoque_db.models.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {

    Optional<Caixa> findByCodigoCaixa(String codigoCaixa);
    boolean existsByCodigoCaixa(String codigoCaixa);
}
