package com.hyenatechnologies.wallapet.pantallas;

import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by David
        */
public class ValidacionRegistro {

    //Expresiones regulares que comprobarán los campos
    private static final String EMAIL_REGEX= "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)" +
            "*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX= "\\d{9}";
    private static final String PASS_REGEX= "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9].*[0-9])" +
            "(?=.*[^a-zA-Z0-9]).{8,}";
    private static final String DNI_REGEX= "\\d{8}[A-Za-z]";

    //Mensajes de error
    private static final String REQUERIDO_MENS = "Campo requerido";
    private static final String EMAIL_MENS = "Email invalido";
    private static final String PHONE_MENS = "Telefono invalido";
    private static final String DNI_MENS = "DNI invalido";
    private static final String PASS_MENS = "Contraseña debil";

    /**
     * Pre: Cierto
     * Post: Comprueba la validez del email
     */
    public static boolean validoEmailAddress(EditText editText, boolean requerido){
        return esValido(editText, EMAIL_REGEX, EMAIL_MENS, requerido);
    }

    /**
     * Pre: Cierto
     * Post: Comprueba la validez del telefono
     */
    public static boolean validoTelefono(EditText editText, boolean requerido){
        return esValido(editText, PHONE_REGEX, PHONE_MENS, requerido);
    }

    /**
     * Pre: Cierto
     * Post: Comprueba la validez del DNI
     */
    public static boolean validoDNI(EditText editText, boolean requerido){
        return esValido(editText, DNI_REGEX, DNI_MENS, requerido);
    }

    /**
     * Pre: Cierto
     * Post: Comprueba la validez de la contraseña
     */
    public static boolean validoPASS(EditText editText, boolean requerido){
        return esValido(editText, PASS_REGEX, PASS_MENS, requerido);
    }

    /**
     * Pre: Cierto
     * Post: Devuelve true si el inputField es valido
     */
    public static boolean esValido(EditText editText,String regex,String errMsg,
                                   boolean requerido){
        String text =editText.getText().toString().trim();
        //Eliminar posibles errores asociados
         editText.setError(null);

        //Se requiere texto y esta en blanco, devolver false
        if(requerido && !hasText(editText)) return false;

        //No se corresponde con expresion regular, devolver false
        if (requerido && !Pattern.matches(regex,text)){
            editText.setError(errMsg);
            return false;
        };

        //Sitodo ha ido bien
        return true;

    }

    /**
     * Pre: Cierto
     * Post: Comprobar si el inputFiel tiene rexto o no
     *       Devuelve true si contiene texto, sino false
     */
    public static boolean hasText(EditText editText){

        String text = editText.getText().toString().trim();
        editText.setError(null);

        //Longitud a 0 indica que no hay texto
        if(text.length()==0){
            editText.setError(REQUERIDO_MENS);
            return false;
        }

        //Si tenia texto
        return true;
    }
}