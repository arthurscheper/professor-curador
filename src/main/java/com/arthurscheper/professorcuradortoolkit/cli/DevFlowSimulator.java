package com.arthurscheper.professorcuradortoolkit.cli;

import com.arthurscheper.professorcuradortoolkit.domain.*;
import com.arthurscheper.professorcuradortoolkit.service.AiService;
import dev.langchain4j.data.message.PdfFileContent;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DevFlowSimulator {

    // Configuration Paths
    private static final String PROPERTIES_PATH = "src/main/resources/META-INF/microprofile-config.properties";
    private static final String PDF_PATH = "src/main/resources/ementa/gestao-projetos-e-gestao-manutencao.pdf";
    private static final String EMENTA_JSON_PATH = "src/main/resources/autoMode/ementa.json";

    private ChatModel virtualProfessorModel;
    private AiService aiService;
    private Scanner scanner;
    private boolean isAutoMode;

    public static void main(String[] args) {
        new DevFlowSimulator().run();
    }

    public void run() {
        printHeader();
        try {
            setupEnvironment();
            selectMode();

            Curso curso = analyzePdf();
            if (curso == null) return;

            Selection selection = selectBlockAndUa(curso);

            PerfilPedagogico perfil = definePedagogicalProfile(selection);

            SinteseRequisicao sintese = validateAndAnalyzeRequest(curso, perfil);
            if (sintese == null) return; // Flow interrupted

            executePc3pCycle(perfil, sintese);

            printFooter();

        } catch (Exception e) {
            System.err.println("Erro crítico durante a simulação: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Step Methods ---

    private void setupEnvironment() throws IOException {
        Map<String, String> config = loadConfig();
        String apiKey = config.get("gemini.api.key");
        String modelName = config.get("gemini.model.name");

        if (apiKey == null || modelName == null) {
            throw new RuntimeException("Configuração inválida em microprofile-config.properties. Verifique gemini.api.key e gemini.model.name.");
        }

        // Main Chat Model
        // State
        ChatModel appChatModel = GoogleAiGeminiChatModel.builder()
                                                        .apiKey(apiKey)
                                                        .modelName(modelName)
                                                        .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                                                        .logRequests(false)
                                                        .logResponses(false)
                                                        .build();

        // Virtual Professor Model
        this.virtualProfessorModel = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .build();

        this.aiService = AiServices.builder(AiService.class)
                .chatModel(appChatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .build();

        this.scanner = new Scanner(System.in);
    }

    private void selectMode() {
        System.out.println("\nSelecione o modo de operação:");
        System.out.println("1 - MANUAL (Você digita as respostas)");
        System.out.println("2 - AUTO (IA simula o professor)");
        System.out.print("> ");
        
        String modeInput = scanner.nextLine();
        this.isAutoMode = "2".equals(modeInput.trim());
    }

    private Curso analyzePdf() throws IOException {
        System.out.println("\n[ETAPA 1] Lendo e Analisando Plano de Ensino...");
        Curso curso;

        if (isAutoMode) {
            curso = loadCursoFromJson(EMENTA_JSON_PATH);
            System.out.println(">> Modo Auto: Plano de ensino carregado do arquivo JSON pré-processado.");
        } else {
            System.out.println("Deseja usar o plano de ensino pré-processado (ementa.json) ou enviar o PDF para a IA?");
            System.out.println("1 - Usar JSON pré-processado (mais rápido)");
            System.out.println("2 - Enviar PDF para IA (mais lento, processamento real)");
            System.out.print("> ");
            String choice = scanner.nextLine().trim();

            if ("1".equals(choice)) {
                curso = loadCursoFromJson(EMENTA_JSON_PATH);
                System.out.println(">> Plano de ensino carregado do arquivo JSON pré-processado.");
            } else {
                curso = processPdfWithAi();
                System.out.println(">> Plano de ensino processado pela IA.");
            }
        }
        printCurso(curso);
        return curso;
    }

    private Curso loadCursoFromJson(String jsonPath) throws IOException {
        Path jsonFilePath = Paths.get(jsonPath);
        if (!Files.exists(jsonFilePath)) {
            throw new RuntimeException("Arquivo JSON de ementa não encontrado: " + jsonPath);
        }
        String jsonContent = Files.readString(jsonFilePath);
        ObjectMapper objectMapper = new ObjectMapper(); // Assuming Jackson is available
        try {
            return objectMapper.readValue(jsonContent, Curso.class);
        } catch (Exception e) {
            throw new IOException("Erro ao deserializar JSON da ementa: " + e.getMessage(), e);
        }
    }

    private Curso processPdfWithAi() throws IOException {
        Path pdfPath = Paths.get(PDF_PATH);
        if (!Files.exists(pdfPath)) {
            throw new RuntimeException("Arquivo PDF não encontrado: " + PDF_PATH);
        }
        byte[] pdfBytes = Files.readAllBytes(pdfPath);
        String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);
        PdfFileContent pdfContent = PdfFileContent.from(base64Pdf, "application/pdf");
        return aiService.analisarPlanoEnsino(pdfContent);
    }

    private Selection selectBlockAndUa(Curso curso) {
        if (curso.getEstrutura() == null || curso.getEstrutura().isEmpty()) {
            throw new RuntimeException("Nenhum bloco encontrado no plano de ensino.");
        }

        System.out.println("\n[SELEÇÃO] Selecione um Bloco:");
        for (int i = 0; i < curso.getEstrutura().size(); i++) {
            System.out.println(i + " - " + curso.getEstrutura().get(i).getTituloBloco());
        }
        System.out.print("> ");
        String blocoInput = scanner.nextLine();
        int blocoIndex = Integer.parseInt(blocoInput.trim());
        Bloco blocoSelecionado = curso.getEstrutura().get(blocoIndex);

        System.out.println("\n[SELEÇÃO] Selecione uma UA:");
        for (int i = 0; i < blocoSelecionado.getUnidadesAprendizagem().size(); i++) {
            System.out.println(i + " - " + blocoSelecionado.getUnidadesAprendizagem().get(i).getTituloUnidadeAprendizagem());
        }
        System.out.print("> ");
        String uaInput = scanner.nextLine();
        int uaIndex = Integer.parseInt(uaInput.trim());
        UnidadeAprendizagem uaSelecionada = blocoSelecionado.getUnidadesAprendizagem().get(uaIndex);

        System.out.println("\n[SELEÇÃO] UA Selecionada: " + uaSelecionada.getTituloUnidadeAprendizagem());

        return new Selection(blocoSelecionado, uaSelecionada);
    }

    private PerfilPedagogico definePedagogicalProfile(Selection selection) {
        System.out.println("\n[ETAPA 2] Definindo Perfil Pedagógico...");
        String userPreference;
        if (isAutoMode) {
            String prompt = "Você é um professor de Engenharia. Dado o tópico '" + selection.ua.getTituloUnidadeAprendizagem() + "', descreva em 2 frases como você gostaria de ensinar isso. Seja criativo e específico (ex: estudos de caso, PBL).";
            userPreference = askVirtualProfessor(prompt);
            System.out.println(">> Professor Virtual diz: " + userPreference);
        } else {
            System.out.println(">> Como você gostaria de abordar este conteúdo? (ex: 'Quero focar em prática...')");
            System.out.print("> ");
            userPreference = scanner.nextLine();
        }

        List<String> prefList = new ArrayList<>();
        prefList.add(userPreference);

        PerfilPedagogico perfil = aiService.enviarPreferenciaAbordagem(selection.ua.getTituloUnidadeAprendizagem(), selection.ua.getTopicosChave(), prefList);
        System.out.println(">> Perfil Gerado (Diretriz Mestra): " + perfil.getDiretrizMestra());
        return perfil;
    }

    private SinteseRequisicao validateAndAnalyzeRequest(Curso curso, PerfilPedagogico perfil) {
        System.out.println("\n[ETAPA 3] Validando Requisição...");

        FaseTrilha faseTrilha;
        if (isAutoMode) {
            FaseTrilha[] fases = FaseTrilha.values();
            faseTrilha = fases[new Random().nextInt(fases.length)];
            System.out.println(">> Modo Auto: Fase selecionada aleatoriamente: " + faseTrilha.getNome());
        } else {
            System.out.println("Selecione a Fase da Trilha:");
            for (FaseTrilha f : FaseTrilha.values()) {
                System.out.println(f.getId() + " - " + f.getNome() + " (" + f.getObjetivo() + ")");
            }
            System.out.print("> ");
            try {
                int faseId = Integer.parseInt(scanner.nextLine());
                faseTrilha = FaseTrilha.fromId(faseId);
            } catch (Exception e) {
                System.out.println("Seleção inválida. Usando padrão: " + FaseTrilha.AVALIACAO_QUIZ.getNome());
                faseTrilha = FaseTrilha.AVALIACAO_QUIZ;
            }
        }

        String userRequest;
        if (isAutoMode) {
            String prompt = "Você é um professor universitário. A fase atual da aula é '" + faseTrilha.getNome() +
                    "' com o objetivo: '" + faseTrilha.getObjetivo() + "'. " +
                    "Faça uma requisição curta (1 frase) ao assistente de IA para gerar um material pertinente a esta fase.";
            userRequest = askVirtualProfessor(prompt);
            System.out.println(">> Professor Virtual pede: " + userRequest);
        } else {
            System.out.println(">> O que você deseja criar para a fase '" + faseTrilha.getNome() + "'? (ex: 'Crie um quiz de 5 perguntas')");
            System.out.print("> ");
            userRequest = scanner.nextLine();
        }

        ValidacaoRequisicao validacao = aiService.analisarRequisicao(perfil, faseTrilha.getNome(), faseTrilha.getObjetivo(), curso, userRequest);
        System.out.println(">> Status: " + validacao.getAdequacaoEtapa());
        System.out.println(">> Justificativa: " + validacao.getJustificativaAdequacao());

        if (ValidacaoRequisicao.StatusAdequacao.NAO_ADEQUADO.equals(validacao.getAdequacaoEtapa())) {
            System.out.println("!!! Fluxo interrompido: Pedido Inadequado. !!!");
            return null;
        }
        return validacao.getSinteseRequisicao();
    }

    private void executePc3pCycle(PerfilPedagogico perfil, SinteseRequisicao sintese) {
        System.out.println("\n[ETAPA 4] Executando Ciclo PC3P...");

        ResultadoRefinamentoPrompt pc3pResult;
        int iteration = 1;
        int autoModeCount = 0;

        while (true) {
            System.out.println("\n--- Iteração " + iteration + " ---");
            String prompt = iteration == 1 ? "Gere o prompt inicial." : "Continue refinando o prompt.";
            pc3pResult = aiService.gerarPrompt(perfil, sintese, prompt);
            printPC3P(pc3pResult);

            if (isAutoMode) {
                autoModeCount++;
                if (autoModeCount >= 2 || pc3pResult.getPerguntasRefinamento() == null || pc3pResult.getPerguntasRefinamento()
                                                                                                    .isEmpty()) {
                    break;
                }

                System.out.println("\n--- Refinamento Automático ---");
                StringBuilder answers = new StringBuilder();
                int qCount = 1;
                for (String q : pc3pResult.getPerguntasRefinamento()) {
                    String ans = askVirtualProfessor("Responda a esta pergunta do especialista em prompt de forma concisa: " + q);
                    answers.append(qCount++).append(". ").append(ans).append(" ");
                }
                System.out.println(">> Respostas do Professor Virtual: " + answers.toString());
            } else {
                System.out.print("\nDeseja continuar refinando? (Y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if (!response.isEmpty() && !response.equals("y")) {
                    break;
                }

                if (pc3pResult.getPerguntasRefinamento() != null && !pc3pResult.getPerguntasRefinamento().isEmpty()) {
                    StringBuilder answers = new StringBuilder();
                    int qCount = 1;
                    System.out.println("\nResponda às perguntas de refinamento:");
                    for (String q : pc3pResult.getPerguntasRefinamento()) {
                        System.out.println(q);
                        System.out.print("> ");
                        String ans = scanner.nextLine();
                        answers.append(qCount++).append(". ").append(ans).append(" ");
                    }
                    pc3pResult = aiService.gerarPrompt(perfil, sintese,
                                                       "Aqui estão as respostas para suas perguntas: " + answers.toString());
                    printPC3P(pc3pResult);
                }
            }
            iteration++;
        }
    }

    // --- Helpers ---

    private String askVirtualProfessor(String prompt) {
        return virtualProfessorModel.chat(prompt);
    }

    private Map<String, String> loadConfig() throws IOException {
        Map<String, String> config = new HashMap<>();
        List<String> lines = Files.readAllLines(Paths.get(PROPERTIES_PATH));
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            if (line.contains("=")) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    config.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
        return config;
    }

    private void printHeader() {
        System.out.println("============================================================");
        System.out.println("   PROFESSOR CURADOR - DEV FLOW SIMULATOR (CLI)   ");
        System.out.println("============================================================");
    }

    private void printFooter() {
        System.out.println("\n============================================================");
        System.out.println("   SIMULAÇÃO CONCLUÍDA COM SUCESSO   ");
        System.out.println("============================================================");
    }

    private void printCurso(Curso curso) {
        System.out.println(">> Curso Estruturado:");
        if (curso.getMetadadosCurso() != null) {
            System.out.println("   [DISCIPLINA] " + curso.getMetadadosCurso().getNomeDisciplina());
            if (curso.getMetadadosCurso().getBibliografia() != null) {
                System.out.println("   [BIBLIOGRAFIA] " + curso.getMetadadosCurso().getBibliografia().size() + " itens encontrados.");
            }
        }

        if (curso.getEstrutura() != null) {
            for (Bloco b : curso.getEstrutura()) {
                System.out.println("   [BLOCO] " + b.getTituloBloco());
                if (b.getUnidadesAprendizagem() != null) {
                    for (UnidadeAprendizagem ua : b.getUnidadesAprendizagem()) {
                        System.out.println("       - (UA) " + ua.getTituloUnidadeAprendizagem());
                    }
                }
            }
        }
    }

    private void printPC3P(ResultadoRefinamentoPrompt res) {
        System.out.println("   [PROMPT GERADO]: " + res.getSugestaoPrompt());
        System.out.println("   [CRÍTICA]: " + res.getCriticaTecnica());
        System.out.println("   [PERGUNTAS]:");
        if (res.getPerguntasRefinamento() != null) {
            res.getPerguntasRefinamento().forEach(p -> System.out.println("       ? " + p));
        }
    }

    // Inner class for selection context
    private static class Selection {
        final Bloco bloco;
        final UnidadeAprendizagem ua;

        public Selection(Bloco bloco, UnidadeAprendizagem ua) {
            this.bloco = bloco;
            this.ua = ua;
        }
    }

}