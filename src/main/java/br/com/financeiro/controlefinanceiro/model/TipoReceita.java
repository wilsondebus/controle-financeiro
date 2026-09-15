package br.com.financeiro.controlefinanceiro.model;

public enum TipoReceita {

    SALARIO("Salário"),
    FREELANCE("Freelance"),
    VENDA("Venda"),
    REEMBOLSO("Reembolso"),
    OUTRO("Outro");

    private final String descricao;

    TipoReceita(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}