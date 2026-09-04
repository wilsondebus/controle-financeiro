package br.com.financeiro.controlefinanceiro.repository;

import br.com.financeiro.controlefinanceiro.model.FormaPagamento;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FormaPagamentoRepository {

    private static final String ARQUIVO =
            "data/formas-pagamento.json";

    private final JsonRepositoryUtil json;

    public FormaPagamentoRepository(
            JsonRepositoryUtil json
    ) {
        this.json = json;
    }

    public List<FormaPagamento> listar() {

        return json.lerLista(
                ARQUIVO,
                FormaPagamento.class
        );
    }

    public FormaPagamento salvar(
            FormaPagamento forma
    ) {

        List<FormaPagamento> formas = listar();

        if (forma.getId() == null) {

            forma.setId(
                    json.proximoId(formas)
            );

            formas.add(forma);

        } else {

            formas.removeIf(
                    f -> f.getId()
                            .equals(forma.getId())
            );

            formas.add(forma);
        }

        json.salvarLista(
                ARQUIVO,
                formas
        );

        return forma;
    }

    public void excluir(Long id) {

        List<FormaPagamento> formas = listar();

        formas.removeIf(
                forma ->
                        forma
                                .getId()
                                .equals(id)
        );

        json.salvarLista(
                ARQUIVO,
                formas
        );
    }
}
