/** Copyright (C) 2015 Hyena Technologies
 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.
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

    //Obtiene lista de JSON
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
