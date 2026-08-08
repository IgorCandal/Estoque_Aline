package com.candall.estoque_db.models.dto;

import java.util.List;

public record CaixaResponseDto(

        Long id,
        String codigoCaixa,
        List<ProdutoResumoDto> produtos

){}
