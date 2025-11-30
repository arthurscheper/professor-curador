package com.arthurscheper.professorcuradortoolkit.domain;

import com.arthurscheper.professorcuradortoolkit.util.CollectionUtils;
import dev.langchain4j.model.output.structured.Description;

import java.util.List;

public class PerfilPedagogico {

    @Description("Tom de voz inferido. Ex: Formal e Técnico, Descontraído e Prático")
    private String tomDeVoz;

    @Description("Metodologia de ensino implícita sugerida pelo professor")
    private String metodologiaSugerida;

    @Description("O público-alvo inferido para esta unidade de aprendizagem")
    private String publicoAlvoInferido;

    @Description("Lista de elementos que o professor indicou como obrigatórios")
    private List<String> elementosObrigatorios;

    @Description("Lista de restrições ou coisas que o professor explicitamente NÃO quer")
    private List<String> restricoesNegativas;

    @Description("Um resumo conciso de como a IA deve se comportar nesta UA. Ex: 'Atue como um mentor sênior focado em aplicações práticas, evitando jargões excessivos.'")
    private String diretrizMestra;

    public String getTomDeVoz() {
        return tomDeVoz;
    }

    public void setTomDeVoz(String tomDeVoz) {
        this.tomDeVoz = tomDeVoz;
    }

    public String getMetodologiaSugerida() {
        return metodologiaSugerida;
    }

    public void setMetodologiaSugerida(String metodologiaSugerida) {
        this.metodologiaSugerida = metodologiaSugerida;
    }

    public String getPublicoAlvoInferido() {
        return publicoAlvoInferido;
    }

    public void setPublicoAlvoInferido(String publicoAlvoInferido) {
        this.publicoAlvoInferido = publicoAlvoInferido;
    }

    public List<String> getElementosObrigatorios() {
        return elementosObrigatorios;
    }

    public void setElementosObrigatorios(List<String> elementosObrigatorios) {
        this.elementosObrigatorios = elementosObrigatorios;
    }

    public List<String> getRestricoesNegativas() {
        return restricoesNegativas;
    }

    public void setRestricoesNegativas(List<String> restricoesNegativas) {
        this.restricoesNegativas = restricoesNegativas;
    }

    public String getDiretrizMestra() {
        return diretrizMestra;
    }

    public void setDiretrizMestra(String diretrizMestra) {
        this.diretrizMestra = diretrizMestra;
    }

    public String getElementosObrigatoriosConcatenados() {
        return CollectionUtils.concatenar(elementosObrigatorios);
    }

    public String getRestricoesNegativasConcatenados() {
        return CollectionUtils.concatenar(restricoesNegativas);
    }
}
