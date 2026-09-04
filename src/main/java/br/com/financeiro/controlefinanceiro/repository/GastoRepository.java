package br.com.financeiro.controlefinanceiro.repository;

import br.com.financeiro.controlefinanceiro.model.Gasto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GastoRepository {

    private static final String ARQUIVO =
            "data/gastos.json";

    private final JsonRepositoryUtil json;

    public GastoRepository(
            JsonRepositoryUtil json
    ) {
        this.json = json;
    }

    public List<Gasto> listar() {

        return json.lerLista(
                ARQUIVO,
                Gasto.class
        );
    }

    public Optional<Gasto> buscarPorId(
            Long id
    ) {

        return listar()
                .stream()
                .filter(
                        gasto ->
                                gasto
                                        .getId()
                                        .equals(id)
                )
                .findFirst();
    }

    public Gasto salvar(
            Gasto gasto
    ) {

        List<Gasto> gastos = listar();

        if (gasto.getId() == null) {

            gasto.setId(
                    json.proximoId(gastos)
            );

            gastos.add(gasto);

        } else {

            gastos.removeIf(
                    g -> g.getId()
                            .equals(gasto.getId())
            );

            gastos.add(gasto);
        }

        json.salvarLista(
                ARQUIVO,
                gastos
        );

        return gasto;
    }

    public void excluir(Long id) {

        List<Gasto> gastos = listar();

        gastos.removeIf(
                gasto ->
                        gasto
                                .getId()
                                .equals(id)
        );

        json.salvarLista(
                ARQUIVO,
                gastos
        );
    }
}
