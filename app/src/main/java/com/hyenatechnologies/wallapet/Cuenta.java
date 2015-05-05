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
import com.google.gson.JsonParseException;
public class Cuenta {

    // Atributos de una cuenta
    private String DNI;
    private String nombre;
    private String apellido;
    private String direccion;
    private String email;
    private int telefono;
    private String contrasegna;
    private String usuario;
    /**
     * Pre: Cierto.
     * Post: Devueve la conversion de c en un JSON.
     */
    // Pasa a JSON
    public static String toJson(Cuenta c) {
        Gson gson = new Gson();
        return gson.toJson(c);
    }



    /**
     * Pre: Cierto.
     * Post: Devuelve el objeto Cuenta resultante de transformar json.
     */
    // Obtiene objeto de JSON
    public static Cuenta fromJson(String json)throws JsonParseException {
        Gson gson = new Gson();
        return gson.fromJson(json, Cuenta.class);
    }

    /**
     * Pre: Cierto.
     * Post: getContrasegna() = this.contrasegna
     */
    public String getContrasegna() {
        return contrasegna;
    }

    /**
     * Pre: Cierto.
     * Post: this.contrasegna = contrasegna
     */
    public void setContrasegna(String contrasegna) {
        this.contrasegna = contrasegna;
    }

    /**
     * Pre: Cierto..
     * Post: getDNI() = this.DNI
     */
    public String getDNI() {
        return DNI;
    }

    /**
     * Pre: Cierto.
     * Post: this.DNI = dNI
     */
    public void setDNI(String dNI) {
        DNI = dNI;
    }

    /**
     * Pre: Cierto.
     * Post: getNombre() = nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Pre: Cierto.
     * Post: this.nombre = nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Pre: Cierto.
     * Post: getApellido() = this.apellido
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Pre: Cierto.
     * Post:
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Pre: Cierto.
     * Post: getDireccion() = this.direccion
     */
    public String getDireccion() {
        return direccion;
    }

    public String getUsuario(){ return usuario;}

    /**
     * Pre: Cierto.
     * Post: this.direccion = direccion
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Pre: Cierto.
     * Post: getEmail() = this.email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Pre: Cierto.
     * Post: this.email = email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Pre: Cierto.
     * Post: getTelefono() = this.telefono
     */
    public int getTelefono() {
        return telefono;
    }

    /**
     * Pre: Cierto.
     * Post: this.telefono = telefono
     */
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    /**
     * Pre: Cierto.
     * Post: this.usuario = usuario
     */
    public void setUsuario(String usuario) { this.usuario = usuario;}
}

