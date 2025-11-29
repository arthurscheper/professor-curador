package com.arthurscheper.professorcuradortoolkit.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class MetadadosCursoDTO implements Serializable {

    @JsonProperty("nome_disciplina")
    private String nomeDisciplina;

    @JsonProperty("curso_sugerido")
    private String cursoSugerido;

    @JsonProperty("carga_horaria")
    private String cargaHoraria;

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getCursoSugerido() {
        return cursoSugerido;
    }

    public void setCursoSugerido(String cursoSugerido) {
        this.cursoSugerido = cursoSugerido;
    }

    public String getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(String cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }
}