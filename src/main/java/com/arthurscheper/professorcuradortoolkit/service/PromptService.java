package com.arthurscheper.professorcuradortoolkit.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;

@ApplicationScoped
public class PromptService {

    private JsonNode root;

    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getResourceAsStream("/prompts.json");
            root = mapper.readTree(is);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar prompts.json", e);
        }
    }

    public String get(String prompt) {
        JsonNode node = root.path(prompt);
        return node.isMissingNode() ? null : node.toPrettyString();
    }

}