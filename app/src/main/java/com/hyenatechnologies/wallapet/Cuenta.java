package com.hyenatechnologies.wallapet;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class Cuenta {

    private String DNI;
    private String nombre;
    private String apellido;
    private String direccion;
    private String email;
    private int telefono;
    private String contrasegna;

    // Pasa a JSON
    public static String toJson(Cuenta c) {
        Gson gson = new Gson();
        return gson.toJson(c);
    }

    // Obtiene objeto de JSON
    public static Cuenta fromJson(String json)throws JsonParseException {
        Gson gson = new Gson();
        return gson.fromJson(json, Cuenta.class);
    }

    public String getContrasegna() {
        return contrasegna;
    }

    public void setContrasegna(String contrasegna) {
        this.contrasegna = contrasegna;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String dNI) {
        DNI = dNI;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

}

