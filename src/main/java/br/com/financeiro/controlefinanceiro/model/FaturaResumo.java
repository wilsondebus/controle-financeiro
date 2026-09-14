package br.com.financeiro.controlefinanceiro.model;

import java.time.LocalDate;

public class FaturaResumo {

    private Long cartaoId;

    private String cartaoNome;

    private double valor;

    private LocalDate vencimento;

    private long diasParaVencimento;

    public FaturaResumo() {
    }

    public FaturaResumo(
            Long cartaoId,
            String cartaoNome,
            double valor,
            LocalDate vencimento,
            long diasParaVencimento
    ) {

        this.cartaoId = cartaoId;
        this.cartaoNome = cartaoNome;
        this.valor = valor;
        this.vencimento = vencimento;
        this.diasParaVencimento =
                diasParaVencimento;
    }

    public Long getCartaoId() {
        return cartaoId;
    }

    public void setCartaoId(
            Long cartaoId
    ) {
        this.cartaoId = cartaoId;
    }

    public String getCartaoNome() {
        return cartaoNome;
    }

    public void setCartaoNome(
            String cartaoNome
    ) {
        this.cartaoNome = cartaoNome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(
            double valor
    ) {
        this.valor = valor;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(
            LocalDate vencimento
    ) {
        this.vencimento = vencimento;
    }

    public long getDiasParaVencimento() {
        return diasParaVencimento;
    }

    public void setDiasParaVencimento(
            long diasParaVencimento
    ) {
        this.diasParaVencimento =
                diasParaVencimento;
    }

    public boolean isProxima() {

        return diasParaVencimento >= 0
                && diasParaVencimento <= 5;
    }
}