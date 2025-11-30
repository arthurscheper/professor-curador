package com.arthurscheper.professorcuradortoolkit.domain;

import dev.langchain4j.model.output.structured.Description;
import java.util.List;

public class ValidacaoRequisicao {

    public enum StatusAdequacao {
        ADEQUADO,
        NAO_ADEQUADO
    }

    @Description("Classificação da requisição. Deve ser estritamente ADEQUADO se o pedido faz sentido para a etapa atual, ou NAO_ADEQUADO caso contrário.")
    private StatusAdequacao adequacaoEtapa;

    @Description("Explicação clara do porquê o pedido foi aceito ou rejeitado dada a etapa atual.")
    private String justificativaAdequacao;

    @Description("Se adequado: uma frase sintetizando o entendimento (ex: 'Entendi que você deseja criar...'). Se não adequado, deve ser null.")
    private String resumoEntendimento;

    @Description("Lista de artefatos ou conteúdos que serão gerados. Se não adequado, deve ser null.")
    private List<String> artefatosAGerar;

    @Description("Se adequado: breve explicação de como o perfil pedagógico será aplicado nesta geração. Se não adequado, deve ser null.")
    private String estrategiaAdotada;

    // Getters e Setters

    public StatusAdequacao getAdequacaoEtapa() {
        return adequacaoEtapa;
    }

    public void setAdequacaoEtapa(StatusAdequacao adequacaoEtapa) {
        this.adequacaoEtapa = adequacaoEtapa;
    }

    public String getJustificativaAdequacao() {
        return justificativaAdequacao;
    }

    public void setJustificativaAdequacao(String justificativaAdequacao) {
        this.justificativaAdequacao = justificativaAdequacao;
    }

    public String getResumoEntendimento() {
        return resumoEntendimento;
    }

    public void setResumoEntendimento(String resumoEntendimento) {
        this.resumoEntendimento = resumoEntendimento;
    }

    public List<String> getArtefatosAGerar() {
        return artefatosAGerar;
    }

    public void setArtefatosAGerar(List<String> artefatosAGerar) {
        this.artefatosAGerar = artefatosAGerar;
    }

    public String getEstrategiaAdotada() {
        return estrategiaAdotada;
    }

    public void setEstrategiaAdotada(String estrategiaAdotada) {
        this.estrategiaAdotada = estrategiaAdotada;
    }
}