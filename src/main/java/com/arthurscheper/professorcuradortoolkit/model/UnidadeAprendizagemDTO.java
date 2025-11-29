package com.arthurscheper.professorcuradortoolkit.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public class UnidadeAprendizagemDTO implements Serializable {

    @JsonProperty("titulo_ua")
    private String tituloUnidadeAprendizagem;

    @JsonProperty("topicos_chave")
    private List<String> topicosChave;

    public String getTituloUnidadeAprendizagem() {
        return tituloUnidadeAprendizagem;
    }

    public void setTituloUnidadeAprendizagem(String tituloUnidadeAprendizagem) {
        this.tituloUnidadeAprendizagem = tituloUnidadeAprendizagem;
    }

    public List<String> getTopicosChave() {
        return topicosChave;
    }

    public void setTopicosChave(List<String> topicosChave) {
        this.topicosChave = topicosChave;
    }
}