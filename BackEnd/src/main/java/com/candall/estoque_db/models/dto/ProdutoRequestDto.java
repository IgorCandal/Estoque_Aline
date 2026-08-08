package com.candall.estoque_db.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoRequestDto (

        @NotBlank
        String nome,

        @NotNull
        String categoria,
        @NotNull
        LocalDateTime validade,
        @NotNull
        @Positive
        BigDecimal preco,
        @NotNull
        Integer quantidade,
        @NotNull
        Long caixaId,
        MultipartFile imagem

){}
