package com.arthurscheper.professorcuradortoolkit.domain;

import java.io.Serializable;
import java.util.List;

public class Curso implements Serializable {

    private MetadadosCurso metadadosCurso;
    private List<Bloco> estrutura;

    public MetadadosCurso getMetadadosCurso() {
        return metadadosCurso;
    }

    public void setMetadadosCurso(MetadadosCurso metadadosCurso) {
        this.metadadosCurso = metadadosCurso;
    }

    public List<Bloco> getEstrutura() {
        return estrutura;
    }

    public void setEstrutura(List<Bloco> estrutura) {
        this.estrutura = estrutura;
    }
}
