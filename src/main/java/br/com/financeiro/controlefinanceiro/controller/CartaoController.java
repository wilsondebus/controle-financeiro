package br.com.financeiro.controlefinanceiro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cartoes")
public class CartaoController {

    @GetMapping
    public String redirecionarParaPagamentos() {

        return "redirect:/pagamentos";
    }
}