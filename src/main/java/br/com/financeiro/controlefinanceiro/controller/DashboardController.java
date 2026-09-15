package br.com.financeiro.controlefinanceiro.controller;

import br.com.financeiro.controlefinanceiro.service.DashboardService;
import br.com.financeiro.controlefinanceiro.service.GastoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final GastoService gastoService;

    public DashboardController(
            DashboardService dashboardService,
            GastoService gastoService
    ) {
        this.dashboardService = dashboardService;
        this.gastoService = gastoService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {

        model.addAttribute(
                "totalMes",
                dashboardService.totalDoMesAtual()
        );


        model.addAttribute(
                "receitasMes",
                dashboardService.totalReceitasMesAtual()
        );


        model.addAttribute(
                "saldoMes",
                dashboardService.saldoMesAtual()
        );


        model.addAttribute(
                "saldoAcumulado",
                dashboardService.saldoAcumulado()
        );


        model.addAttribute(
                "gastosCategoria",
                dashboardService.gastosPorCategoriaNoMes()
        );


        model.addAttribute(
                "faturas",
                dashboardService.proximasFaturas()
        );


        model.addAttribute(
                "ultimosGastos",
                gastoService
                        .listar()
                        .stream()
                        .limit(5)
                        .toList()
        );


        return "dashboard";
    }
}