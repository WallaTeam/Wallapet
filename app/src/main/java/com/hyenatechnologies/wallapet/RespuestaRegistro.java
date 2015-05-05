package com.hyenatechnologies.wallapet;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by ismaro3 on 05/05/2015.
 */
public class RespuestaRegistro {

    public String getRespuestaRegistro() {
        return RespuestaRegistro;
    }

    public void setRespuestaRegistro(String respuestaRegistro) {
        RespuestaRegistro = respuestaRegistro;
    }

    private String RespuestaRegistro;

    // Pasa a JSON
    public static String toJson( RespuestaRegistro c) {
        Gson gson = new Gson();
        return gson.toJson(c);
    }



    /**
     * Pre: Cierto.
     * Post: Devuelve el objeto Cuenta resultante de transformar json.
     */
    // Obtiene objeto de JSON
    public static RespuestaRegistro fromJson(String json)throws JsonParseException {
        Gson gson = new Gson();
        return gson.fromJson(json,  RespuestaRegistro.class);
    }

}
