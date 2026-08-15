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
    public String listar(Model model,
                         @RequestParam(value = "filtro", defaultValue = "nome") String filtro,
                         @RequestParam(value = "caixaId", required = false) Long caixaId) {

        List<ProdutoResponseDto> produtos;
        if (caixaId != null) {
            produtos = produtoService.listarPorCaixa(caixaId);

            produtos = switch (filtro) {
                case "validade" -> produtoService.ordenarPorValidade(produtos);
                case "quantidade" -> produtoService.ordenarPorQuantidade(produtos);
                default -> produtoService.ordenarPorNome(produtos);
            };
            var caixaAtual = caixaService.buscarPorId(caixaId);
            model.addAttribute("nomeCaixaAtual", caixaAtual.codigoCaixa());
        } else {
            produtos = switch (filtro) {
                case "validade" -> produtoService.listarPorValidadeCrescente();
                case "quantidade" -> produtoService.listarPorQuantidadeDecrescente();
                default -> produtoService.listarPorNomeCrescente();
            };
        }

        model.addAttribute("produtos", produtos);
        model.addAttribute("filtroAtual", filtro);
        model.addAttribute("caixaIdAtual", caixaId);
        model.addAttribute("todasCaixas", caixaService.listarTodos());

        return "produtos/lista";
    }

    @GetMapping("/novo")
    public String novoForm(@RequestParam(value = "caixaIdAtual", required = false) Long caixaIdAtual, Model model) {
        model.addAttribute("caixas", caixaService.listarTodos());
        model.addAttribute("caixaIdAtual", caixaIdAtual);
        if (caixaIdAtual != null) {
            var caixaOrigem = caixaService.buscarPorId(caixaIdAtual);
            model.addAttribute("nomeCaixaOrigem", caixaOrigem.codigoCaixa());
        }
        return "produtos/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute ProdutoRequestDto dto,
                         @RequestParam(value = "id", required = false) Long id,
                         @RequestParam(value = "imagemFile", required = false) MultipartFile imagemFile,
                         @RequestParam(value = "caixaIdAtual", required = false) Long caixaIdAtual) throws IOException {

        if (id != null) {
            produtoService.atualizar(id, dto, imagemFile);
        } else {
            produtoService.criar(dto, imagemFile);
        }

        if (caixaIdAtual != null) {
            return "redirect:/produtos?caixaId=" + caixaIdAtual;
        }
        return "redirect:/produtos";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id,
                             @RequestParam(value = "caixaIdAtual", required = false) Long caixaIdAtual,
                             Model model) {
        ProdutoResponseDto response = produtoService.buscarPorId(id);
        model.addAttribute("produto", response);
        model.addAttribute("caixas", caixaService.listarTodos());
        model.addAttribute("caixaIdAtual", caixaIdAtual);
        return "produtos/formulario";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id,
                          @RequestParam(value = "caixaIdAtual", required = false) Long caixaIdAtual) {

        produtoService.deletar(id);

        if (caixaIdAtual != null) {
            return "redirect:/produtos?caixaId=" + caixaIdAtual;
        }
        return "redirect:/produtos";
    }
}
