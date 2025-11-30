package com.arthurscheper.professorcuradortoolkit.web;

import com.arthurscheper.professorcuradortoolkit.domain.Etapa;
import com.arthurscheper.professorcuradortoolkit.domain.FaseTrilha;
import com.arthurscheper.professorcuradortoolkit.domain.PerfilPedagogico;
import com.arthurscheper.professorcuradortoolkit.service.AiService;
import com.arthurscheper.professorcuradortoolkit.domain.Bloco;
import com.arthurscheper.professorcuradortoolkit.domain.Curso;
import com.arthurscheper.professorcuradortoolkit.domain.UnidadeAprendizagem;
import com.arthurscheper.professorcuradortoolkit.service.FileService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@ViewScoped
@Named
public class ChatBean implements Serializable {

    private Curso curso;
    private Bloco bloco;
    private UnidadeAprendizagem unidadeAprendizagem;
    private PerfilPedagogico perfilPedagogico;
    private FaseTrilha faseTrilha;
    private UploadedFile file;
    private String preferenciaAbordagem;

    private Etapa etapaAtual;

    @Inject
    private AiService aiService;

    @Inject
    private FileService fileService;

    @PostConstruct
    public void init() {
        this.etapaAtual = Etapa.ANALISAR_PLANO_ENSINO;
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();

        if (file != null) {
            curso = aiService.analisarPlanoEnsino(fileService.converterArquivo(file));
        }

        alterarEtapaAtual(Etapa.SELECIONAR_BLOCO);
    }

    private void alterarEtapaAtual(Etapa etapa) {
        this.etapaAtual = etapa;
    }

    public void selecionarBloco(Bloco bloco) {
        this.bloco = bloco;
        alterarEtapaAtual(Etapa.SELECIONAR_UNIDADE_APRENDIZAGEM);
    }

    public void selecionarUnidadeAprendizagem(UnidadeAprendizagem unidadeAprendizagem) {
        this.unidadeAprendizagem = unidadeAprendizagem;
        alterarEtapaAtual(Etapa.DEFINIR_PERFIL_PEDAGOGICO);
    }

    public void selecionarEtapaTrilha(FaseTrilha faseTrilha) {
        this.faseTrilha = faseTrilha;
    }

    public void enviarPreferenciaAbordagem() {
        this.perfilPedagogico = aiService.enviarPreferenciaAbordagem(unidadeAprendizagem.getTituloUnidadeAprendizagem(), unidadeAprendizagem.getTopicosChave(), preferenciaAbordagem);
        alterarEtapaAtual(Etapa.SELECIONAR_FASE_TRILHA);
    }

    public boolean isRenderizarBlocos() {
        return etapaAtual.equals(Etapa.SELECIONAR_BLOCO);
    }

    public boolean isRenderizarUnidadesAprendizagens() {
        return etapaAtual.equals(Etapa.SELECIONAR_UNIDADE_APRENDIZAGEM);
    }

    public boolean isRenderizarUnidadeAprendizagemSelecionada() {
        return etapaAtual.equals(Etapa.DEFINIR_PERFIL_PEDAGOGICO);
    }

    public boolean isRenderizarEtapaTrilha() {
        return etapaAtual.equals(Etapa.SELECIONAR_FASE_TRILHA);
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

    public List<FaseTrilha> getEtapasTrilha() {
        return Arrays.asList(FaseTrilha.values());
    }

    public FaseTrilha getEtapaTrilha() {
        return faseTrilha;
    }

    public Etapa getEtapaAtual() {
        return etapaAtual;
    }

    public PerfilPedagogico getPerfilPedagogico() {
        return perfilPedagogico;
    }
}
