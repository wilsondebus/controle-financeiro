package br.com.financeiro.controlefinanceiro.controller;

import br.com.financeiro.controlefinanceiro.model.CartaoCredito;
import br.com.financeiro.controlefinanceiro.service.CartaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cartoes")
public class CartaoController {

    private final CartaoService service;

    public CartaoController(
            CartaoService service
    ) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "cartoes",
                service.listar()
        );

        model.addAttribute(
                "novoCartao",
                new CartaoCredito()
        );

        return "cartoes";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute CartaoCredito cartao
    ) {

        service.salvar(cartao);

        return "redirect:/cartoes";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id
    ) {

        service.excluir(id);

        return "redirect:/cartoes";
    }
}