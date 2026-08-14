package com.candall.estoque_db.controllers;

import com.candall.estoque_db.models.dto.ProdutoRequestDto;
import com.candall.estoque_db.models.dto.ProdutoResponseDto;
import com.candall.estoque_db.services.CaixaService;
import com.candall.estoque_db.services.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoViewController {

    private final ProdutoService produtoService;
    private final CaixaService caixaService;

    @GetMapping
    public String listar(Model model, @RequestParam(value = "filtro", defaultValue = "nome") String filtro) {
        List<ProdutoResponseDto> produtos = switch (filtro) {
            case "validade" -> produtoService.listarPorValidadeCrescente();
            case "quantidade" -> produtoService.listarPorQuantidadeDecrescente();
            default -> produtoService.listarPorNomeCrescente();
        };

        model.addAttribute("produtos", produtos);
        model.addAttribute("filtroAtual", filtro);
        return "produtos/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("caixas", caixaService.listarTodos());
        return "produtos/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute ProdutoRequestDto dto,
                         @RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "imagemFile", required = false) MultipartFile imagemFile) throws IOException {
        if (id != null) {
            produtoService.atualizar(id, dto, imagemFile);
        } else {
            produtoService.criar(dto, imagemFile);
        }
        return "redirect:/produtos";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        ProdutoResponseDto response = produtoService.buscarPorId(id);
        model.addAttribute("produto", response);
        model.addAttribute("caixas", caixaService.listarTodos());
        return "produtos/formulario";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return "redirect:/produtos";
    }

}
