package com.example.vitor.driverforme_motorista.logica;

import android.util.Base64;

/**
 * Created by Vitor on 03/06/2017.
 */

public class Base64Custom {

    public static String codificar(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n||\\r)", "");
    }

    public static String decodificar(String texto){
        return new String (Base64.decode(texto, Base64.DEFAULT));
    }
}
