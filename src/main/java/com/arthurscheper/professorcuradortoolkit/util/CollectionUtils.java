package com.arthurscheper.professorcuradortoolkit.util;

import java.util.List;
import java.util.StringJoiner;

public class CollectionUtils {

    private CollectionUtils() {}

    public static String concatenar(List<String> lista) {
        if (lista == null || lista.isEmpty()) {
            return "";
        }

        if (lista.size() == 1) {
            return lista.get(0);
        }

        if (lista.size() == 2) {
            return lista.get(0) + " e " + lista.get(1);
        }

        String ultimoElemento = lista.get(lista.size() - 1);
        List<String> subLista = lista.subList(0, lista.size() - 1);

        StringJoiner separador = new StringJoiner(", ");
        for (String elemento : subLista) {
            separador.add(elemento);
        }

        return separador + " e " + ultimoElemento;
    }
}
