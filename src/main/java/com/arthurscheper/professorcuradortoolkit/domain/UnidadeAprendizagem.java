package com.arthurscheper.professorcuradortoolkit.domain;

import java.io.Serializable;
import java.util.List;

public class UnidadeAprendizagem implements Serializable {

    private String tituloUnidadeAprendizagem;
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