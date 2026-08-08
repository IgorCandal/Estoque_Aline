package com.candall.estoque_db.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutoResponseDto(

        Long id,
        String nome,
        String categoria,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate validade,
        String imagemUrl,
        BigDecimal preco,
        Integer quantidade,
        BigDecimal precoTotal,
        Long caixaId

){}
