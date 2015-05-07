/**
 * Nombre:  ValorSesion.java
 * Version: 1.0
 * Autor:  Ismael Rodriguez
 * Fecha: 6-5-2015
 * Descripcion: Este fichero implementa la clase que guarda de forma estatica
 * la cuenta de la sesion actual.
 */
package com.hyenatechnologies.wallapet.clasesEstaticas;

import com.hyenatechnologies.wallapet.objetosDeDatos.Cuenta;

public class ValorSesion {

    private static Cuenta cuenta;

    /**
     * Pre: cierto
     * Post: devuelve la cuenta actual.
     */
    public static Cuenta getCuenta() {
        return cuenta;
    }

    /**
     *Pre: cierto
     * Post: establece la cuenta actual a "cuenta", que puede ser null.
     */
    public static void setCuenta(Cuenta cuenta) {
        ValorSesion.cuenta = cuenta;
    }


}
