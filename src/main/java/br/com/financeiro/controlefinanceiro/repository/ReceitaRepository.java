package br.com.financeiro.controlefinanceiro.repository;

import br.com.financeiro.controlefinanceiro.model.Receita;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReceitaRepository {

    private static final String ARQUIVO =
            "data/receitas.json";

    private final JsonRepositoryUtil json;


    public ReceitaRepository(
            JsonRepositoryUtil json
    ) {
        this.json = json;
    }


    public List<Receita> listar() {

        return json.lerLista(
                ARQUIVO,
                Receita.class
        );
    }


    public Optional<Receita> buscarPorId(
            Long id
    ) {

        return listar()
                .stream()

                .filter(
                        receita ->
                                receita
                                        .getId()
                                        .equals(id)
                )

                .findFirst();
    }


    public Receita salvar(
            Receita receita
    ) {

        List<Receita> receitas =
                listar();


        if (receita.getId() == null) {

            receita.setId(
                    json.proximoId(receitas)
            );

            receitas.add(receita);

        } else {

            receitas.removeIf(
                    r ->
                            r.getId()
                                    .equals(
                                            receita.getId()
                                    )
            );

            receitas.add(receita);
        }


        json.salvarLista(
                ARQUIVO,
                receitas
        );


        return receita;
    }


    public void excluir(Long id) {

        List<Receita> receitas =
                listar();


        receitas.removeIf(
                receita ->
                        receita
                                .getId()
                                .equals(id)
        );


        json.salvarLista(
                ARQUIVO,
                receitas
        );
    }
}