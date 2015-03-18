package com.hyenatechnologies.wallapet;

import java.util.ArrayList;
import java.util.List;
import android.widget.Button;

public class Anuncio {
    private String estado;

    public String tipoIntercambio;

    public String tipoAnimal;

    public String descripcion;

    public List<Animal> animal = new ArrayList<Animal> ();

    public List<Order> order = new ArrayList<Order> ();

    public List<Imagen> imagen = new ArrayList<Imagen> ();

    public void cambiarEstado(final String nuevoEstado) {
    }

    public void modificar() {
    }

    public void getInfo() {
    }

    public Order getOrder() {
        // TODO Auto-generated return
        return null;
    }

    public void actualizar() {
    }

}