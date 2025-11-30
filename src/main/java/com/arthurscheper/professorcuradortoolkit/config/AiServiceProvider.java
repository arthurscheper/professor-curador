package com.arthurscheper.professorcuradortoolkit.config;

import com.arthurscheper.professorcuradortoolkit.service.AiService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class AiServiceProvider {

    private AiService aiService;

    @Inject
    private ChatModel chatModel;

    @Produces
    public AiService getAssistente() {
        if (aiService != null) {
            return aiService;
        }

        aiService = AiServices.create(AiService.class, chatModel);
        return aiService;
    }

}
