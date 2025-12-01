package com.arthurscheper.professorcuradortoolkit.domain;

public class OpcaoConfirmarPerfilPedagogico {

    private boolean confirmar;

    public OpcaoConfirmarPerfilPedagogico(boolean confirmar) {
        this.confirmar = confirmar;
    }

    public boolean isConfirmar() {
        return confirmar;
    }

    public void setConfirmar(boolean confirmar) {
        this.confirmar = confirmar;
    }
}
