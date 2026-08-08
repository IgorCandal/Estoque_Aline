package com.candall.estoque_db.repositories;

import com.candall.estoque_db.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByCaixaId(Long caixaId);
    List<Produto> findByOrderByValidadeAsc();
    List<Produto> findAllByOrderByQuantidadeDesc();
    List<Produto> findAllByOrderByNomeAsc();
}
