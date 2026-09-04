package br.com.financeiro.controlefinanceiro.repository;

import br.com.financeiro.controlefinanceiro.model.Parcela;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParcelaRepository {

    private static final String ARQUIVO =
            "data/parcelas.json";

    private final JsonRepositoryUtil json;

    public ParcelaRepository(
            JsonRepositoryUtil json
    ) {
        this.json = json;
    }

    public List<Parcela> listar() {

        return json.lerLista(
                ARQUIVO,
                Parcela.class
        );
    }

    public void salvarTodas(
            List<Parcela> novasParcelas
    ) {

        List<Parcela> parcelas = listar();

        for (Parcela parcela : novasParcelas) {

            parcela.setId(
                    json.proximoId(parcelas)
            );

            parcelas.add(parcela);
        }

        json.salvarLista(
                ARQUIVO,
                parcelas
        );
    }

    public void excluirPorGasto(
            Long gastoId
    ) {

        List<Parcela> parcelas = listar();

        parcelas.removeIf(
                parcela ->
                        parcela
                                .getGastoId()
                                .equals(gastoId)
        );

        json.salvarLista(
                ARQUIVO,
                parcelas
        );
    }

    public void atualizar(
            Parcela parcelaAtualizada
    ) {

        List<Parcela> parcelas = listar();

        parcelas.removeIf(
                parcela ->
                        parcela
                                .getId()
                                .equals(
                                        parcelaAtualizada.getId()
                                )
        );

        parcelas.add(parcelaAtualizada);

        json.salvarLista(
                ARQUIVO,
                parcelas
        );
    }
}
