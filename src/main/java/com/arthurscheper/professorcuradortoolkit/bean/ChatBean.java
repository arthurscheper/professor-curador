package com.arthurscheper.professorcuradortoolkit.bean;


import com.arthurscheper.professorcuradortoolkit.model.BlocoDTO;
import com.arthurscheper.professorcuradortoolkit.model.CursoDTO;
import com.arthurscheper.professorcuradortoolkit.model.MetadadosCursoDTO;
import com.arthurscheper.professorcuradortoolkit.model.UnidadeAprendizagemDTO;
import com.arthurscheper.professorcuradortoolkit.service.GeminiService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import java.io.Serializable;
import java.util.ArrayList;

@ViewScoped
@Named
public class ChatBean implements Serializable {

    @Inject
    private GeminiService geminiService;

    private CursoDTO cursoDTO;
    private BlocoDTO blocoDTO;
    private UnidadeAprendizagemDTO unidadeAprendizagemDTO;

    private UploadedFile file;

    private boolean mock;

    @PostConstruct
    public void init() {
        if (mock) {
            cursoDTO = new CursoDTO();
            cursoDTO.setMetadadosCurso(new MetadadosCursoDTO());
            cursoDTO.getMetadadosCurso().setCursoSugerido("Engenharia Mecânica");
            cursoDTO.setEstrutura(new ArrayList<>());

            blocoDTO = new BlocoDTO();
            blocoDTO.setTituloBloco("Bloco 1");
            blocoDTO.setUnidadesAprendizagem(new ArrayList<>());

            unidadeAprendizagemDTO = new UnidadeAprendizagemDTO();
            unidadeAprendizagemDTO.setTituloUnidadeAprendizagem("Conceitos e Ciclo de Vida da Gestão de Projetos");

            blocoDTO.getUnidadesAprendizagem().add(unidadeAprendizagemDTO);
            cursoDTO.getEstrutura().add(blocoDTO);
        }
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

    public void selecionarBloco(BlocoDTO blocoDTO) {
        this.blocoDTO = blocoDTO;
    }

    public void selecionarUnidadeAprendizagem(UnidadeAprendizagemDTO unidadeAprendizagemDTO) {
        this.unidadeAprendizagemDTO = unidadeAprendizagemDTO;
    }

    public boolean isRenderizarBlocos() {
        return cursoDTO != null && blocoDTO == null;
    }

    public boolean isRenderizarUnidadesAprendizagens() {
        return cursoDTO != null && blocoDTO != null && unidadeAprendizagemDTO == null;
    }

    public boolean isRenderizarUnidadeAprendizagemSelecionada() {
        return cursoDTO != null && blocoDTO != null && unidadeAprendizagemDTO != null;
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

    public BlocoDTO getBlocoDTO() {
        return blocoDTO;
    }

    public void setBlocoDTO(BlocoDTO blocoDTO) {
        this.blocoDTO = blocoDTO;
    }

    public UnidadeAprendizagemDTO getUnidadeAprendizagemDTO() {
        return unidadeAprendizagemDTO;
    }

    public void setUnidadeAprendizagemDTO(UnidadeAprendizagemDTO unidadeAprendizagemDTO) {
        this.unidadeAprendizagemDTO = unidadeAprendizagemDTO;
    }
}
