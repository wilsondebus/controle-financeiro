package br.com.financeiro.controlefinanceiro.service;

import br.com.financeiro.controlefinanceiro.model.Gasto;
import br.com.financeiro.controlefinanceiro.repository.GastoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class GastoService {

    private final GastoRepository repository;

    private final ParcelaService parcelaService;

    public GastoService(
            GastoRepository repository,
            ParcelaService parcelaService
    ) {

        this.repository = repository;
        this.parcelaService = parcelaService;
    }

    public List<Gasto> listar() {

        return repository
                .listar()
                .stream()
                .sorted(
                        Comparator
                                .comparing(
                                        Gasto::getData
                                )
                                .reversed()
                )
                .toList();
    }

    public Gasto buscarPorId(Long id) {

        return repository
                .buscarPorId(id)
                .orElse(null);
    }

    public void salvar(Gasto gasto) {

        if (gasto.getCartaoId() != null) {

            gasto.setFormaPagamentoId(null);
        }
        /*
         * Se por algum motivo não vier data,
         * usamos a data atual.
         */
        if (gasto.getData() == null) {

            gasto.setData(
                    LocalDate.now()
            );
        }

        /*
         * Compra não parcelada sempre terá
         * uma única parcela.
         */
        if (!gasto.isParcelado()) {

            gasto.setQuantidadeParcelas(1);
        }

        /*
         * Se não tiver juros,
         * não precisamos manter
         * valor total com juros.
         */
        if (!gasto.isPossuiJuros()) {

            gasto.setValorTotalComJuros(0);
        }

        /*
         * Evita valores como:
         *
         * 0 parcelas
         * -2 parcelas
         */
        if (
                gasto.getQuantidadeParcelas() < 1
        ) {

            gasto.setQuantidadeParcelas(1);
        }

        repository.salvar(gasto);

        /*
         * Depois que o gasto ganhou ID,
         * criamos suas parcelas.
         */
        parcelaService.gerarParcelas(
                gasto
        );
    }

    public void excluir(Long id) {

        /*
         * Primeiro excluímos as parcelas
         * relacionadas.
         */
        parcelaService.excluirPorGasto(
                id
        );

        /*
         * Depois excluímos o gasto.
         */
        repository.excluir(id);
    }
}