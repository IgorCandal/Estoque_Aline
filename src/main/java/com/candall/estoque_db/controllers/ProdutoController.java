package com.candall.estoque_db.controllers;

import com.candall.estoque_db.models.dto.ProdutoResponseDto;
import com.candall.estoque_db.services.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoResponseDto> criar(
            @RequestPart("produto") ProdutoResponseDto dto,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) {

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
            @PathVariable Long id,
            @RequestPart("produto") ProdutoResponseDto dto,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) {

        ProdutoResponseDto response = produtoService.atualizar(id, dto, imagem);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
