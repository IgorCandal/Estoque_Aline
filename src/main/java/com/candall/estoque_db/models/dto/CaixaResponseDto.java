package com.candall.estoque_db.models.dto;

public record CaixaResponseDto(

        Long id,
        String codigoCaixa,
        int quantidadeProdutos
){}
