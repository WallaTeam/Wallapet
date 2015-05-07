/**
 * Nombre:  NavigationAdapter.java
 * Version: 1.0
 * Autor:  Sergio Soro
 * Fecha: 3-4-2015
 * Descripcion: Este fichero implementa un adaptador para la lista del Drawer.
 */

package com.hyenatechnologies.wallapet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NavigationAdapter extends BaseAdapter {
    private Activity activity;
    ArrayList<Item_objct> arrayitms;

    /**
     * Pre: parametros != null
     * Post: Crea un nuevo objeto NavigationAdapter
     */
    public NavigationAdapter(Activity activity, ArrayList<Item_objct> listarry) {
        super();
        this.activity = activity;
        this.arrayitms = listarry;
    }


    @Override
    /**
     *Pre: position >= 0
     * Post:Retorna objeto Item_objct del array list con la posicion indicada.
     */
    public Object getItem(int position) {
        return arrayitms.get(position);
    }

    /**
     * Pre: cierto
     * Post:Devuelve la cuenta de elementos en la lista.
     */
    public int getCount() {
        return arrayitms.size();
    }

    @Override
    /**
     * Pre: position >= 0
     * Post: devuelve id del elemento de la lista con posicion indicada.
     */
    public long getItemId(int position) {
        return position;
    }

    //Declaramos clase estatica la cual representa a la fila
    public static class Fila {
        TextView titulo_itm;
        ImageView icono;
    }

    /**
     * Pre: parametros no nulos
     * Post: Construye un nuevo objeto View con el Layout correspondiente a la
     * posicion position y lo devuelve. El ultimo parametro es el padre
     * al que la lista va a ser añadida.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        Fila view;
        LayoutInflater inflator = activity.getLayoutInflater();
        if (convertView == null) {
            view = new Fila();

            //Creo objeto item y lo obtengo del array
            Item_objct itm = arrayitms.get(position);
            convertView = inflator.inflate(R.layout.itm, null);

            //Titulo
            view.titulo_itm = (TextView) convertView.findViewById(R.id.title_item);

            //Seteo en el campo titulo el nombre correspondiente obtenido del objeto
            view.titulo_itm.setText(itm.getTitulo());

            //Icono
            view.icono = (ImageView) convertView.findViewById(R.id.icon);

            //Seteo el icono
            view.icono.setImageResource(itm.getIcono());
            convertView.setTag(view);
        } else {
            view = (Fila) convertView.getTag();
        }
        return convertView;
    }
}
