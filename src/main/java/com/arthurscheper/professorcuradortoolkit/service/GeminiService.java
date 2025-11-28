package com.arthurscheper.professorcuradortoolkit.service;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GeminiService {

    private static final String API_KEY = "AIzaSyCrLmuJi-nXvB2VUyA6oR5I-FJtkLf8jfc";
    private static final String MODEL = "gem/55d090365b06";

    public String enviarMensagem(String mensagem) {
        ChatModel gemini = GoogleAiGeminiChatModel.builder()
                .apiKey(API_KEY)
                .modelName("gemini-2.5-flash")
                .build();

        return gemini.chat(mensagem);
    }

}
