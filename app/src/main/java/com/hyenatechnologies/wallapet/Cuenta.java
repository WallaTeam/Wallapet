package com.hyenatechnologies.wallapet;

import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import com.hyenatechnologies.connections.*;

public class Cuenta {

    public String apellidos;

    public int edad;

    public String nombre;

    public int dni;

    public String direccion;

    public String correoElectronico;

    public int telefono;

    public List<ListadoAnuncios> listadoAnuncios = new ArrayList<ListadoAnuncios> ();


    public void modificarAnuncio() {
    }

    public Order solicitarInfo(final Anuncio anuncio) {
        // TODO Auto-generated return
        return null;
    }

    public void filtrarAnuncios() {
    }

    public void borrarAnuncio() {
    }

    public void iniciarSesion() {
    }

    public void crearAnuncio(final Imagen foto) {
    }

    public Order solicitarDatos(final Anuncio anuncio) {
        // TODO Auto-generated return
        return null;
    }

    public ListadoAnuncios filtrarPorTipoAnuncio(final String tipoAnuncio) {
        // TODO Auto-generated return
        return null;
    }

    public ListadoAnuncios filtrarPorPalabraClave(final String palabra) {
        // TODO Auto-generated return
        return null;
    }

    public ListadoAnuncios filtrarPorEspecie(final String especie) {
        // TODO Auto-generated return
        return null;
    }

    public void cerrarSesion() {
    }

    public void registro(final int edad, final String nombre, final String apellidos, final int dni, final int telefono, final String direccion, final String correoElectronico) {
    }

    public void borrarCuenta() {
    }

    public void listarOtrosAnuncios() {
    }

    public void cerrarAnuncio(final Anuncio anuncio) {
    }

    public void fillFormulario(ListActivity ListActivity) {
    }

    public void modificarDatos(final String data) {
    }

}