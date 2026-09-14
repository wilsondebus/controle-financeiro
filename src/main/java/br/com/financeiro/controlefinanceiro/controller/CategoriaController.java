package br.com.financeiro.controlefinanceiro.controller;

import br.com.financeiro.controlefinanceiro.model.Categoria;
import br.com.financeiro.controlefinanceiro.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(
            CategoriaService service
    ) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "categorias",
                service.listar()
        );

        model.addAttribute(
                "novaCategoria",
                new Categoria()
        );

        return "categorias";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute Categoria categoria
    ) {

        service.salvar(categoria);

        return "redirect:/categorias";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id
    ) {

        service.excluir(id);

        return "redirect:/categorias";
    }
}