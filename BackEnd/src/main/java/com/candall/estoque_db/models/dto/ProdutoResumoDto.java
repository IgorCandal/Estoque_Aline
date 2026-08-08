package com.candall.estoque_db.models.dto;

import java.math.BigDecimal;

public record ProdutoResumoDto(

        Long id,
        String nome,
        String categoria,
        BigDecimal preco,
        Integer quantidade,
        BigDecimal precoTotal,
        String imagemUrl

){}
