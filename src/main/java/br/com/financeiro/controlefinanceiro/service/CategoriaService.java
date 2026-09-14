package br.com.financeiro.controlefinanceiro.service;

import br.com.financeiro.controlefinanceiro.model.Categoria;
import br.com.financeiro.controlefinanceiro.repository.CategoriaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void criarCategoriasPadrao() {

        if (repository.listar().isEmpty()) {

            repository.salvar(
                    new Categoria(null, "Alimentação")
            );

            repository.salvar(
                    new Categoria(null, "Lazer")
            );

            repository.salvar(
                    new Categoria(null, "Transporte")
            );

            repository.salvar(
                    new Categoria(null, "Saúde")
            );

            repository.salvar(
                    new Categoria(null, "Educação")
            );

            repository.salvar(
                    new Categoria(null, "Moradia")
            );

            repository.salvar(
                    new Categoria(null, "Assinaturas")
            );

            repository.salvar(
                    new Categoria(null, "Compras")
            );

            repository.salvar(
                    new Categoria(null, "Outros")
            );
        }
    }

    public List<Categoria> listar() {
        return repository.listar();
    }

    public void salvar(Categoria categoria) {

        if (
                categoria.getNome() != null
                        && !categoria.getNome().isBlank()
        ) {

            categoria.setNome(
                    categoria.getNome().trim()
            );

            repository.salvar(categoria);
        }
    }

    public void excluir(Long id) {
        repository.excluir(id);
    }

    public String nomePorId(Long id) {

        return listar()
                .stream()
                .filter(
                        categoria ->
                                categoria
                                        .getId()
                                        .equals(id)
                )
                .map(Categoria::getNome)
                .findFirst()
                .orElse("Sem categoria");
    }
}