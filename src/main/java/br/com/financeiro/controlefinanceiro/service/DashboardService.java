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


    /*
     * Total de gastos do mês atual.
     *
     * O cálculo considera as parcelas que vencem
     * no mês atual.
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
     * Total de receitas do mês atual.
     */
    public double totalReceitasMesAtual() {

        return receitaService
                .totalReceitasMesAtual();
    }


    /*
     * Saldo apenas do mês atual.
     *
     * Exemplo:
     *
     * Receita: 3000
     * Gastos: 1800
     *
     * Saldo do mês: 1200
     */
    public double saldoMesAtual() {

        return totalReceitasMesAtual()
                - totalDoMesAtual();
    }


    /*
     * Saldo acumulado.
     *
     * Considera:
     *
     * todas as receitas até hoje
     * -
     * todas as parcelas com vencimento até hoje
     */
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
     * Gastos agrupados por categoria
     * no mês atual.
     */
    public Map<String, Double> gastosPorCategoriaNoMes() {

        YearMonth mesAtual =
                YearMonth.now();


        Map<String, Double> resultado =
                new LinkedHashMap<>();


        List<Parcela> parcelas =
                parcelaService.listar();


        for (Parcela parcela : parcelas) {

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
                    gastoService
                            .buscarPorId(
                                    parcela.getGastoId()
                            );


            if (gasto == null) {
                continue;
            }


            String categoria =
                    categoriaService
                            .nomePorId(
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
     * Retorna as próximas faturas dos cartões.
     *
     * São consideradas parcelas não pagas
     * com vencimento nos próximos 60 dias.
     */
    public List<FaturaResumo> proximasFaturas() {

        LocalDate hoje =
                LocalDate.now();


        LocalDate limite =
                hoje.plusDays(60);


        Map<String, FaturaResumo> faturas =
                new LinkedHashMap<>();


        for (Parcela parcela : parcelaService.listar()) {


            /*
             * Ignora parcela sem vencimento.
             */
            if (parcela.getVencimento() == null) {
                continue;
            }


            /*
             * Ignora parcela já paga.
             */
            if (parcela.isPaga()) {
                continue;
            }


            /*
             * Ignora vencimentos anteriores a hoje.
             */
            if (parcela
                    .getVencimento()
                    .isBefore(hoje)) {

                continue;
            }


            /*
             * Ignora vencimentos muito distantes.
             */
            if (parcela
                    .getVencimento()
                    .isAfter(limite)) {

                continue;
            }


            /*
             * Busca o gasto relacionado à parcela.
             */
            Gasto gasto =
                    gastoService
                            .buscarPorId(
                                    parcela.getGastoId()
                            );


            if (gasto == null) {
                continue;
            }


            /*
             * Se o gasto não possui cartão,
             * ele não pertence a uma fatura.
             */
            if (gasto.getCartaoId() == null) {
                continue;
            }


            /*
             * Busca o cartão.
             */
            CartaoCredito cartao =
                    cartaoService
                            .buscarPorId(
                                    gasto.getCartaoId()
                            );


            if (cartao == null) {
                continue;
            }


            LocalDate vencimento =
                    parcela.getVencimento();


            LocalDate fechamento =
                    calcularFechamento(
                            vencimento,
                            cartao
                    );


            long diasParaFechamento =
                    ChronoUnit.DAYS.between(
                            hoje,
                            fechamento
                    );


            long diasParaVencimento =
                    ChronoUnit.DAYS.between(
                            hoje,
                            vencimento
                    );


            /*
             * Uma fatura é identificada
             * pelo cartão + data de vencimento.
             *
             * Assim duas parcelas do mesmo cartão
             * e da mesma fatura são somadas.
             */
            String chave =
                    cartao.getId()
                            + "-"
                            + vencimento;


            if (!faturas.containsKey(chave)) {

                FaturaResumo resumo =
                        new FaturaResumo(

                                cartao.getId(),

                                cartao.getNome(),

                                parcela.getValor(),

                                fechamento,

                                vencimento,

                                diasParaFechamento,

                                diasParaVencimento

                        );


                faturas.put(
                        chave,
                        resumo
                );

            } else {

                FaturaResumo resumo =
                        faturas.get(chave);


                resumo.setValor(
                        resumo.getValor()
                                + parcela.getValor()
                );
            }
        }


        return faturas
                .values()
                .stream()

                .sorted(
                        Comparator
                                .comparing(
                                        FaturaResumo::getVencimento
                                )
                )

                .toList();
    }


    /*
     * Calcula a data de fechamento
     * correspondente a uma fatura.
     *
     * Exemplo:
     *
     * fechamento: dia 25
     * vencimento: dia 05
     *
     * vencimento: 05/10
     * fechamento: 25/09
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
         * Se o fechamento acontece depois
         * do dia de vencimento, então o
         * fechamento pertence ao mês anterior.
         */
        if (cartao.getDiaFechamento()
                > vencimento.getDayOfMonth()) {


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