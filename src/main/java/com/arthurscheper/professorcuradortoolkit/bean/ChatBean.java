package com.arthurscheper.professorcuradortoolkit.bean;


import com.arthurscheper.professorcuradortoolkit.service.GeminiService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.file.UploadedFile;

import java.io.Serializable;

@RequestScoped
@Named
public class ChatBean implements Serializable {

    @Inject
    private GeminiService geminiService;

    private String mensagem;

    private UploadedFile file;

    @PostConstruct
    public void init() {
        try {
            mensagem = geminiService.enviarMensagem("Olá, Gemini!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upload() {
        if (file != null) {
        }
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
}
