/**
 * Nombre:  AdaptadorAnuncios.java
 * Version: 1.0
 * Autor: Ismael Rodriguez
 * Fecha: 6-4-2015
 * Descripcion: Este fichero implementa una adaptador que permite poblar
 * la lista de busqueda de anuncios de forma personalizada.
 */
package com.hyenatechnologies.wallapet.AdaptadoresListas;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.objetosDeDatos.Anuncio;

import java.util.List;


public class AdaptadorAnuncios extends ArrayAdapter<Anuncio> {
    private final Activity actividad;
    private final List<Anuncio> lista;

    /**
     * Pre: cierto
     * Post: Crea el adaptador.
     */
    public AdaptadorAnuncios(Activity ctx, int resourceId, List<Anuncio> lista){
        super(ctx,resourceId,lista);
        this.actividad = ctx;
        this.lista = lista;
    }

    /**
     *Pre: parametros no nulos
     * Post: Construye un nuevo objeto View con el Layout correspondiente a la
     * posicion position y lo devuelve. El ultimo parametro es el padre
     * al que la lista va a ser anadida.
     */
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = actividad.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_lista_anuncios, null, true);

        Anuncio anuncio = lista.get(position);
        TextView titulo = (TextView) view.findViewById(R.id.lista_titulo);
        TextView especie = (TextView) view.findViewById(R.id.lista_especie);
        TextView precio = (TextView) view.findViewById(R.id.lista_precio);

        ImageView image = (ImageView) view.findViewById(R.id.thumbnail);
        titulo.setText(anuncio.getTitulo());
        especie.setText("Especie: " + anuncio.getEspecie());

        if(anuncio.getTipoIntercambio().contains("Venta")){
            precio.setText("Venta - " + anuncio.getPrecio() + " Euros");
            image.setImageResource(R.drawable.sell);
        }
        else{
            precio.setText("Adopcion");
            image.setImageResource(R.drawable.deal);
        }


        return view;
    }

    /**
     * Pre: cierto
     * Post: Devuelve el numero de elementos almacenados
     */
    public int getCount(){
        return lista.size();
    }

    /**
     * Pre: arg0 >= 0
     * Post: devuelve el objeto con indice indicado.
     */
    public Anuncio getItem(int arg0){
        return lista.get(arg0);
    }

    /**
     * Pre: position >= 0
     * Post: devuelve el id del objeto con indice indicado.
     */
    public long getItemId(int position){
        return lista.get(position).getIdAnuncio();
    }

    /**
     * Pre: cierto
     * Post: limpia la lista.
     */
    public void clear(){
        this.lista.clear();
    }

    /**
     * Pre: lista2!=null
     * Post: a�ade a la lista actual todos los elementos de la lista indicada.
     */
    public void addAll(List<Anuncio> lista2){
        this.lista.addAll(lista2);
    }
}
