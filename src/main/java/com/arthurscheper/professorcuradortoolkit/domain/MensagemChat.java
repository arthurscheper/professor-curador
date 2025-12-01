package com.arthurscheper.professorcuradortoolkit.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MensagemChat {

    private final String id = UUID.randomUUID().toString();
    private final TipoMensagemChat tipo;
    private String texto;
    private final List<OpcaoChat> opcoes = new ArrayList<>();

    public MensagemChat(TipoMensagemChat tipo, String texto) {
        this.tipo = tipo;
        this.texto = texto;
    }

    public MensagemChat(TipoMensagemChat tipo) {
        this.tipo = tipo;
    }

    public MensagemChat(TipoMensagemChat tipo, List<OpcaoChat> opcoes) {
        this.tipo = tipo;
        this.opcoes.addAll(opcoes);
    }

    public String getId() {
        return id;
    }

    public TipoMensagemChat getTipo() {
        return tipo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public List<OpcaoChat> getOpcoes() {
        return opcoes;
    }

    public boolean isMensagemTexto() {
        return TipoMensagemChat.IA.equals(tipo) || TipoMensagemChat.IA_DIGITANDO.equals(tipo) || TipoMensagemChat.USUARIO.equals(tipo);
    }

    public boolean isDigitando() {
        return TipoMensagemChat.IA_DIGITANDO.equals(tipo);
    }

    public boolean isUploadArquivo() {
        return TipoMensagemChat.UPLOAD_ARQUIVO.equals(tipo);
    }

    public boolean isSelecionarOpcoes() {
        return TipoMensagemChat.OPCOES.equals(tipo);
    }

}