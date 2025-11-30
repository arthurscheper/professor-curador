package com.arthurscheper.professorcuradortoolkit.domain;

import java.io.Serializable;
import java.util.List;

public class Bloco implements Serializable {

    private String tituloBloco;
    private List<UnidadeAprendizagem> unidadesAprendizagem;

    public String getTituloBloco() {
        return tituloBloco;
    }

    public void setTituloBloco(String tituloBloco) {
        this.tituloBloco = tituloBloco;
    }

    public List<UnidadeAprendizagem> getUnidadesAprendizagem() {
        return unidadesAprendizagem;
    }

    public void setUnidadesAprendizagem(List<UnidadeAprendizagem> unidadesAprendizagem) {
        this.unidadesAprendizagem = unidadesAprendizagem;
    }
}