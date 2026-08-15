package com.candall.estoque_db.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.candall.estoque_db.exceptions.ResourceAlreadyExistsException;
import com.candall.estoque_db.models.dto.CaixaRequestDto;
import com.candall.estoque_db.services.CaixaService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/caixas")
@RequiredArgsConstructor
public class CaixaViewController {

    private final CaixaService caixaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("caixas", caixaService.listarTodos());
        return "caixas/lista";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam("codigoCaixa") String codigo, RedirectAttributes redirectAttributes) {
        try {
            CaixaRequestDto dto = new CaixaRequestDto(codigo);
            caixaService.criar(dto);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Caixa cadastrada com sucesso!");
        } catch (ResourceAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/caixas";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            caixaService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Caixa removida com sucesso!");
        } catch (ResourceAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/caixas";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("caixa", caixaService.buscarPorId(id));
        return "caixas/editar";
    }

    @PostMapping("/editar/{id}")
    public String renomear(@PathVariable Long id, @RequestParam("codigoCaixa") String novoCodigo, RedirectAttributes redirectAttributes) {
        try {
            CaixaRequestDto dto = new CaixaRequestDto(novoCodigo);
            caixaService.atualizarCodigo(id, dto);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Caixa renomeada com sucesso!");
        } catch (ResourceAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/caixas";
    }
}
