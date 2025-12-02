package com.arthurscheper.professorcuradortoolkit.domain;

public class OpcaoUltimoProcesso {

    private UltimoProcesso processo;

    public OpcaoUltimoProcesso(UltimoProcesso processo) {
        this.processo = processo;
    }

    public UltimoProcesso getProcesso() {
        return processo;
    }

    public void setProcesso(UltimoProcesso processo) {
        this.processo = processo;
    }
}