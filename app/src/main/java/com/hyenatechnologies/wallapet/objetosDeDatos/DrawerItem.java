/**
 * Nombre:  DrawerItem.java
 * Version: 1.0
 * Autor:  Sergio Soro
 * Fecha: 3-4-2015
 * Descripcion: Este fichero implementa una estructura de datos que sirve
 * como elemento de lista del Drawer lateral.
 */
package com.hyenatechnologies.wallapet.objetosDeDatos;

public class DrawerItem {
    private String titulo;
    private int icono;

    /**
     * Pre: title!=null, icon es un numero de recurso de la aplicacion
     * Post:crea un nuevo objeto DrawerItem.
     *
     */
    public DrawerItem(String title, int icon) {
        this.titulo = title;
        this.icono = icon;
    }

    /**
     * Pre: cierto
     * Post: devuelve el titulo.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Pre: titulo!=null
     * Post: establece el titulo.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Pre: cierto
     * Post: devuelve el icono.
     */
    public int getIcono() {
        return icono;
    }

    /**
     * Pre: icono corresponde a un numero de recurso de la aplicacion.
     * Post: establece el icono.
     */
    public void setIcono(int icono) {
        this.icono = icono;
    }
}