package com.arthurscheper.professorcuradortoolkit.bean;


import com.arthurscheper.professorcuradortoolkit.model.CursoDTO;
import com.arthurscheper.professorcuradortoolkit.service.GeminiService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import java.io.Serializable;

@RequestScoped
@Named
public class ChatBean implements Serializable {

    @Inject
    private GeminiService geminiService;

    private CursoDTO cursoDTO;

    private UploadedFile file;

    @PostConstruct
    public void init() {

    }

    public void upload() {
        if (file != null) {
            cursoDTO = geminiService.enviarArquivo(file);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();

        if (file != null) {
            cursoDTO = geminiService.enviarArquivo(file);
        }
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public CursoDTO getCursoDTO() {
        return cursoDTO;
    }

    public void setCursoDTO(CursoDTO cursoDTO) {
        this.cursoDTO = cursoDTO;
    }
}
