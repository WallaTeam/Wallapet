/**
 * Nombre:  Anuncio.java
 * Version: 1.2
 * Autor: Raul Piraces, Ismael Rodriguez
 * Fecha: 5-4-2015
 * Descripcion: Este fichero implementa la representacion de un Anuncio.
 */

package com.hyenatechnologies.wallapet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class Anuncio {

    private int idAnuncio;
    private String email;
    private String estado;
    private String descripcion;
    private String tipoIntercambio;
    private String especie;
    private String rutaImagen;
    private double precio;
    private String titulo;

    /*
    * Pre: a!=null
    *Post: devuelve una cadena con el JSON del anuncio
     */
    public static String toJson(Anuncio a){
        Gson gson = new Gson();

        return gson.toJson(a);
    }

    /**
     * Pre: a!=null
     * Post: Devuelve una cadena con el JSON de la lista de anuncios
     */
    public static String listToJson(List<Anuncio> a){
        Gson gson = new Gson();
        return gson.toJson(a);
    }

    /*
    *Pre: json!=null y json representa un Anuncio en JSON
    *Post: devuelve el objeto Anuncio correspondiente
     */
    public static Anuncio fromJson(String json) throws JsonParseException{
        Gson gson = new Gson();
        return gson.fromJson(json, Anuncio.class);
    }

    /*
    *Pre: json!=null y json representa una lista de  Anuncio en JSON
    *Post: devuelve el objeto List<Anuncio> correspondiente
     */
    public static List<Anuncio> fromJsonList(String json) throws JsonParseException{
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonArray anunciosArray =  jsonParser.parse(json).getAsJsonArray();
        List<Anuncio> anuncios = new ArrayList<>();
        for (JsonElement anuncio : anunciosArray){
            anuncios.add(gson.fromJson(anuncio, Anuncio.class));
        }
        return anuncios;
    }

    /**
     * Pre: cierto
     * Post: devuelve el precio
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Pre: cierto
     * Post: cambia el precio del anuncio
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Pre: cierto
     * Post: devuelve el titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Pre: titulo != null
     * Post: cambia el titulo del anuncio
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    /**
     * Pre: cierto
     * Post: devuelve el id de anuncio
     */
    public int getIdAnuncio() {
        return idAnuncio;
    }

    /**
     * Pre: cierto
     * Post: cambia el id del anuncio
     */
    public void setIdAnuncio(int idAnuncio) {
        this.idAnuncio = idAnuncio;
    }

    /**
     * Pre: cierto
     * Post: devuelve el email
     */
    public String getEmail() {
        return email;
    }


    /**
     * Pre: email != null
     * Post: cambia el email del anuncio
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Pre: cierto
     * Post: devuelve el estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Pre: estado != null
     * Post: cambia el estado del anuncio
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Pre: cierto
     * Post: devuelve la descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Pre: descripcion != null
     * Post: cambia la descripcion del anuncio
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Pre: cierto
     * Post: devuelve el tipo de intercambio
     */
    public String getTipoIntercambio() {
        return tipoIntercambio;
    }

    /**
     * Pre: tipoIntercambio != null
     * Post: cambia el tipo del anuncio
     */
    public void setTipoIntercambio(String tipoIntercambio) {
        this.tipoIntercambio = tipoIntercambio;
    }

    /**
     * Pre: cierto
     * Post: devuelve la especie
     */
    public String getEspecie() {
        return especie;
    }

    /**
     * Pre: especie != null
     * Post: cambia la especie del anuncio
     */
    public void setEspecie(String especie) {
        this.especie = especie;
    }

    /**
     * Pre: cierto
     * Post: devuelve la ruta de imagen
     */
    public String getRutaImagen() {
        return rutaImagen;
    }

    /**
     * Pre: rutaImagen != null
     * Post: cambia la ruta de imagen del anuncio
     */
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }


}
