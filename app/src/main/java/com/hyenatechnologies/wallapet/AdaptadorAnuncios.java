package com.hyenatechnologies.wallapet;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ismaro3 on 05/05/2015.
 */
public class AdaptadorAnuncios extends ArrayAdapter<Anuncio> {
    private final Activity actividad;
    private final List<Anuncio> lista;

    public AdaptadorAnuncios(Activity ctx, int resourceId, List<Anuncio> lista){
        super(ctx,resourceId,lista);
        this.actividad = ctx;
        this.lista = lista;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = actividad.getLayoutInflater();
        View view = inflater.inflate(R.layout.elemento_lista, null, true);

        Anuncio anuncio = lista.get(position);
        TextView titulo = (TextView) view.findViewById(R.id.lista_titulo);
        TextView especie = (TextView) view.findViewById(R.id.lista_especie);
        TextView precio = (TextView) view.findViewById(R.id.lista_precio);
        TextView tipo = (TextView) view.findViewById(R.id.lista_tipo);

        titulo.setText(anuncio.getTitulo());
        especie.setText("Especie: " + anuncio.getEspecie());
        precio.setText("" + anuncio.getPrecio() + " €");
        tipo.setText(anuncio.getTipoIntercambio());
        //Modificamos el texto del View construido



        return view;
    }

    public int getCount(){
        return lista.size();
    }

    public Anuncio getItem(int arg0){
        return lista.get(arg0);
    }

    public long getItemId(int position){
        return lista.get(position).getIdAnuncio();
    }

    public void clear(){
        this.lista.clear();
    }

    public void addAll(List<Anuncio> lista2){
        this.lista.addAll(lista2);
    }
}
