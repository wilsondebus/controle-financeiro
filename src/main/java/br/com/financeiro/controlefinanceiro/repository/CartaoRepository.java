package br.com.financeiro.controlefinanceiro.repository;

import br.com.financeiro.controlefinanceiro.model.CartaoCredito;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CartaoRepository {

    private static final String ARQUIVO =
            "data/cartoes.json";

    private final JsonRepositoryUtil json;

    public CartaoRepository(
            JsonRepositoryUtil json
    ) {
        this.json = json;
    }

    public List<CartaoCredito> listar() {

        return json.lerLista(
                ARQUIVO,
                CartaoCredito.class
        );
    }

    public Optional<CartaoCredito> buscarPorId(
            Long id
    ) {

        if (id == null) {
            return Optional.empty();
        }

        return listar()
                .stream()
                .filter(
                        cartao ->
                                cartao
                                        .getId()
                                        .equals(id)
                )
                .findFirst();
    }

    public CartaoCredito salvar(
            CartaoCredito cartao
    ) {

        List<CartaoCredito> cartoes = listar();

        if (cartao.getId() == null) {

            cartao.setId(
                    json.proximoId(cartoes)
            );

            cartoes.add(cartao);

        } else {

            cartoes.removeIf(
                    c -> c.getId()
                            .equals(cartao.getId())
            );

            cartoes.add(cartao);
        }

        json.salvarLista(
                ARQUIVO,
                cartoes
        );

        return cartao;
    }

    public void excluir(Long id) {

        List<CartaoCredito> cartoes = listar();

        cartoes.removeIf(
                cartao ->
                        cartao
                                .getId()
                                .equals(id)
        );

        json.salvarLista(
                ARQUIVO,
                cartoes
        );
    }
}