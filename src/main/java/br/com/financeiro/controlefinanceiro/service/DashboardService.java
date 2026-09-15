package br.com.financeiro.controlefinanceiro.service;

import br.com.financeiro.controlefinanceiro.model.CartaoCredito;
import br.com.financeiro.controlefinanceiro.model.FaturaResumo;
import br.com.financeiro.controlefinanceiro.model.Gasto;
import br.com.financeiro.controlefinanceiro.model.Parcela;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final ParcelaService parcelaService;

    private final GastoService gastoService;

    private final CartaoService cartaoService;

    private final CategoriaService categoriaService;

    private final ReceitaService receitaService;


    public DashboardService(
            ParcelaService parcelaService,
            GastoService gastoService,
            CartaoService cartaoService,
            CategoriaService categoriaService,
            ReceitaService receitaService
    ) {

        this.parcelaService = parcelaService;
        this.gastoService = gastoService;
        this.cartaoService = cartaoService;
        this.categoriaService = categoriaService;
        this.receitaService = receitaService;
    }

    public double totalReceitasMesAtual() {

        return receitaService
                .totalReceitasMesAtual();
    }

    public double saldoMesAtual() {

        return totalReceitasMesAtual()
                - totalDoMesAtual();
    }

    public double saldoAcumulado() {

        double receitas =
                receitaService
                        .totalReceitasAteHoje();


        double gastos =
                parcelaService
                        .totalParcelasAteHoje();


        return receitas - gastos;
    }
    /*
     * TOTAL DO MÊS
     */

    public double totalDoMesAtual() {

        YearMonth mesAtual =
                YearMonth.now();


        return parcelaService
                .listar()
                .stream()

                .filter(
                        parcela ->
                                parcela.getVencimento() != null
                )

                .filter(
                        parcela ->
                                YearMonth
                                        .from(
                                                parcela.getVencimento()
                                        )
                                        .equals(mesAtual)
                )

                .mapToDouble(
                        Parcela::getValor
                )

                .sum();
    }


    /*
     * GASTOS POR CATEGORIA
     */

    public Map<String, Double>
    gastosPorCategoriaNoMes() {

        YearMonth mesAtual =
                YearMonth.now();


        Map<String, Double> resultado =
                new LinkedHashMap<>();


        for (
                Parcela parcela :
                parcelaService.listar()
        ) {

            if (parcela.getVencimento() == null) {
                continue;
            }


            YearMonth mesParcela =
                    YearMonth.from(
                            parcela.getVencimento()
                    );


            if (!mesParcela.equals(mesAtual)) {
                continue;
            }


            Gasto gasto =
                    gastoService.buscarPorId(
                            parcela.getGastoId()
                    );


            if (gasto == null) {
                continue;
            }


            String categoria =
                    categoriaService.nomePorId(
                            gasto.getCategoriaId()
                    );


            resultado.merge(
                    categoria,
                    parcela.getValor(),
                    Double::sum
            );
        }


        return resultado;
    }


    /*
     * FATURAS DOS CARTÕES
     */

    public List<FaturaResumo>
    proximasFaturas() {

        LocalDate hoje =
                LocalDate.now();


        /*
         * Mostra faturas dos próximos
         * 60 dias.
         */

        LocalDate limite =
                hoje.plusDays(60);


        Map<String, FaturaResumo> resumo =
                new LinkedHashMap<>();


        for (
                Parcela parcela :
                parcelaService.listar()
        ) {

            if (parcela.getVencimento() == null) {
                continue;
            }


            if (parcela.isPaga()) {
                continue;
            }


            if (
                    parcela
                            .getVencimento()
                            .isBefore(hoje)
            ) {
                continue;
            }


            if (
                    parcela
                            .getVencimento()
                            .isAfter(limite)
            ) {
                continue;
            }


            Gasto gasto =
                    gastoService.buscarPorId(
                            parcela.getGastoId()
                    );


            if (
                    gasto == null
                            || gasto.getCartaoId() == null
            ) {
                continue;
            }


            CartaoCredito cartao =
                    cartaoService.buscarPorId(
                            gasto.getCartaoId()
                    );


            if (cartao == null) {
                continue;
            }


            LocalDate vencimento =
                    parcela.getVencimento();


            /*
             * Descobre quando essa fatura fecha.
             */

            LocalDate fechamento =
                    calcularFechamento(
                            vencimento,
                            cartao
                    );


            String chave =
                    cartao.getId()
                            + "-"
                            + vencimento;


            FaturaResumo existente =
                    resumo.get(chave);


            if (existente == null) {

                long diasVencimento =
                        ChronoUnit.DAYS
                                .between(
                                        hoje,
                                        vencimento
                                );


                long diasFechamento =
                        ChronoUnit.DAYS
                                .between(
                                        hoje,
                                        fechamento
                                );


                FaturaResumo novaFatura =
                        new FaturaResumo(

                                cartao.getId(),

                                cartao.getNome(),

                                parcela.getValor(),

                                fechamento,

                                vencimento,

                                diasFechamento,

                                diasVencimento

                        );


                resumo.put(
                        chave,
                        novaFatura
                );

            } else {

                existente.setValor(

                        existente.getValor()
                                + parcela.getValor()

                );
            }
        }


        return resumo
                .values()
                .stream()

                .sorted(
                        Comparator.comparing(
                                FaturaResumo::getVencimento
                        )
                )

                .toList();
    }


    /*
     * CALCULA O FECHAMENTO DE UMA FATURA
     */

    private LocalDate calcularFechamento(
            LocalDate vencimento,
            CartaoCredito cartao
    ) {

        YearMonth mes =
                YearMonth.from(
                        vencimento
                );


        int diaFechamento =
                Math.min(
                        cartao.getDiaFechamento(),
                        mes.lengthOfMonth()
                );


        LocalDate fechamento =
                mes.atDay(
                        diaFechamento
                );


        /*
         * Exemplo:
         *
         * vence dia 10
         * fecha dia 25
         *
         * então o fechamento pertence
         * ao mês anterior.
         */

        if (
                cartao.getDiaFechamento()
                        > cartao.getDiaVencimento()
        ) {

            YearMonth mesAnterior =
                    mes.minusMonths(1);


            diaFechamento =
                    Math.min(
                            cartao.getDiaFechamento(),
                            mesAnterior.lengthOfMonth()
                    );


            fechamento =
                    mesAnterior.atDay(
                            diaFechamento
                    );
        }


        return fechamento;
    }
}