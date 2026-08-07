package com.candall.estoque_db.services;

import com.candall.estoque_db.models.Caixa;
import com.candall.estoque_db.models.dto.CaixaRequestDto;
import com.candall.estoque_db.models.dto.CaixaResponseDto;
import com.candall.estoque_db.models.dto.ProdutoResumoDto;
import com.candall.estoque_db.repositories.CaixaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaixaService {

    private final CaixaRepository caixaRepository;

    @Transactional
    public CaixaResponseDto criar(CaixaRequestDto dto){
        if(caixaRepository.existsByCodigoCaixa(dto.codigoCaixa())){
            throw new RuntimeException("Codigo de caixa já existe");
        }

        Caixa caixa = Caixa.builder().codigoCaixa(dto.codigoCaixa()).build();

        Caixa caixaSalva = caixaRepository.save(caixa);
        return converterParaResponse(caixaSalva);
    }

    private CaixaResponseDto converterParaResponse(Caixa caixa){
        List<ProdutoResumoDto> produtosDto = List.of();

        if(caixa.getProdutos() != null){
            produtosDto = caixa.getProdutos().stream().map(p -> new ProdutoResumoDto(
                    p.getId(),
                    p.getNome(),
                    p.getCategoria(),
                    p.getPreco(),
                    p.getQuantidade(),
                    p.getPrecoTotal(),
                    p.getImagemUrl()
            )).toList();
        }
        return new CaixaResponseDto(caixa.getId(), caixa.getCodigoCaixa(), produtosDto);
    }

}
