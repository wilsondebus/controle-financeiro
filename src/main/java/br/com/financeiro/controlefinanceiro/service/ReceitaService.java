package br.com.financeiro.controlefinanceiro.service;

import br.com.financeiro.controlefinanceiro.model.Receita;
import br.com.financeiro.controlefinanceiro.repository.ReceitaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;

@Service
public class ReceitaService {

    private final ReceitaRepository repository;


    public ReceitaService(
            ReceitaRepository repository
    ) {
        this.repository = repository;
    }


    public List<Receita> listar() {

        return repository
                .listar()
                .stream()

                .sorted(
                        Comparator
                                .comparing(
                                        Receita::getData
                                )
                                .reversed()
                )

                .toList();
    }


    public Receita buscarPorId(Long id) {

        return repository
                .buscarPorId(id)
                .orElse(null);
    }


    public void salvar(
            Receita receita
    ) {

        if (receita.getData() == null) {

            receita.setData(
                    LocalDate.now()
            );
        }


        repository.salvar(
                receita
        );
    }


    public void excluir(Long id) {

        repository.excluir(id);
    }


    public double totalReceitasMesAtual() {

        YearMonth mesAtual =
                YearMonth.now();


        return repository
                .listar()
                .stream()

                .filter(
                        receita ->
                                receita.getData()
                                        != null
                )

                .filter(
                        receita ->
                                YearMonth
                                        .from(
                                                receita.getData()
                                        )
                                        .equals(mesAtual)
                )

                .mapToDouble(
                        Receita::getValor
                )

                .sum();
    }


    public double totalReceitasAteHoje() {

        LocalDate hoje =
                LocalDate.now();


        return repository
                .listar()
                .stream()

                .filter(
                        receita ->
                                receita.getData()
                                        != null
                )

                .filter(
                        receita ->
                                !receita
                                        .getData()
                                        .isAfter(hoje)
                )

                .mapToDouble(
                        Receita::getValor
                )

                .sum();
    }
}