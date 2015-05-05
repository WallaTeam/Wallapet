package com.hyenatechnologies.wallapet;

/**
 * Created by ismaro3 on 05/05/2015.
 */

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by ismaro3 on 05/05/2015.
 */
public class DatosLogin {

    private String mail;

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    private String pass;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    // Pasa a JSON
    public static String toJson(DatosLogin c) {
        Gson gson = new Gson();
        return gson.toJson(c);
    }



    /**
     * Pre: Cierto.
     * Post: Devuelve el objeto Cuenta resultante de transformar json.
     */
    // Obtiene objeto de JSON
    public static DatosLogin fromJson(String json)throws JsonParseException {
        Gson gson = new Gson();
        return gson.fromJson(json, DatosLogin.class);
    }
}
