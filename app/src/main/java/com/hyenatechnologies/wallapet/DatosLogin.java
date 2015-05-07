/**
 * Nombre:  DatosLogin.java
 * Version: 1.0
 * Autor:  Ismael Rodriguez
 * Fecha: 5-5-2015
 * Descripcion: Este fichero implementa una estructura de datos que sirve
 * para enviar datos de login.
 */

package com.hyenatechnologies.wallapet;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class DatosLogin {

    private String mail;
    private String pass;

    /**
     *Pre: cierto
     * Post: Devuelve pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * Pre: pass!=null
     * Post: establece el pass
     */
    public void setPass(String pass) {
        this.pass = pass;
    }


    /**
     * Pre: cierto
     * Post: devuelve mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * Pre: mail!=null
     * Post: Establece el mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
    *Pre: c!=null
    *Post: Devuelve la representacion JSON de c
     */
    public static String toJson(DatosLogin c) {
        Gson gson = new Gson();
        return gson.toJson(c);
    }



    /**
     * Pre: json es un objeto DatosLogin JSON valido
     * Post: Devuelve el objeto DatosLogin resultante de transformar json.
     */
    public static DatosLogin fromJson(String json)throws JsonParseException {
        Gson gson = new Gson();
        return gson.fromJson(json, DatosLogin.class);
    }
}
