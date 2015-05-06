package com.hyenatechnologies.wallapet;

/**
 * Created by ismaro3 on 06/05/2015.
 */
public class ValorSesion {

    private static Cuenta cuenta;
    private static String JSESSIONID;

    public static Cuenta getCuenta() {
        return cuenta;
    }

    public static void setCuenta(Cuenta cuenta) {
        ValorSesion.cuenta = cuenta;
    }

    public static String getJSESSIONID() {
        return JSESSIONID;
    }

    public static void setJSESSIONID(String JSESSIONID) {
        ValorSesion.JSESSIONID = JSESSIONID;
    }
}
