package com.arthurscheper.professorcuradortoolkit.web;

import com.arthurscheper.professorcuradortoolkit.domain.PerfilPedagogico;
import com.arthurscheper.professorcuradortoolkit.service.AiService;
import com.arthurscheper.professorcuradortoolkit.domain.Bloco;
import com.arthurscheper.professorcuradortoolkit.domain.Curso;
import com.arthurscheper.professorcuradortoolkit.domain.UnidadeAprendizagem;
import com.arthurscheper.professorcuradortoolkit.service.FileService;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import java.io.Serializable;

@ViewScoped
@Named
public class ChatBean implements Serializable {

    private Curso curso;
    private Bloco bloco;
    private UnidadeAprendizagem unidadeAprendizagem;
    private UploadedFile file;
    private String preferenciaAbordagem;
    
    @Inject
    private AiService aiService;

    @Inject
    private FileService fileService;

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();

        if (file != null) {
            curso = aiService.analisarPlanoEnsino(fileService.converterArquivo(file));
        }
    }

    public void selecionarBloco(Bloco bloco) {
        this.bloco = bloco;
    }

    public void selecionarUnidadeAprendizagem(UnidadeAprendizagem unidadeAprendizagem) {
        this.unidadeAprendizagem = unidadeAprendizagem;
    }

    public boolean isRenderizarBlocos() {
        return curso != null && bloco == null;
    }

    public boolean isRenderizarUnidadesAprendizagens() {
        return curso != null && bloco != null && unidadeAprendizagem == null;
    }

    public boolean isRenderizarUnidadeAprendizagemSelecionada() {
        return curso != null && bloco != null && unidadeAprendizagem != null;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Bloco getBloco() {
        return bloco;
    }

    public void setBloco(Bloco bloco) {
        this.bloco = bloco;
    }

    public UnidadeAprendizagem getUnidadeAprendizagem() {
        return unidadeAprendizagem;
    }

    public void setUnidadeAprendizagem(UnidadeAprendizagem unidadeAprendizagem) {
        this.unidadeAprendizagem = unidadeAprendizagem;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public String getPreferenciaAbordagem() {
        return preferenciaAbordagem;
    }

    public void setPreferenciaAbordagem(String preferenciaAbordagem) {
        this.preferenciaAbordagem = preferenciaAbordagem;
    }

    public void enviarPreferenciaAbordagem() {
        PerfilPedagogico perfilPedagogico = aiService.enviarPreferenciaAbordagem(
                unidadeAprendizagem.getTituloUnidadeAprendizagem(), unidadeAprendizagem.getTopicosChave(), preferenciaAbordagem);

        System.out.println(perfilPedagogico);
    }
}
