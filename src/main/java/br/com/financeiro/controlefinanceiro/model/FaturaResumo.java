package br.com.financeiro.controlefinanceiro.model;

import java.time.LocalDate;

public class FaturaResumo {

    private Long cartaoId;

    private String cartaoNome;

    private double valor;

    private LocalDate fechamento;

    private LocalDate vencimento;

    private long diasParaFechamento;

    private long diasParaVencimento;


    public FaturaResumo() {
    }


    public FaturaResumo(
            Long cartaoId,
            String cartaoNome,
            double valor,
            LocalDate fechamento,
            LocalDate vencimento,
            long diasParaFechamento,
            long diasParaVencimento
    ) {

        this.cartaoId = cartaoId;
        this.cartaoNome = cartaoNome;
        this.valor = valor;
        this.fechamento = fechamento;
        this.vencimento = vencimento;
        this.diasParaFechamento = diasParaFechamento;
        this.diasParaVencimento = diasParaVencimento;
    }


    public Long getCartaoId() {
        return cartaoId;
    }

    public void setCartaoId(Long cartaoId) {
        this.cartaoId = cartaoId;
    }


    public String getCartaoNome() {
        return cartaoNome;
    }

    public void setCartaoNome(String cartaoNome) {
        this.cartaoNome = cartaoNome;
    }


    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }


    public LocalDate getFechamento() {
        return fechamento;
    }

    public void setFechamento(LocalDate fechamento) {
        this.fechamento = fechamento;
    }


    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }


    public long getDiasParaFechamento() {
        return diasParaFechamento;
    }

    public void setDiasParaFechamento(long diasParaFechamento) {
        this.diasParaFechamento = diasParaFechamento;
    }


    public long getDiasParaVencimento() {
        return diasParaVencimento;
    }

    public void setDiasParaVencimento(long diasParaVencimento) {
        this.diasParaVencimento = diasParaVencimento;
    }


    public boolean isProxima() {

        return diasParaVencimento >= 0
                && diasParaVencimento <= 5;
    }


    public boolean isPertoDeFechar() {

        return diasParaFechamento >= 0
                && diasParaFechamento <= 5;
    }
}