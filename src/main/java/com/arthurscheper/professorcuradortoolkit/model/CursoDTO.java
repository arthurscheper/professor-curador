package com.arthurscheper.professorcuradortoolkit.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public class CursoDTO implements Serializable {

    @JsonProperty("metadados_curso")
    private MetadadosCursoDTO metadadosCurso;

    @JsonProperty("estrutura")
    private List<BlocoDTO> estrutura;

    public MetadadosCursoDTO getMetadadosCurso() {
        return metadadosCurso;
    }

    public void setMetadadosCurso(MetadadosCursoDTO metadadosCurso) {
        this.metadadosCurso = metadadosCurso;
    }

    public List<BlocoDTO> getEstrutura() {
        return estrutura;
    }

    public void setEstrutura(List<BlocoDTO> estrutura) {
        this.estrutura = estrutura;
    }
}