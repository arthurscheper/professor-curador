package com.arthurscheper.professorcuradortoolkit.service;

import com.arthurscheper.professorcuradortoolkit.domain.Curso;
import com.arthurscheper.professorcuradortoolkit.domain.PerfilPedagogico;
import com.arthurscheper.professorcuradortoolkit.domain.ValidacaoRequisicao;
import dev.langchain4j.data.message.PdfFileContent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

public interface AiService {

    @SystemMessage(fromResource = "prompts/etapa-1-analisar-plano-ensino.json")
    Curso analisarPlanoEnsino(PdfFileContent planoEnsino);

    @SystemMessage(fromResource = "prompts/etapa-2-gerar-perfil-pedagogico.json")
    PerfilPedagogico enviarPreferenciaAbordagem(@V("NOME_UA") String nomeUnidadeAprendizagem, @V("TOPICOS_UA") List<String> topicosUa, @UserMessage String preferenciaAbordagem);

    @SystemMessage(fromResource = "prompts/etapa-3-analise-requisicao.json")
    ValidacaoRequisicao analisarRequisicao(@V("PERFIL_PEDAGOGICO") PerfilPedagogico perfilPedagogico, @V("NOME_ETAPA") String nomeEtapa, @V("OBJETIVO_ETAPA") String objetivoEtapa, @UserMessage String requisicao);
}
