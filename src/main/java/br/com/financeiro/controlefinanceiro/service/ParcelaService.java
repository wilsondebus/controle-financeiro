package br.com.financeiro.controlefinanceiro.service;

import br.com.financeiro.controlefinanceiro.model.CartaoCredito;
import br.com.financeiro.controlefinanceiro.model.Gasto;
import br.com.financeiro.controlefinanceiro.model.Parcela;
import br.com.financeiro.controlefinanceiro.repository.ParcelaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParcelaService {

    private final ParcelaRepository repository;

    private final CartaoService cartaoService;

    public ParcelaService(
            ParcelaRepository repository,
            CartaoService cartaoService
    ) {

        this.repository = repository;
        this.cartaoService = cartaoService;
    }

    public void gerarParcelas(Gasto gasto) {

        repository.excluirPorGasto(
                gasto.getId()
        );

        int quantidade;

        if (gasto.isParcelado()) {

            quantidade =
                    Math.max(
                            gasto.getQuantidadeParcelas(),
                            1
                    );

        } else {

            quantidade = 1;
        }

        double total = gasto.getValor();

        if (
                gasto.isPossuiJuros()
                        && gasto.getValorTotalComJuros() > 0
        ) {

            total =
                    gasto.getValorTotalComJuros();
        }

        BigDecimal totalDecimal =
                BigDecimal.valueOf(total);

        BigDecimal valorBase =
                totalDecimal.divide(
                        BigDecimal.valueOf(quantidade),
                        2,
                        RoundingMode.DOWN
                );

        BigDecimal acumulado =
                BigDecimal.ZERO;

        List<Parcela> parcelas =
                new ArrayList<>();

        LocalDate primeiroVencimento =
                calcularPrimeiroVencimento(gasto);

        for (
                int numero = 1;
                numero <= quantidade;
                numero++
        ) {

            BigDecimal valorParcela;

            if (numero == quantidade) {

                valorParcela =
                        totalDecimal.subtract(
                                acumulado
                        );

            } else {

                valorParcela = valorBase;

                acumulado =
                        acumulado.add(
                                valorParcela
                        );
            }

            Parcela parcela =
                    new Parcela();

            parcela.setGastoId(
                    gasto.getId()
            );

            parcela.setNumeroParcela(
                    numero
            );

            parcela.setTotalParcelas(
                    quantidade
            );

            parcela.setValor(
                    valorParcela.doubleValue()
            );

            parcela.setVencimento(
                    primeiroVencimento.plusMonths(
                            numero - 1L
                    )
            );

            parcela.setPaga(false);

            parcelas.add(parcela);
        }

        repository.salvarTodas(parcelas);
    }

    private LocalDate calcularPrimeiroVencimento(
            Gasto gasto
    ) {

        CartaoCredito cartao =
                cartaoService.buscarPorId(
                        gasto.getCartaoId()
                );

        /*
         * Se não houver cartão,
         * considera a data do próprio gasto.
         *
         * Exemplo:
         * PIX, dinheiro ou débito.
         */
        if (cartao == null) {
            return gasto.getData();
        }

        LocalDate dataCompra =
                gasto.getData();

        YearMonth mesFatura;

        /*
         * Exemplo:
         *
         * fechamento = dia 5
         *
         * compra dia 03:
         * entra na fatura atual.
         *
         * compra dia 10:
         * entra na próxima fatura.
         */
        if (
                dataCompra.getDayOfMonth()
                        <= cartao.getDiaFechamento()
        ) {

            mesFatura =
                    YearMonth.from(dataCompra);

        } else {

            mesFatura =
                    YearMonth
                            .from(dataCompra)
                            .plusMonths(1);
        }

        /*
         * Alguns meses possuem menos de 31 dias.
         *
         * Se vencimento for dia 31 e fevereiro
         * tiver 28 dias, usamos dia 28.
         */
        int diaVencimento =
                Math.min(
                        cartao.getDiaVencimento(),
                        mesFatura.lengthOfMonth()
                );

        LocalDate vencimento =
                mesFatura.atDay(
                        diaVencimento
                );

        /*
         * Proteção para configurações incomuns.
         *
         * Exemplo:
         * fechamento dia 25
         * vencimento dia 5
         *
         * A fatura não pode vencer antes da compra.
         */
        if (
                vencimento.isBefore(
                        dataCompra
                )
        ) {

            YearMonth proximoMes =
                    mesFatura.plusMonths(1);

            diaVencimento =
                    Math.min(
                            cartao.getDiaVencimento(),
                            proximoMes.lengthOfMonth()
                    );

            vencimento =
                    proximoMes.atDay(
                            diaVencimento
                    );
        }

        return vencimento;
    }

    public List<Parcela> listar() {
        return repository.listar();
    }

    public List<Parcela> listarPorGasto(
            Long gastoId
    ) {

        return listar()
                .stream()
                .filter(
                        parcela ->
                                parcela
                                        .getGastoId()
                                        .equals(gastoId)
                )
                .sorted(
                        (p1, p2) ->
                                p1
                                        .getVencimento()
                                        .compareTo(
                                                p2.getVencimento()
                                        )
                )
                .toList();
    }

    public void excluirPorGasto(
            Long gastoId
    ) {

        repository.excluirPorGasto(
                gastoId
        );
    }

    public void alternarPaga(
            Long parcelaId
    ) {

        Parcela parcela =
                listar()
                        .stream()
                        .filter(
                                p ->
                                        p
                                                .getId()
                                                .equals(parcelaId)
                        )
                        .findFirst()
                        .orElse(null);

        if (parcela != null) {

            parcela.setPaga(
                    !parcela.isPaga()
            );

            repository.atualizar(
                    parcela
            );
        }
    }

    public double totalParcelasAteHoje() {

        LocalDate hoje =
                LocalDate.now();


        return listar()
                .stream()

                .filter(
                        parcela ->
                                parcela.getVencimento()
                                        != null
                )

                .filter(
                        parcela ->
                                !parcela
                                        .getVencimento()
                                        .isAfter(hoje)
                )

                .mapToDouble(
                        Parcela::getValor
                )

                .sum();
    }

}