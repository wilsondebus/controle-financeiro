package br.com.financeiro.controlefinanceiro.model;

public class CartaoCredito {

    private Long id;
    private String nome;
    private String banco;
    private int diaFechamento;
    private int diaVencimento;

    public CartaoCredito(){
    }

    public CartaoCredito(Long id, String nome, String banco, int diaFechamento, int diaVencimento) {
        this.id = id;
        this.nome = nome;
        this.banco = banco;
        this.diaFechamento = diaFechamento;
        this.diaVencimento = diaVencimento;
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

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public int getDiaFechamento() {
        return diaFechamento;
    }

    public void setDiaFechamento(int diaFechamento) {
        this.diaFechamento = diaFechamento;
    }

    public int getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(int diaVencimento) {
        this.diaVencimento = diaVencimento;
    }
}
