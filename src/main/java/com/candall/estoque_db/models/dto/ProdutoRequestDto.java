package com.candall.estoque_db.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutoRequestDto (

        @NotBlank
        String nome,

        @NotNull
        String categoria,

        @NotNull
        LocalDate validade,

        @NotNull
        @Positive
        BigDecimal preco,

        @NotNull
        Integer quantidade,

        @NotNull
        Long caixaId,

        String imagemUrl
){}
