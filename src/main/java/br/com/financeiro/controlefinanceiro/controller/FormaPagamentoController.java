package br.com.financeiro.controlefinanceiro.controller;

import br.com.financeiro.controlefinanceiro.model.FormaPagamento;
import br.com.financeiro.controlefinanceiro.model.TipoPagamento;
import br.com.financeiro.controlefinanceiro.service.FormaPagamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pagamentos")
public class FormaPagamentoController {

    private final FormaPagamentoService service;

    public FormaPagamentoController(
            FormaPagamentoService service
    ) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "formas",
                service.listar()
        );

        model.addAttribute(
                "novaForma",
                new FormaPagamento()
        );

        model.addAttribute(
                "tipos",
                TipoPagamento.values()
        );

        return "pagamentos";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute FormaPagamento forma
    ) {

        service.salvar(forma);

        return "redirect:/pagamentos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id
    ) {

        service.excluir(id);

        return "redirect:/pagamentos";
    }
}