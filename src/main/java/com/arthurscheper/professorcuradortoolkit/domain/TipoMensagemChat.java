package com.arthurscheper.professorcuradortoolkit.domain;

public enum TipoMensagemChat {

    IA("mensagemIA", "bolhaMensagemIA"),
    IA_DIGITANDO("mensagemIA", "bolhaMensagemIA"),
    USUARIO("mensagemUsuario", "bolhaMensagemUsuario"),
    UPLOAD_ARQUIVO("mensagemUpload", "bolhaMensagemUpload"),
    OPCOES("", "", true);

    private final String classeMensagem;
    private final String classeBolha;

    public boolean isBloquearMensagemUsuario() {
        return bloquearMensagemUsuario;
    }

    private boolean bloquearMensagemUsuario;

    TipoMensagemChat(String classeMensagem, String classeBolha, boolean bloquearMensagemUsuario) {
        this.classeMensagem = classeMensagem;
        this.classeBolha = classeBolha;
        this.bloquearMensagemUsuario = bloquearMensagemUsuario;
    }

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
