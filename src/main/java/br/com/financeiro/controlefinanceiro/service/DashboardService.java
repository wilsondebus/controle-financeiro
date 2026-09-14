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

    public DashboardService(
            ParcelaService parcelaService,
            GastoService gastoService,
            CartaoService cartaoService,
            CategoriaService categoriaService
    ) {

        this.parcelaService =
                parcelaService;

        this.gastoService =
                gastoService;

        this.cartaoService =
                cartaoService;

        this.categoriaService =
                categoriaService;
    }

    public double totalDoMesAtual() {

        YearMonth mesAtual =
                YearMonth.now();

        return parcelaService
                .listar()
                .stream()
                .filter(
                        parcela ->
                                parcela.getVencimento()
                                        != null
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

            if (
                    parcela.getVencimento()
                            == null
            ) {
                continue;
            }

            YearMonth mesParcela =
                    YearMonth.from(
                            parcela.getVencimento()
                    );

            if (
                    !mesParcela.equals(
                            mesAtual
                    )
            ) {
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

    public List<FaturaResumo>
    proximasFaturas() {

        LocalDate hoje =
                LocalDate.now();

        LocalDate limite =
                hoje.plusDays(40);

        Map<String, FaturaResumo> resumo =
                new LinkedHashMap<>();

        for (
                Parcela parcela :
                parcelaService.listar()
        ) {

            if (
                    parcela.getVencimento()
                            == null
            ) {
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

            /*
             * Queremos juntar parcelas:
             *
             * mesmo cartão
             * +
             * mesmo vencimento
             *
             * em uma única fatura.
             */
            String chave =
                    cartao.getId()
                            + "-"
                            + parcela.getVencimento();

            FaturaResumo existente =
                    resumo.get(chave);

            if (existente == null) {

                long dias =
                        ChronoUnit.DAYS
                                .between(
                                        hoje,
                                        parcela.getVencimento()
                                );

                FaturaResumo novaFatura =
                        new FaturaResumo(
                                cartao.getId(),
                                cartao.getNome(),
                                parcela.getValor(),
                                parcela.getVencimento(),
                                dias
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
}