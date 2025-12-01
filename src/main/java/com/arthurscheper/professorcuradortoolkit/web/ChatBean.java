package com.arthurscheper.professorcuradortoolkit.web;

import com.arthurscheper.professorcuradortoolkit.domain.Etapa;
import com.arthurscheper.professorcuradortoolkit.domain.FaseTrilha;
import com.arthurscheper.professorcuradortoolkit.domain.PerfilPedagogico;
import com.arthurscheper.professorcuradortoolkit.domain.ResultadoRefinamentoPrompt;
import com.arthurscheper.professorcuradortoolkit.domain.ValidacaoRequisicao;
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
import java.util.ArrayList;
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
    private List<String> preferenciasAbordagem = new ArrayList<>();
    private String requisicao;

    private Etapa etapaAtual;

    @Inject
    private AiService aiService;

    @Inject
    private FileService fileService;
    private ResultadoRefinamentoPrompt ultimoResultado;
    private ValidacaoRequisicao validacaoRequisicao;

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
        alterarEtapaAtual(Etapa.REFINAMENTO_PROMPT);
    }

    public void enviarPreferenciaAbordagem() {
        this.preferenciasAbordagem.add(preferenciaAbordagem);

        this.perfilPedagogico = aiService.enviarPreferenciaAbordagem(unidadeAprendizagem.getTituloUnidadeAprendizagem(), unidadeAprendizagem.getTopicosChave(), preferenciasAbordagem);
        alterarEtapaAtual(Etapa.CONFIRMAR_PERFIL_PEDAGOGICO);

        this.preferenciaAbordagem = "";
    }

    public void redefinirPerfilPedagogico() {
        alterarEtapaAtual(Etapa.DEFINIR_PERFIL_PEDAGOGICO);
    }

    public void confirmarPreferenciaAbordagem() {
        alterarEtapaAtual(Etapa.SELECIONAR_FASE_TRILHA);

        this.preferenciaAbordagem = "";
    }

    public boolean isRenderizarAnalisePlanoEnsino() {
        return etapaAtual.equals(Etapa.ANALISAR_PLANO_ENSINO);
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

    public boolean isRenderizarConfirmacaoPerfilPedagogico() {
        return etapaAtual.equals(Etapa.CONFIRMAR_PERFIL_PEDAGOGICO);
    }

    public boolean isRenderizarGerarPrompt() {
        return etapaAtual.equals(Etapa.REFINAMENTO_PROMPT);
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

    public FaseTrilha getFaseTrilha() {
        return faseTrilha;
    }

    public Etapa getEtapaAtual() {
        return etapaAtual;
    }

    public PerfilPedagogico getPerfilPedagogico() {
        return perfilPedagogico;
    }

    public String getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(String requisicao) {
        this.requisicao = requisicao;
    }

    public ResultadoRefinamentoPrompt getUltimoResultado() {
        return ultimoResultado;
    }

    public void setUltimoResultado(ResultadoRefinamentoPrompt ultimoResultado) {
        this.ultimoResultado = ultimoResultado;
    }

    public void setValidacaoRequisicao(ValidacaoRequisicao validacaoRequisicao) {
        this.validacaoRequisicao = validacaoRequisicao;
    }

    public void enviarRequisicao() {
        validacaoRequisicao = aiService.analisarRequisicao(perfilPedagogico, faseTrilha.getNome(), faseTrilha.getObjetivo(), requisicao);
        ultimoResultado = aiService.gerarPrompt(perfilPedagogico, validacaoRequisicao.getSinteseRequisicao(), requisicao);
    }

    public List<String> getPreferenciasAbordagem() {
        return preferenciasAbordagem;
    }

    public void setPreferenciasAbordagem(List<String> preferenciasAbordagem) {
        this.preferenciasAbordagem = preferenciasAbordagem;
    }
}
