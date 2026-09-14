package br.com.financeiro.controlefinanceiro.service;

import br.com.financeiro.controlefinanceiro.model.CompraFutura;
import br.com.financeiro.controlefinanceiro.repository.CompraFuturaRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class CompraFuturaService {

    private final CompraFuturaRepository repository;

    public CompraFuturaService(
            CompraFuturaRepository repository
    ) {

        this.repository = repository;
    }

    public List<CompraFutura> listar() {

        return repository
                .listar()
                .stream()
                .sorted(
                        Comparator.comparing(
                                CompraFutura::getDataPrevista,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()
                                )
                        )
                )
                .toList();
    }

    public void salvar(
            CompraFutura compra
    ) {

        repository.salvar(compra);
    }

    public void excluir(Long id) {

        repository.excluir(id);
    }

    public void alternarRealizada(
            Long id
    ) {

        CompraFutura compra =
                repository
                        .buscarPorId(id)
                        .orElse(null);

        if (compra != null) {

            compra.setRealizada(
                    !compra.isRealizada()
            );

            repository.salvar(
                    compra
            );
        }
    }
}