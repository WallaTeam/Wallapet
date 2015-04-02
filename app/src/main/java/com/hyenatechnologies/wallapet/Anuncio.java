package com.hyenatechnologies.wallapet;

<<<<<<< HEAD
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
=======
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.List;

>>>>>>> pr/15
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


    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    //Pasa a JSON
    public static String toJson(Anuncio a){
        Gson gson = new Gson();

        return gson.toJson(a);
    }

    public static String listToJson(List<Anuncio> a){
        Gson gson = new Gson();
        return gson.toJson(a);
    }

    //Obtiene objeto de JSON
    public static Anuncio fromJson(String json) throws JsonParseException{
        Gson gson = new Gson();
        return gson.fromJson(json, Anuncio.class);
    }

    public int getIdAnuncio() {
        return idAnuncio;
    }
    public void setIdAnuncio(int idAnuncio) {
        this.idAnuncio = idAnuncio;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getTipoIntercambio() {
        return tipoIntercambio;
    }
    public void setTipoIntercambio(String tipoIntercambio) {
        this.tipoIntercambio = tipoIntercambio;
    }
    public String getEspecie() {
        return especie;
    }
    public void setEspecie(String especie) {
        this.especie = especie;
    }
    public String getRutaImagen() {
        return rutaImagen;
    }
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }


}
