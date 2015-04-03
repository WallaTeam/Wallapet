package com.hyenatechnologies.wallapet.conexiones;

/**
 * Created by ismaro3 on 03/04/2015.
 */
public class ServerException extends Exception {

    private int code;
    public ServerException(int code){
        super();
       this.code = code;
    }

    public int getCode(){
        return code;
    }
}
