package br.com.financeiro.controlefinanceiro.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Gasto {

    private Long id;
    private String descricao;
    private double valor;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data;

    private Long categoriaId;
    private Long formaPagamento;
    private Long cartaoId;
    private boolean parcelado;
    private int quantidadeParcelas = 1;
    private boolean possuiJuros;
    private double valorTotalComJuros;
    private String observacao;

    public Gasto(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaID) {
        this.categoriaId = categoriaID;
    }

    public Long getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(Long formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Long getCartaoId() {
        return cartaoId;
    }

    public void setCartaoId(Long cartaoId) {
        this.cartaoId = cartaoId;
    }

    public boolean isParcelado() {
        return parcelado;
    }

    public void setParcelado(boolean parcelado) {
        this.parcelado = parcelado;
    }

    public int getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public void setQuantidadeParcelas(int quantidadeParcelas) {
        this.quantidadeParcelas = quantidadeParcelas;
    }

    public boolean isPossuiJuros() {
        return possuiJuros;
    }

    public void setPossuiJuros(boolean possuiJuros) {
        this.possuiJuros = possuiJuros;
    }

    public double getValorTotalComJuros() {
        return valorTotalComJuros;
    }

    public void setValorTotalComJuros(double valorTotalComJuros) {
        this.valorTotalComJuros = valorTotalComJuros;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
