package br.com.financeiro.controlefinanceiro.controller;

import br.com.financeiro.controlefinanceiro.model.CartaoCredito;
import br.com.financeiro.controlefinanceiro.model.FormaPagamento;
import br.com.financeiro.controlefinanceiro.model.TipoPagamento;
import br.com.financeiro.controlefinanceiro.service.CartaoService;
import br.com.financeiro.controlefinanceiro.service.FormaPagamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pagamentos")
public class FormaPagamentoController {

    private final FormaPagamentoService formaPagamentoService;
    private final CartaoService cartaoService;


    public FormaPagamentoController(
            FormaPagamentoService formaPagamentoService,
            CartaoService cartaoService
    ) {

        this.formaPagamentoService = formaPagamentoService;
        this.cartaoService = cartaoService;
    }


    /*
     * Página principal de pagamentos.
     *
     * Aqui carregamos:
     *
     * - formas de pagamento
     * - cartões de crédito
     * - formulário para nova forma
     * - formulário para novo cartão
     */
    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "formas",
                formaPagamentoService.listar()
        );


        model.addAttribute(
                "novaForma",
                new FormaPagamento()
        );


        model.addAttribute(
                "tipos",
                TipoPagamento.values()
        );


        model.addAttribute(
                "cartoes",
                cartaoService.listar()
        );


        model.addAttribute(
                "novoCartao",
                new CartaoCredito()
        );


        return "pagamentos";
    }


    /*
     * Salva uma forma de pagamento.
     *
     * Exemplos:
     *
     * PIX
     * Dinheiro
     * Débito
     */
    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute FormaPagamento formaPagamento
    ) {

        formaPagamentoService.salvar(
                formaPagamento
        );

        return "redirect:/pagamentos";
    }


    /*
     * Exclui uma forma de pagamento.
     */
    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id
    ) {

        formaPagamentoService.excluir(id);

        return "redirect:/pagamentos";
    }


    /*
     * Salva um cartão de crédito
     * diretamente pela página de pagamentos.
     */
    @PostMapping("/cartao/salvar")
    public String salvarCartao(
            @ModelAttribute CartaoCredito cartao
    ) {

        cartaoService.salvar(cartao);

        return "redirect:/pagamentos";
    }


    /*
     * Exclui um cartão pela página
     * de pagamentos.
     */
    @GetMapping("/cartao/excluir/{id}")
    public String excluirCartao(
            @PathVariable Long id
    ) {

        cartaoService.excluir(id);

        return "redirect:/pagamentos";
    }
}