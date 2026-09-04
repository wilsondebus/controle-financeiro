package br.com.financeiro.controlefinanceiro.model;

public class FormaPagamento {

    private Long id;
    private String nome;
    private TipoPagamento tipo;

    public FormaPagamento(){
    }

    public FormaPagamento(Long id, String nome, TipoPagamento tipo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoPagamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoPagamento tipo) {
        this.tipo = tipo;
    }
}
