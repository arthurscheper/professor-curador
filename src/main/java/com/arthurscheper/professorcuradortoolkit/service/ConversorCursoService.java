package com.arthurscheper.professorcuradortoolkit.service;

import com.arthurscheper.professorcuradortoolkit.model.CursoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;

@ApplicationScoped
public class ConversorCursoService {

    public CursoDTO converterJsonParaCurso(String json) {
        json = json.replace("```json", "");

        ObjectMapper mapeadorObjeto = new ObjectMapper();

        try {
            return mapeadorObjeto.readValue(json, CursoDTO.class);
        } catch (IOException excecaoEntradaSaida) {
            excecaoEntradaSaida.printStackTrace();
            return null;
        }
    }

}