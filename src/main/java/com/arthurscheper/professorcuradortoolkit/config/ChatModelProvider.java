package com.arthurscheper.professorcuradortoolkit.config;

import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ChatModelProvider {

    @Inject
    @ConfigProperty(name = "gemini.api.key")
    private String API_KEY;

    @Inject
    @ConfigProperty(name = "gemini.model.name")
    private String MODEL_NAME;

    private ChatModel chatModel;

    @Produces
    public ChatModel getChatModel() {
        if (chatModel != null) {
            return chatModel;
        }

        chatModel = GoogleAiGeminiChatModel.builder()
                                      .apiKey(API_KEY)
                                      .modelName(MODEL_NAME)
                                      .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                                      .logRequests(true)
                                      .logResponses(true)
                                      .build();
        return chatModel;
    }

}
