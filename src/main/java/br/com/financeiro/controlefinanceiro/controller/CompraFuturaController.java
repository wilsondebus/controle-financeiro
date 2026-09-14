package br.com.financeiro.controlefinanceiro.controller;

import br.com.financeiro.controlefinanceiro.model.CompraFutura;
import br.com.financeiro.controlefinanceiro.service.CompraFuturaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/compras-futuras")
public class CompraFuturaController {

    private final CompraFuturaService service;

    public CompraFuturaController(
            CompraFuturaService service
    ) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "compras",
                service.listar()
        );

        model.addAttribute(
                "novaCompra",
                new CompraFutura()
        );

        return "compras-futuras";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute CompraFutura compra
    ) {

        service.salvar(compra);

        return "redirect:/compras-futuras";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id
    ) {

        service.excluir(id);

        return "redirect:/compras-futuras";
    }

    @GetMapping("/realizada/{id}")
    public String realizada(
            @PathVariable Long id
    ) {

        service.alternarRealizada(id);

        return "redirect:/compras-futuras";
    }
}