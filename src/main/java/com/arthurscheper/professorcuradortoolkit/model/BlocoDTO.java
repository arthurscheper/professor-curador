package com.arthurscheper.professorcuradortoolkit.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public class BlocoDTO implements Serializable {

    @JsonProperty("titulo_bloco")
    private String tituloBloco;

    @JsonProperty("unidades_aprendizagem")
    private List<UnidadeAprendizagemDTO> unidadesAprendizagem;

    public String getTituloBloco() {
        return tituloBloco;
    }

    public void setTituloBloco(String tituloBloco) {
        this.tituloBloco = tituloBloco;
    }

    public List<UnidadeAprendizagemDTO> getUnidadesAprendizagem() {
        return unidadesAprendizagem;
    }

    public void setUnidadesAprendizagem(List<UnidadeAprendizagemDTO> unidadesAprendizagem) {
        this.unidadesAprendizagem = unidadesAprendizagem;
    }
}