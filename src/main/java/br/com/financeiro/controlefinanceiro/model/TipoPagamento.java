package br.com.financeiro.controlefinanceiro.model;

//ENUM -> Porque os tipos possíveis são limitados
public enum TipoPagamento {
    PIX("PIX"),
    DEBITO("Débito"),
    CREDITO("Crédito"),
    DINHEIRO("Dinheiro"),
    OUTRO("Outro");

    private final String descricao;

    TipoPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
