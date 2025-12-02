package com.arthurscheper.professorcuradortoolkit.domain;

public class ContinuarRefinamento {

    public static ContinuarRefinamento SIM = new ContinuarRefinamento(true);
    public static ContinuarRefinamento NAO = new ContinuarRefinamento(false);

    private boolean continuar;

    public ContinuarRefinamento(boolean continuar) {
        this.continuar = continuar;
    }

    public boolean isContinuar() {
        return continuar;
    }

    public void setContinuar(boolean continuar) {
        this.continuar = continuar;
    }
}
