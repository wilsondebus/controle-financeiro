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

        this.dashboardService =
                dashboardService;

        this.gastoService =
                gastoService;
    }


    @GetMapping("/")
    public String dashboard(
            Model model
    ) {

        /*
         * Gastos do mês atual
         */
        model.addAttribute(
                "totalMes",
                dashboardService.totalDoMesAtual()
        );


        /*
         * Receitas do mês atual
         */
        model.addAttribute(
                "receitasMes",
                dashboardService.totalReceitasMesAtual()
        );


        /*
         * Saldo somente deste mês
         */
        model.addAttribute(
                "saldoMes",
                dashboardService.saldoMesAtual()
        );


        /*
         * Saldo acumulado desde o início
         * dos registros
         */
        model.addAttribute(
                "saldoAcumulado",
                dashboardService.saldoAcumulado()
        );


        /*
         * Gastos agrupados por categoria
         */
        model.addAttribute(
                "gastosCategoria",
                dashboardService.gastosPorCategoriaNoMes()
        );


        /*
         * Próximas faturas dos cartões
         */
        model.addAttribute(
                "faturas",
                dashboardService.proximasFaturas()
        );


        /*
         * Últimos 5 gastos cadastrados
         */
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