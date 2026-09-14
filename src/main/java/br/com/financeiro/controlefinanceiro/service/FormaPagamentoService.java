package br.com.financeiro.controlefinanceiro.service;

import br.com.financeiro.controlefinanceiro.model.FormaPagamento;
import br.com.financeiro.controlefinanceiro.model.TipoPagamento;
import br.com.financeiro.controlefinanceiro.repository.FormaPagamentoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormaPagamentoService {

    private final FormaPagamentoRepository repository;

    public FormaPagamentoService(
            FormaPagamentoRepository repository
    ) {
        this.repository = repository;
    }

    @PostConstruct
    public void criarFormasPadrao() {

        if (repository.listar().isEmpty()) {

            repository.salvar(
                    new FormaPagamento(
                            null,
                            "PIX",
                            TipoPagamento.PIX
                    )
            );

            repository.salvar(
                    new FormaPagamento(
                            null,
                            "Dinheiro",
                            TipoPagamento.DINHEIRO
                    )
            );
        }
    }

    public List<FormaPagamento> listar() {
        return repository.listar();
    }

    public void salvar(FormaPagamento forma) {

        if (
                forma.getNome() != null
                        && !forma.getNome().isBlank()
                        && forma.getTipo() != null
        ) {

            forma.setNome(
                    forma.getNome().trim()
            );

            repository.salvar(forma);
        }
    }

    public void excluir(Long id) {
        repository.excluir(id);
    }

    public FormaPagamento buscarPorId(Long id) {

        if (id == null) {
            return null;
        }

        return listar()
                .stream()
                .filter(
                        forma ->
                                forma
                                        .getId()
                                        .equals(id)
                )
                .findFirst()
                .orElse(null);
    }

    public String nomePorId(Long id) {

        FormaPagamento forma =
                buscarPorId(id);

        if (forma != null) {
            return forma.getNome();
        }

        return "Não informado";
    }
}