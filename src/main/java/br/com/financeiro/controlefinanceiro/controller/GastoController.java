package br.com.financeiro.controlefinanceiro.controller;

import br.com.financeiro.controlefinanceiro.model.Gasto;
import br.com.financeiro.controlefinanceiro.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gastos")
public class GastoController {

    private final GastoService gastoService;
    private final CategoriaService categoriaService;
    private final FormaPagamentoService formaPagamentoService;
    private final CartaoService cartaoService;
    private final ParcelaService parcelaService;

    public GastoController(
            GastoService gastoService,
            CategoriaService categoriaService,
            FormaPagamentoService formaPagamentoService,
            CartaoService cartaoService,
            ParcelaService parcelaService
    ) {

        this.gastoService = gastoService;
        this.categoriaService = categoriaService;
        this.formaPagamentoService =
                formaPagamentoService;
        this.cartaoService = cartaoService;
        this.parcelaService = parcelaService;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "gastos",
                gastoService.listar()
        );

        model.addAttribute(
                "categoriaService",
                categoriaService
        );

        model.addAttribute(
                "formaPagamentoService",
                formaPagamentoService
        );

        model.addAttribute(
                "cartaoService",
                cartaoService
        );

        return "gastos";
    }

    @GetMapping("/novo")
    public String novo(Model model) {

        Gasto gasto = new Gasto();

        gasto.setQuantidadeParcelas(1);

        prepararFormulario(
                model,
                gasto
        );

        return "novo-gasto";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model
    ) {

        Gasto gasto =
                gastoService.buscarPorId(id);

        if (gasto == null) {
            return "redirect:/gastos";
        }

        prepararFormulario(
                model,
                gasto
        );

        return "novo-gasto";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute Gasto gasto
    ) {

        gastoService.salvar(gasto);

        return "redirect:/gastos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id
    ) {

        gastoService.excluir(id);

        return "redirect:/gastos";
    }

    @GetMapping("/{id}/parcelas")
    public String parcelas(
            @PathVariable Long id,
            Model model
    ) {

        Gasto gasto =
                gastoService.buscarPorId(id);

        if (gasto == null) {
            return "redirect:/gastos";
        }

        model.addAttribute(
                "gasto",
                gasto
        );

        model.addAttribute(
                "parcelas",
                parcelaService.listarPorGasto(id)
        );

        return "parcelas";
    }

    @GetMapping("/parcela/{id}/alternar")
    public String alternarParcela(
            @PathVariable Long id,
            @RequestParam Long gastoId
    ) {

        parcelaService.alternarPaga(id);

        return "redirect:/gastos/"
                + gastoId
                + "/parcelas";
    }

    private void prepararFormulario(
            Model model,
            Gasto gasto
    ) {

        model.addAttribute(
                "gasto",
                gasto
        );

        model.addAttribute(
                "categorias",
                categoriaService.listar()
        );

        model.addAttribute(
                "formasPagamento",
                formaPagamentoService.listar()
        );

        model.addAttribute(
                "cartoes",
                cartaoService.listar()
        );
    }
}