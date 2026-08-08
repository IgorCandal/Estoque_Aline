package com.candall.estoque_db.controllers;

import com.candall.estoque_db.models.dto.ProdutoRequestDto;
import com.candall.estoque_db.models.dto.ProdutoResponseDto;
import com.candall.estoque_db.services.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoResponseDto> criar(@RequestParam String nome,
                                                    @RequestParam String categoria,
                                                    @RequestParam String validade,
                                                    @RequestParam Double preco,
                                                    @RequestParam Integer quantidade,
                                                    @RequestParam Long ideCaixa,
                                                    @RequestParam(value = "imagem") MultipartFile imagem) throws IOException {

        ProdutoRequestDto dto = new ProdutoRequestDto(nome, categoria, LocalDate.parse(validade), BigDecimal.valueOf(preco), quantidade, ideCaixa);

        ProdutoResponseDto response = produtoService.criar(dto, imagem);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> buscarPorId(@PathVariable Long id) {
        ProdutoResponseDto response = produtoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filtro/validade")
    public ResponseEntity<List<ProdutoResponseDto>> listarPorValidade() {
        List<ProdutoResponseDto> response = produtoService.listarPorValidadeCrescente();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filtro/quantidade")
    public ResponseEntity<List<ProdutoResponseDto>> listarPorQuantidade() {
        List<ProdutoResponseDto> response = produtoService.listarPorQuantidadeDecrescente();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filtro/nome")
    public ResponseEntity<List<ProdutoResponseDto>> listarPorNome() {
        List<ProdutoResponseDto> response = produtoService.listarPorNomeCrescente();
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoResponseDto> atualizar(
            @RequestParam Long id,
            @RequestPart("produto") ProdutoRequestDto dto,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) throws IOException {

        ProdutoResponseDto response = produtoService.atualizar(id, dto, imagem);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
