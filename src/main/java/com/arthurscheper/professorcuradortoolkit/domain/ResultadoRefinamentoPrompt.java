package com.arthurscheper.professorcuradortoolkit.domain;

import dev.langchain4j.model.output.structured.Description;

import java.io.Serializable;
import java.util.List;

public class ResultadoRefinamentoPrompt implements Serializable {

    @Description("O texto completo do prompt sugerido, escrito na primeira pessoa (usuário para IA).")
    private String sugestaoPrompt;

    @Description("Análise técnica e crítica dos pontos fracos ou limitações da versão atual do prompt.")
    private String criticaTecnica;

    @Description("Lista com exatamente 3 perguntas estratégicas para obter detalhes e refinar a próxima versão.")
    private List<String> perguntasRefinamento;

    public String getCriticaTecnica() {
        return criticaTecnica;
    }

    public void setCriticaTecnica(String criticaTecnica) {
        this.criticaTecnica = criticaTecnica;
    }

    public List<String> getPerguntasRefinamento() {
        return perguntasRefinamento;
    }

    public void setPerguntasRefinamento(List<String> perguntasRefinamento) {
        this.perguntasRefinamento = perguntasRefinamento;
    }

    public String getSugestaoPrompt() {
        return sugestaoPrompt;
    }

    public void setSugestaoPrompt(String sugestaoPrompt) {
        this.sugestaoPrompt = sugestaoPrompt;
    }
}