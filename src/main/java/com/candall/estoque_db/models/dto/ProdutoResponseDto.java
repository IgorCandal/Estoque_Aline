package com.candall.estoque_db.models.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponseDto(

        Long id,
        String nome,
        String categoria,
        LocalDateTime validade,
        String imagemUrl,
        BigDecimal preco,
        Integer quantidade,
        BigDecimal precoTotal,
        Long caixaId

){}
