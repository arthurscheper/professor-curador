package com.arthurscheper.professorcuradortoolkit.domain;

public enum TipoMensagemChat {

    IA("mensagemIA", "bolhaMensagemIA"),
    IA_DIGITANDO("mensagemIA", "bolhaMensagemIA"),
    USUARIO("mensagemUsuario", "bolhaMensagemUsuario"),
    UPLOAD_ARQUIVO("mensagemUpload", "bolhaMensagemUpload"),
    OPCOES("", "");

    private final String classeMensagem;
    private final String classeBolha;

    TipoMensagemChat(String classeMensagem, String classeBolha) {
        this.classeMensagem = classeMensagem;
        this.classeBolha = classeBolha;
    }

    public String getClasseMensagem() {
        return classeMensagem;
    }

    public String getClasseBolha() {
        return classeBolha;
    }
}
