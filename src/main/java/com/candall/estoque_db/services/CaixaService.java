package com.candall.estoque_db.services;

import com.candall.estoque_db.exceptions.ResourceAlreadyExistsException;
import com.candall.estoque_db.exceptions.ResourceNotFoundException;
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
            throw new ResourceAlreadyExistsException("Código de caixa " + dto.codigoCaixa() + " já existe.");
        }

        Caixa caixa = Caixa.builder().codigoCaixa(dto.codigoCaixa()).build();
        Caixa caixaSalva = caixaRepository.save(caixa);
        return converterParaResponse(caixaSalva);
    }

    @Transactional(readOnly = true)
    public List<CaixaResponseDto> listarTodos() {

        return caixaRepository.findAll()
                              .stream()
                              .map(this::converterParaResponse)
                              .toList();
    }

    @Transactional(readOnly = true)
    public CaixaResponseDto buscarPorId(Long id) {

        Caixa caixa = caixaRepository.findById(id)
                                     .orElseThrow(() -> new ResourceNotFoundException("A caixa com esse nome/ID " + id + " não existe."));

        return converterParaResponse(caixa);
    }

    @Transactional
    public void deletar(Long id) {

        Caixa caixa = caixaRepository.findById(id)
                                     .orElseThrow(() -> new ResourceNotFoundException("Não existe uma caixa com esse nome/ID " + id + " para deletar."));

        if (caixa.getProdutos() != null && !caixa.getProdutos().isEmpty()) {
            throw new ResourceAlreadyExistsException("Não é possível deletar uma caixa que possui produtos");
        }

        caixaRepository.delete(caixa);
    }

    @Transactional
    public CaixaResponseDto atualizarCodigo(Long id, CaixaRequestDto dto) {

        Caixa caixa = caixaRepository.findById(id)
                                     .orElseThrow(() -> new ResourceNotFoundException("Caixa não encontrada"));

        if (!caixa.getCodigoCaixa().equals(dto.codigoCaixa()) &&
                caixaRepository.existsByCodigoCaixa(dto.codigoCaixa())) {

            throw new ResourceAlreadyExistsException("O código " + id + " já existe");
        }

        caixa.setCodigoCaixa(dto.codigoCaixa());

        return converterParaResponse(caixa);
    }

    private CaixaResponseDto converterParaResponse(Caixa caixa){
        return new CaixaResponseDto(caixa.getId(), caixa.getCodigoCaixa());
    }
}
