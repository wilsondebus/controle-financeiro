package br.com.financeiro.controlefinanceiro.service;

import br.com.financeiro.controlefinanceiro.model.CartaoCredito;
import br.com.financeiro.controlefinanceiro.repository.CartaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartaoService {

    private final CartaoRepository repository;

    public CartaoService(
            CartaoRepository repository
    ) {
        this.repository = repository;
    }

    public List<CartaoCredito> listar() {
        return repository.listar();
    }

    public void salvar(
            CartaoCredito cartao
    ) {

        if (
                cartao.getNome() == null
                        || cartao.getNome().isBlank()
        ) {
            return;
        }

        if (
                cartao.getBanco() == null
                        || cartao.getBanco().isBlank()
        ) {
            return;
        }

        cartao.setNome(
                cartao.getNome().trim()
        );

        cartao.setBanco(
                cartao.getBanco().trim()
        );

        repository.salvar(cartao);
    }

    public void excluir(Long id) {
        repository.excluir(id);
    }

    public CartaoCredito buscarPorId(Long id) {

        return repository
                .buscarPorId(id)
                .orElse(null);
    }

    public String nomePorId(Long id) {

        CartaoCredito cartao =
                buscarPorId(id);

        if (cartao != null) {
            return cartao.getNome();
        }

        return "-";
    }
}