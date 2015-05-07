/**
 * Nombre:  RespuestaRegistro.java
 * Version: 1.0
 * Autor:  Ismael Rodriguez
 * Fecha: 5-5-2015
 * Descripcion: Este fichero implementa una estructura de datos
 * para obtener la respuesta del procedimiento de registro.
 */
package com.hyenatechnologies.wallapet.objetosDeDatos;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;


public class RespuestaRegistro {

    private String RespuestaRegistro;

    /**
     *Pre: cierto
     * Post: devuelve la respuesta de registro.
     */
    public String getRespuestaRegistro() {
        return RespuestaRegistro;
    }

    /**
     * Pre: respuestaRegistro != null
     * Post: establece la respuesta de registro.
     */
    public void setRespuestaRegistro(String respuestaRegistro) {
        RespuestaRegistro = respuestaRegistro;
    }


    /**
     * Pre: c!=null
     * Post: devuelve la version JSON del objeto.
     */
    public static String toJson( RespuestaRegistro c) {
        Gson gson = new Gson();
        return gson.toJson(c);
    }



    /**
     * Pre: Cierto y json corresponde a un objeto RespuestaRegistro serializado.
     * Post: Devuelve el objeto RespuestaRegistro resultante de transformar json.
     */
    public static RespuestaRegistro fromJson(String json)throws JsonParseException {
        Gson gson = new Gson();
        return gson.fromJson(json,  RespuestaRegistro.class);
    }

}
