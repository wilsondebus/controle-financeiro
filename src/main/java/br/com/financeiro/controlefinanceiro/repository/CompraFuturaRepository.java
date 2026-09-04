package br.com.financeiro.controlefinanceiro.repository;

import br.com.financeiro.controlefinanceiro.model.CompraFutura;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CompraFuturaRepository {

    private static final String ARQUIVO =
            "data/compras-futuras.json";

    private final JsonRepositoryUtil json;

    public CompraFuturaRepository(
            JsonRepositoryUtil json
    ) {
        this.json = json;
    }

    public List<CompraFutura> listar() {

        return json.lerLista(
                ARQUIVO,
                CompraFutura.class
        );
    }

    public Optional<CompraFutura> buscarPorId(
            Long id
    ) {

        return listar()
                .stream()
                .filter(
                        compra ->
                                compra
                                        .getId()
                                        .equals(id)
                )
                .findFirst();
    }

    public CompraFutura salvar(
            CompraFutura compra
    ) {

        List<CompraFutura> compras = listar();

        if (compra.getId() == null) {

            compra.setId(
                    json.proximoId(compras)
            );

            compras.add(compra);

        } else {

            compras.removeIf(
                    c -> c.getId()
                            .equals(compra.getId())
            );

            compras.add(compra);
        }

        json.salvarLista(
                ARQUIVO,
                compras
        );

        return compra;
    }

    public void excluir(Long id) {

        List<CompraFutura> compras = listar();

        compras.removeIf(
                compra ->
                        compra
                                .getId()
                                .equals(id)
        );

        json.salvarLista(
                ARQUIVO,
                compras
        );
    }
}
