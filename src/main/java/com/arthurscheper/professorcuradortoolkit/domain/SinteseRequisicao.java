package com.arthurscheper.professorcuradortoolkit.domain;

import java.util.List;

public class SinteseRequisicao {

    private String objetivoValidado;
    private List<String> entregaveis;
    private String estrategiaAplicada;

    public SinteseRequisicao(String objetivoValidado, List<String> entregaveis, String estrategiaAplicada) {
        this.objetivoValidado = objetivoValidado;
        this.entregaveis = entregaveis;
        this.estrategiaAplicada = estrategiaAplicada;
    }

    public String getObjetivoValidado() {
        return objetivoValidado;
    }

    public void setObjetivoValidado(String objetivoValidado) {
        this.objetivoValidado = objetivoValidado;
    }

    public List<String> getEntregaveis() {
        return entregaveis;
    }

    public void setEntregaveis(List<String> entregaveis) {
        this.entregaveis = entregaveis;
    }

    public String getEstrategiaAplicada() {
        return estrategiaAplicada;
    }

    public void setEstrategiaAplicada(String estrategiaAplicada) {
        this.estrategiaAplicada = estrategiaAplicada;
    }
}
