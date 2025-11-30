package com.arthurscheper.professorcuradortoolkit.service;

import com.arthurscheper.professorcuradortoolkit.domain.Curso;
import dev.langchain4j.data.message.PdfFileContent;
import dev.langchain4j.service.SystemMessage;

public interface AiService {

    @SystemMessage(fromResource = "prompts/etapa-1-analise_plano_ensino.json")
    Curso analisarPlanoEnsino(PdfFileContent planoEnsino);

}
