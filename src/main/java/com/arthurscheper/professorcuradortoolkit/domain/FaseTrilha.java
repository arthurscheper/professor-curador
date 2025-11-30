package com.arthurscheper.professorcuradortoolkit.domain;

public enum FaseTrilha {

    APRESENTACAO(1, "Apresentação", "Introduzir o tema, despertar interesse e mostrar relevância."),
    TEORIA(2, "Teoria", "Aprofundar conceitos técnicos e fundamentação."),
    ESTUDO_DE_CASO(3, "Estudo de Caso", "Contextualizar a teoria em uma situação real ou simulada."),
    PRATICA_HANDS_ON(4, "Prática (Hands-on)", "Aplicação ativa do conhecimento, exercícios ou laboratório."),
    AVALIACAO_QUIZ(5, "Avaliação/Quiz", "Verificar a retenção de conhecimento e feedback.");

    private final int id;
    private final String nome;
    private final String objetivo;

    FaseTrilha(int id, String nome, String objetivo) {
        this.id = id;
        this.nome = nome;
        this.objetivo = objetivo;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getObjetivo() {
        return objetivo;
    }

    // Método utilitário para buscar o Enum pelo ID numérico
    // Ex: FaseTrilha.fromId(1) retorna FaseTrilha.APRESENTACAO
    public static FaseTrilha fromId(int id) {
        for (FaseTrilha etapa : values()) {
            if (etapa.getId() == id) {
                return etapa;
            }
        }
        throw new IllegalArgumentException("ID de etapa inválido: " + id);
    }
}