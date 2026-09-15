package br.com.financeiro.controlefinanceiro.controller;

import br.com.financeiro.controlefinanceiro.model.Receita;
import br.com.financeiro.controlefinanceiro.model.TipoReceita;
import br.com.financeiro.controlefinanceiro.service.ReceitaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/receitas")
public class ReceitaController {

    private final ReceitaService service;


    public ReceitaController(
            ReceitaService service
    ) {
        this.service = service;
    }


    @GetMapping
    public String listar(
            Model model
    ) {

        model.addAttribute(
                "receitas",
                service.listar()
        );


        model.addAttribute(
                "novaReceita",
                new Receita()
        );


        model.addAttribute(
                "tipos",
                TipoReceita.values()
        );


        model.addAttribute(
                "totalMes",
                service.totalReceitasMesAtual()
        );


        return "receitas";
    }


    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute Receita receita
    ) {

        service.salvar(receita);

        return "redirect:/receitas";
    }


    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id
    ) {

        service.excluir(id);

        return "redirect:/receitas";
    }
}