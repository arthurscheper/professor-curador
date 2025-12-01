package com.arthurscheper.professorcuradortoolkit.domain;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private final List<MensagemChat> mensagens = new ArrayList<>();

    public List<MensagemChat> getMensagens() {
        return mensagens;
    }

    public void adicionarMensagemIA(String texto) {
        mensagens.add(new MensagemChat(TipoMensagemChat.IA, texto));
    }

    public void adicionarMensagemIADigitando() {
        mensagens.add(new MensagemChat(TipoMensagemChat.IA_DIGITANDO));
    }

    public void adicionarMensagemUsuario(String texto) {
        mensagens.add(new MensagemChat(TipoMensagemChat.USUARIO, texto));
    }

    public void adicionarUploadArquivo() {
        mensagens.add(new MensagemChat(TipoMensagemChat.UPLOAD_ARQUIVO));
    }

    public void adicionarOpcoes(List<OpcaoChat> opcoes) {
        mensagens.add(new MensagemChat(TipoMensagemChat.OPCOES, opcoes));
    }

    public void removerMensagemDigitando() {
        mensagens.removeIf(mensagem -> TipoMensagemChat.IA_DIGITANDO.equals(mensagem.getTipo()));
    }

    public void removerUltimaMensagem() {
        mensagens.remove(mensagens.size() - 1);
    }
}
