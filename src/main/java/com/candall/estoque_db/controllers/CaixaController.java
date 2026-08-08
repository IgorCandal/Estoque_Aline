package com.candall.estoque_db.controllers;

import com.candall.estoque_db.models.dto.CaixaRequestDto;
import com.candall.estoque_db.models.dto.CaixaResponseDto;
import com.candall.estoque_db.services.CaixaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caixas")
@RequiredArgsConstructor
public class CaixaController {

    private final CaixaService caixaService;

    @PostMapping
    public ResponseEntity<CaixaResponseDto> criar(@RequestParam String id) {
        CaixaRequestDto dto = new CaixaRequestDto(id);
        CaixaResponseDto novoCaixa = caixaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCaixa);
    }

    @GetMapping
    public ResponseEntity<List<CaixaResponseDto>> listarTodos() {
        return ResponseEntity.ok(caixaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaixaResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(caixaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CaixaResponseDto> renomear(@PathVariable Long id, @RequestBody @Valid CaixaRequestDto dto) {
        CaixaResponseDto caixaAtualizado = caixaService.atualizarCodigo(id, dto);
        return ResponseEntity.ok(caixaAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        caixaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
