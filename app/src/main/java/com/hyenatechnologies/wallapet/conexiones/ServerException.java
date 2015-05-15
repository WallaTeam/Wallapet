/*
* Nombre: Conexiones.java
* Version: 1.0
* Autor: Ismael Rodriguez
* Fecha: 15/4/2015
* Descripcion: Este fichero implementa la excepcion que sirve para indicar
* error en la peticion.
*/
package com.hyenatechnologies.wallapet.conexiones;

public class ServerException extends Exception {

    private int code; //Corresponde al codigo HTTP de estado de la respuesta.

    public ServerException(int code){
        super();
       this.code = code;
    }

    /**
     *Pre: cierto
     * Post: devuelve el codigo de error
     */
    public int getCode(){
        return code;
    }
}
