package br.com.financeiro.controlefinanceiro.repository;

import br.com.financeiro.controlefinanceiro.model.Categoria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoriaRepository {

    private static final String ARQUIVO =
            "data/categorias.json";

    private final JsonRepositoryUtil json;

    public CategoriaRepository(
            JsonRepositoryUtil json
    ) {
        this.json = json;
    }

    public List<Categoria> listar() {

        return json.lerLista(
                ARQUIVO,
                Categoria.class
        );
    }

    public Categoria salvar(
            Categoria categoria
    ) {

        List<Categoria> categorias = listar();

        if (categoria.getId() == null) {

            categoria.setId(
                    json.proximoId(categorias)
            );

            categorias.add(categoria);

        } else {

            categorias.removeIf(
                    c -> c.getId()
                            .equals(categoria.getId())
            );

            categorias.add(categoria);
        }

        json.salvarLista(
                ARQUIVO,
                categorias
        );

        return categoria;
    }

    public void excluir(Long id) {

        List<Categoria> categorias = listar();

        categorias.removeIf(
                categoria ->
                        categoria
                                .getId()
                                .equals(id)
        );

        json.salvarLista(
                ARQUIVO,
                categorias
        );
    }
}
