package com.arthurscheper.professorcuradortoolkit.domain;

public enum UltimoProcesso {

    NOVO_BLOCO("Selecionar outro bloco"),
    NOVA_UA("Selecionar outra Unidade de Aprendizagem"),
    NOVA_ETAPA_TRILHA("Selecionar outra etapa da trilha"),
    FINALIZAR("Finalizar");

    private final String label;

    UltimoProcesso(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
