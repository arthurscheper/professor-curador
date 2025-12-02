package com.arthurscheper.professorcuradortoolkit.web;

import com.arthurscheper.professorcuradortoolkit.domain.*;
import com.arthurscheper.professorcuradortoolkit.service.AiService;
import com.arthurscheper.professorcuradortoolkit.service.FileService;
import dev.langchain4j.data.message.PdfFileContent;
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
import java.util.stream.Collectors;

@ViewScoped
@Named
public class ChatBean implements Serializable {

    private final Chat chat = new Chat();

    private Curso curso;
    private Bloco bloco;
    private UnidadeAprendizagem unidadeAprendizagem;
    private PerfilPedagogico perfilPedagogico;
    private FaseTrilha faseTrilha;
    private UploadedFile file;
    private PdfFileContent arquivoConvertido;
    private String mensagemUsuario;
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
        chat.adicionarMensagemIA("Olá, Professor Curador!");
        chat.adicionarMensagemIA("Eu irei lhe auxiliar a gerar materiais a partir de <strong>planos de ensino</strong> das unidades curriculares.");
        chat.adicionarMensagemIA("Para darmos início ao processo, por favor envie o anexo do plano de ensino no formato <strong>PDF</strong> através dos botões baixo.");

        chat.adicionarUploadArquivo();

        this.etapaAtual = Etapa.ANALISAR_PLANO_ENSINO;
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        arquivoConvertido = fileService.converterArquivo(file);

        chat.removerUltimaMensagem();
        chat.adicionarMensagemUsuario(file.getFileName());
        chat.adicionarMensagemIA("Muito bem, recebi o arquivo. Por favor, aguarde enquanto analiso o plano de ensino enviado.");
        chat.adicionarMensagemIADigitando();

        alterarEtapaAtual(Etapa.ANALISANDO_PLANO_ENSINO);
    }

    public void analisarPlanoEnsino() {
        if (arquivoConvertido != null) {
            curso = aiService.analisarPlanoEnsino(arquivoConvertido);
        }

        chat.removerMensagemDigitando();

        chat.adicionarMensagemIA("Excelente! Analisei o plano de ensino da unidade curricular <strong>" + curso.getMetadadosCurso().getNomeDisciplina() + "</strong> e separei ela em blocos e unidades de aprendizagem.");
        chat.adicionarMensagemIA("Para iniciarmos o desenvolvimento da trilha de aprendizagem, por favor selecione um dos blocos abaixo:");

        chat.adicionarOpcoes(curso.getEstrutura().stream().map(bloco -> new OpcaoChat(bloco.getTituloBloco(), bloco)).collect(Collectors.toList()));

        alterarEtapaAtual(Etapa.SELECIONAR_BLOCO);
    }

    private void alterarEtapaAtual(Etapa etapa) {
        this.etapaAtual = etapa;
    }

    public void selecionarBloco(Bloco bloco) {
        this.bloco = bloco;

        chat.adicionarMensagemIA("Ótimo! Trabalharemos com o bloco <strong>" + bloco.getTituloBloco() + "</strong>.");
        chat.adicionarMensagemIA("Para iniciarmos o desenvolvimento da trilha de aprendizagem, por favor selecione uma das unidades de aprendizagens abaixo:");

        chat.adicionarOpcoes(bloco.getUnidadesAprendizagem().stream().map(unidadeAprendizagem -> new OpcaoChat(unidadeAprendizagem.getTituloUnidadeAprendizagem(), unidadeAprendizagem)).collect(Collectors.toList()));

        alterarEtapaAtual(Etapa.SELECIONAR_UNIDADE_APRENDIZAGEM);
    }

    public void selecionar(MensagemChat mensagemChat, OpcaoChat opcaoChat) {
        if (opcaoChat.getValor() instanceof Bloco) {
            selecionarBloco((Bloco) opcaoChat.getValor());
        } else if (opcaoChat.getValor() instanceof UnidadeAprendizagem) {
            selecionarUnidadeAprendizagem((UnidadeAprendizagem) opcaoChat.getValor());
        } else if (opcaoChat.getValor() instanceof OpcaoConfirmarPerfilPedagogico) {
            confirmarPerfilPedagogico(((OpcaoConfirmarPerfilPedagogico) opcaoChat.getValor()).isConfirmar());
        } else if (opcaoChat.getValor() instanceof FaseTrilha) {
            selecionarEtapaTrilha((FaseTrilha) opcaoChat.getValor());
        } else if (opcaoChat.getValor() instanceof ContinuarRefinamento) {
            continuarRefinamento((ContinuarRefinamento) opcaoChat.getValor());
        } else if (opcaoChat.getValor() instanceof OpcaoUltimoProcesso) {
            escolherNovoProcesso(opcaoChat);
        }

        opcaoChat.setSelecionado(true);

        mensagemChat.getOpcoes().forEach(opcao -> opcao.setDesabilitar(true));
    }

    private void escolherNovoProcesso(OpcaoChat opcaoChat) {
        var opcaoUltimoProcesso = (OpcaoUltimoProcesso) opcaoChat.getValor();

        if (UltimoProcesso.FINALIZAR.equals(opcaoUltimoProcesso.getProcesso())) {
            chat.adicionarMensagemIA("Certo. Muito obrigado!");
        } else if (UltimoProcesso.NOVO_BLOCO.equals(opcaoUltimoProcesso.getProcesso())) {
            chat.adicionarMensagemIA("Muito bem, selecione o bloco que deseja trabalhar:");
            chat.adicionarOpcoes(curso.getEstrutura().stream().map(bloco -> new OpcaoChat(bloco.getTituloBloco(), bloco)).collect(Collectors.toList()));

            alterarEtapaAtual(Etapa.SELECIONAR_BLOCO);
        } else if (UltimoProcesso.NOVA_UA.equals(opcaoUltimoProcesso.getProcesso())) {
            chat.adicionarMensagemIA("Muito bem, seleciona a Unidade de Aprendizagem que deseja trabalhar:");
            chat.adicionarOpcoes(bloco.getUnidadesAprendizagem().stream().map(unidadeAprendizagem -> new OpcaoChat(unidadeAprendizagem.getTituloUnidadeAprendizagem(), unidadeAprendizagem)).collect(Collectors.toList()));

            alterarEtapaAtual(Etapa.SELECIONAR_UNIDADE_APRENDIZAGEM);
        } else if (UltimoProcesso.NOVA_ETAPA_TRILHA.equals(opcaoUltimoProcesso.getProcesso())) {
            chat.adicionarMensagemIA("Muito bem, selecione a etapa da trilha desejada para começarmos.");
            chat.adicionarOpcoes(getEtapasTrilha().stream().map(etapa -> new OpcaoChat(etapa.getNome(), etapa)).collect(Collectors.toList()));

            alterarEtapaAtual(Etapa.SELECIONAR_FASE_TRILHA);
        }
    }

    private void continuarRefinamento(ContinuarRefinamento valor) {
        if (valor.isContinuar()) {
            chat.adicionarMensagemIA("Muito bem, vamos continuar com o processo de desenvolvimento da trilha de aprendizagem.");
            chat.adicionarMensagemIA("Responda as questões de refinamento e coloque suas considerações.");
            return;
        }

        chat.adicionarMensagemIA("Aqui está seu prompt finalizado.");
        chat.adicionarMensagemIA(ultimoResultado.getSugestaoPrompt());

        chat.adicionarMensagemIA("Deseja finalizar o processo, trabalhar com outra UA ou ir para outra etapa da trilha?");
        chat.adicionarOpcoes(Arrays.stream(UltimoProcesso.values()).map(ultimoProcesso -> new OpcaoChat(ultimoProcesso.getLabel(), new OpcaoUltimoProcesso(ultimoProcesso))).collect(Collectors.toList()));
    }

    public void selecionarUnidadeAprendizagem(UnidadeAprendizagem unidadeAprendizagem) {
        this.unidadeAprendizagem = unidadeAprendizagem;

        chat.adicionarMensagemIA("Muito bem, vamos iniciar a trilha de aprendizagem da unidade de aprendizagem <strong>" + unidadeAprendizagem.getTituloUnidadeAprendizagem() + "</strong>.");
        chat.adicionarMensagemIA("Descreva abaixo como você gostaria de abordar a unidade de aprendizagem selecionada.");

        alterarEtapaAtual(Etapa.DEFINIR_PERFIL_PEDAGOGICO);
    }

    public void selecionarEtapaTrilha(FaseTrilha faseTrilha) {
        this.faseTrilha = faseTrilha;

        chat.adicionarMensagemIA("Certo, vamos trabalhar na Fase <strong>" + faseTrilha.getNome() + "</strong> desta trilha.");
        chat.adicionarMensagemIA("Descreva abaixo o que você gostaria de gerar.");

        alterarEtapaAtual(Etapa.REFINAMENTO_PROMPT);
    }

    public void processarMensagemUsuario() {
        if (Etapa.DEFINIR_PERFIL_PEDAGOGICO.equals(etapaAtual)) {
            analisarPerfilPedagogico();
        } else if (Etapa.REFINAMENTO_PROMPT.equals(etapaAtual)) {
            enviarRequisicao();
        }
    }

    public void analisarPerfilPedagogico() {
        this.perfilPedagogico = aiService.enviarPreferenciaAbordagem(unidadeAprendizagem.getTituloUnidadeAprendizagem(), unidadeAprendizagem.getTopicosChave(), preferenciasAbordagem);
        alterarEtapaAtual(Etapa.CONFIRMAR_PERFIL_PEDAGOGICO);

        this.mensagemUsuario = "";

        chat.removerMensagemDigitando();
        chat.adicionarMensagemIA("Muito bem! Esse é o meu entendimento da sua descrição sobre como deseja abordar as trilhas da unidade de aprendizagem:");
        chat.adicionarMensagemIA("<strong>Tom da abordagem:</strong> " + perfilPedagogico.getTomDeVoz());
        chat.adicionarMensagemIA("<strong>Metodologia sugerida:</strong> " + perfilPedagogico.getMetodologiaSugerida());
        chat.adicionarMensagemIA("<strong>Público alvo:</strong> " + perfilPedagogico.getPublicoAlvoInferido());
        chat.adicionarMensagemIA("<strong>Diretriz:</strong> " + perfilPedagogico.getDiretrizMestra());

        if (perfilPedagogico.getElementosObrigatorios() != null && !perfilPedagogico.getElementosObrigatorios().isEmpty()) {
            chat.adicionarMensagemIA("<strong>Elementos obrigatórios:</strong> <br/> " + perfilPedagogico.getElementosObrigatoriosConcatenados());
        }

        if (perfilPedagogico.getRestricoesNegativas() != null && !perfilPedagogico.getRestricoesNegativas().isEmpty()) {
            chat.adicionarMensagemIA("<strong>Restrições:</strong> <br/> " + perfilPedagogico.getRestricoesNegativasConcatenados());
        }

        chat.adicionarMensagemIA("Por favor, confirme se o meu entendimento está correto para continuarmos para a trilha de aprendizagem:");

        chat.adicionarOpcoes(List.of(new OpcaoChat("Confirmar", new OpcaoConfirmarPerfilPedagogico(true)), new OpcaoChat("Enviar novas definições", new OpcaoConfirmarPerfilPedagogico(false))));
    }

    public void enviarMensagemUsuario() {
        chat.adicionarMensagemUsuario(mensagemUsuario);
        chat.adicionarMensagemIADigitando();

        if (Etapa.DEFINIR_PERFIL_PEDAGOGICO.equals(etapaAtual)) {
            this.preferenciasAbordagem.add(mensagemUsuario);
        } else if (Etapa.REFINAMENTO_PROMPT.equals(etapaAtual)) {
            this.requisicao = mensagemUsuario;
        }

        this.mensagemUsuario = "";
    }

    public void enviarRequisicao() {
        validacaoRequisicao = aiService.analisarRequisicao(perfilPedagogico, faseTrilha.getNome(), faseTrilha.getObjetivo(), curso, requisicao);
        ultimoResultado = aiService.gerarPrompt(perfilPedagogico, validacaoRequisicao.getSinteseRequisicao(), curso, unidadeAprendizagem.getTituloUnidadeAprendizagem(), requisicao);

        chat.removerMensagemDigitando();
        chat.adicionarMensagemIA("<strong>Sugestão:</strong> <br/> " + ultimoResultado.getSugestaoPrompt());

        if (ultimoResultado.getCriticaTecnica() != null && !ultimoResultado.getCriticaTecnica().isEmpty()) {
            chat.adicionarMensagemIA("<strong>Crítica:</strong> <br/> " + ultimoResultado.getCriticaTecnica());
        }

        if (ultimoResultado.getPerguntasRefinamento() != null) {
            int numeroPergunta = 1;
            for (var pergunta : ultimoResultado.getPerguntasRefinamento()) {
                chat.adicionarMensagemIA("<strong>Pergunta " + numeroPergunta + ":</strong> <br/> " + pergunta);
                numeroPergunta++;
            }

            chat.adicionarMensagemIA("Deseja continuar refinando o prompt?");
            chat.adicionarOpcoes(List.of(new OpcaoChat("Sim", ContinuarRefinamento.SIM), new OpcaoChat("Não", ContinuarRefinamento.NAO)));
        }
    }

    public void confirmarPerfilPedagogico(boolean confirmar) {
        if (confirmar) {
            alterarEtapaAtual(Etapa.SELECIONAR_FASE_TRILHA);

            chat.adicionarMensagemIA("Agora, selecione a etapa da trilha desejada para começarmos.");
            chat.adicionarOpcoes(getEtapasTrilha().stream().map(etapa -> new OpcaoChat(etapa.getNome(), etapa)).collect(Collectors.toList()));
        } else {
            chat.adicionarMensagemIA("Muito bem, descreva abaixo como você gostaria de abordar a unidade de aprendizagem selecionada.");
            alterarEtapaAtual(Etapa.DEFINIR_PERFIL_PEDAGOGICO);
        }

        this.mensagemUsuario = "";
    }

    public boolean isBloquearMensagemUsuario() {
        return getUltimaMensagem().getTipo().isBloquearMensagemUsuario();
    }

    private MensagemChat getUltimaMensagem() {
        return chat.getMensagens().get(chat.getMensagens().size() - 1);
    }

    public boolean isRenderizarAnalisePlanoEnsino() {
        return etapaAtual.equals(Etapa.ANALISAR_PLANO_ENSINO);
    }

    public Chat getChat() {
        return chat;
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

    public String getMensagemUsuario() {
        return mensagemUsuario;
    }

    public void setMensagemUsuario(String mensagemUsuario) {
        this.mensagemUsuario = mensagemUsuario;
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

    public List<String> getPreferenciasAbordagem() {
        return preferenciasAbordagem;
    }

    public void setPreferenciasAbordagem(List<String> preferenciasAbordagem) {
        this.preferenciasAbordagem = preferenciasAbordagem;
    }
}
