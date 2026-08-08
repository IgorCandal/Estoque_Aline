package com.candall.estoque_db.models.dto;

import jakarta.validation.constraints.NotBlank;

public record CaixaRequestDto(

        @NotBlank
        String codigoCaixa

){}
