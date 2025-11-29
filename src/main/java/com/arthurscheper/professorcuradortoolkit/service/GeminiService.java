package com.arthurscheper.professorcuradortoolkit.service;

import com.arthurscheper.professorcuradortoolkit.model.CursoDTO;
import dev.langchain4j.data.message.PdfFileContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.primefaces.model.file.UploadedFile;

@ApplicationScoped
public class GeminiService {

    private static final String API_KEY = "AIzaSyCrLmuJi-nXvB2VUyA6oR5I-FJtkLf8jfc";
    private static final String MODEL = "gem/55d090365b06";

    @Inject
    private FileService fileService;

    @Inject
    private ConversorCursoService conversorCursoService;

    @Inject
    private PromptService promptService;

    private ChatModel gemini;

    public CursoDTO enviarArquivo(UploadedFile file) {
        var prompt = promptService.get("analise_plano_ensino");
        var pdf = fileService.converterArquivo(file);

        var retorno = getGemini().chat(UserMessage.from(pdf, TextContent.from(prompt))).aiMessage().text();

        return conversorCursoService.converterJsonParaCurso(retorno);
    }

    public String enviarMensagem(String mensagem) {
        return getGemini().chat(mensagem);
    }

    public ChatModel getGemini() {
        if (gemini == null) {
            gemini = GoogleAiGeminiChatModel.builder()
                    .apiKey(API_KEY)
                    .modelName("gemini-2.5-flash")
                    .build();
        }

        return gemini;
    }
}
