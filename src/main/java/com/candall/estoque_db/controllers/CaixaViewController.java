package com.candall.estoque_db.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String salvar(@RequestParam("codigoCaixa") String codigo) {
        CaixaRequestDto dto = new CaixaRequestDto(codigo);
        caixaService.criar(dto);
        return "redirect:/caixas";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        caixaService.deletar(id);
        return "redirect:/caixas";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("caixa", caixaService.buscarPorId(id)); 
        return "caixas/editar";
    }

     @PostMapping("/editar/{id}")
    public String renomear(@PathVariable Long id, @RequestParam("codigoCaixa") String novoCodigo) {
        CaixaRequestDto dto = new CaixaRequestDto(novoCodigo);
        caixaService.atualizarCodigo(id, dto);
        return "redirect:/caixas";
    }
}
