/*
* Nombre: BusquedaAnuncios.java
* Version: 1.0
* Autor: Luis Pellicer e Ismael Rodriguez
* Fecha: 5-5-2015
* Descripcion: Este fichero implementa la pantalla de búsqueda de anuncios.
*/
package com.hyenatechnologies.wallapet.pantallas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.hyenatechnologies.wallapet.AdaptadoresListas.AdaptadorAnuncios;
import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;
import com.hyenatechnologies.wallapet.objetosDeDatos.Anuncio;

import java.util.ArrayList;
import java.util.List;


public class BusquedaAnunciosFragment extends Fragment {


    private Conexiones conexiones;
    String especie = "";
    String tipo = "";
    String palabras = "";
    Spinner spinnerEspecie;
    Spinner spinnerTipo;
    AdaptadorAnuncios adaptadorAnuncios;
    ListView listViewAnuncios;
    View prompt;

    @Override
    /**
     * Pre: inflater != null && container != null && savedInstanceState != null.
     * Post: Método por defecto en la creaci�n de vista. Encargado de crear todos
     * los elementos de la pantalla e inicializarlos.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //Indica que salgan los iconos de menu
        ((PantallaPrincipalActivity) getActivity()).setTitle("Buscar anuncios");
        conexiones = new Conexiones(this.getActivity()); //Objeto de conexion





        View rootView =
                inflater.inflate(R.layout.fragment_buscar_anuncio,
                        container, false);

        spinnerEspecie = (Spinner) rootView.findViewById(R.id.spinnerEspecie);
        spinnerTipo = (Spinner) rootView.findViewById(R.id.spinnerTipo);

        listViewAnuncios = (ListView) rootView.findViewById(R.id.listaAnuncios);
        List<Anuncio> lista = new ArrayList<>();

        //Buscamos anuncios
        new SearchTask().execute("");

        //Aplicamos el adaptador con lal ista obtenida
        adaptadorAnuncios = new AdaptadorAnuncios(this.getActivity(), 0, lista);
        listViewAnuncios.setAdapter(adaptadorAnuncios);

        /** Click Listener de la lista. Al pulsar un item, nos lleva al fragmento
         * que lo visualiza.
         */
        listViewAnuncios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new VistaAnuncioFragment();
                Bundle args = new Bundle();
                args.putInt("anuncio", (int) id);
                fragment.setArguments(args);

                //el addToBackStack sirve para poder volver atras con boton back
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        fragment).addToBackStack(null).commit();

            }
        });

        /**
         * Selected Listener del spinner de Especie, que sirve para recargar la
         * lista de anuncios cuando se cambia la selecci�n de dicho spinner.
         */
        spinnerEspecie.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id){

                if (position == 0) {
                    especie = "";
                } else {
                    especie = getResources().getStringArray(R.array.array_especies)
                            [position];
                }

                //Buscamos anuncios
                new SearchTask().execute("");

            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        /**
         * Selected Listener del spinner de Tipo de Anuncio, que sirve para recargar
         * la lista de anuncios cuando se cambia la selecci�n de dicho spinner.
         */
        spinnerTipo.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id){

                if (position == 0) {
                    tipo = "";
                } else {
                    tipo=getResources().getStringArray(R.array.array_tipos)
                            [position];
                }

                //Buscamos anuncios
                new SearchTask().execute("");

            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        return rootView;

    }

    @Override
    /**
     * Pre: menu!=null e inflater!=null
     * Post: Infla los objetos del menu para usarse en el action bar.
     */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        inflater.inflate(R.menu.menu_lista, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    /**
     * Pre: item!=null
     * Post:Controla si se ha pulsado una opcion de menu.
     */
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.crear_anuncio:

                //Caso de crear anuncio
                Fragment fragment = new CrearModificarAnuncioFragment();
                FragmentManager fragmentManager = getFragmentManager();
                //el addToBackStack sirve para poder volver atras con boton back
                fragmentManager.beginTransaction().replace(R.id.content_frame,
                        fragment).addToBackStack(null).commit();

                return true;
            case R.id.buscar_anuncio:

                //Caso de buscar anuncio
                LayoutInflater li = LayoutInflater.from(this.getActivity());
               prompt = li.inflate(R.layout.dialog_filtrar_palabras, null);

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(this.getActivity());
                alertDialogBuilder.setView(prompt);
                alertDialogBuilder.setInverseBackgroundForced(true);
                // Mostramos el mensaje del cuadro de dialogo
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("BUSCAR",
                                new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText e = (EditText)
                                        prompt.findViewById(R.id.palabrasClave);

                                //Aqui tenemos que buscar por las palabras clave
                                palabras = e.getText().toString();
                                //Buscamos anuncios
                                    new SearchTask().execute("");

                            }
                        })

                        .setNegativeButton("BORRAR FILTRO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //Se borra el filtro
                                palabras = "";
                                new SearchTask().execute("");
                                dialog.cancel();
                            }
                        });


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Clase que se encarga de la busqueda de anuncios en segundo plano
     */
    private class SearchTask extends AsyncTask<String, Void, List<Anuncio>> {

        private ProgressDialog dialog;

        public SearchTask(){
            super();
            dialog = new ProgressDialog(getActivity());
        }

        /**
         * Pre: cierto
         * Post: muestra el dialogo de buscando....
         */
        protected void onPreExecute() {
            dialog.setCancelable(false);
            dialog.setMessage("Buscando...");
            dialog.show();
        }
        @Override
        /**
         * Pre: ninguno
         * Post: Carga la lista de anuncios que cumplen los criterios
         * "tipo", "especie" y "palabras", y actualiza la UI cuando esta listo.
         */
        protected List<Anuncio> doInBackground(String... urls) {

            //No hacemos caso del url


                List<Anuncio> lista = new ArrayList<>();
                try {
                    lista = conexiones.getAnuncios(
                            tipo, especie, palabras);
                    return lista;

                } catch (ServerException ex) {
                 switch(ex.getCode()){

                     case 405:
                         //No hay sesion iniciada, vamos al login...
                         getActivity().runOnUiThread(new Runnable() {
                             public void run() {
                                 //Cerramos el dialogo en curso
                                 if (dialog.isShowing()) {
                                     dialog.dismiss();
                                 }
                                 //Indicamos mensaje
                                 Toast.makeText(getActivity(),
                                         "Sesion caducada",
                                         Toast.LENGTH_SHORT).show();

                                 //Lanzamos actividad
                                 Intent myIntent = new Intent(getActivity(),
                                         LoginActivity.class);
                                 startActivity(myIntent);
                                 getActivity().finish();

                                 //Matamos asynktask
                                 cancel(true);
                             }
                         });

                         break;

                     default:
                         getActivity().runOnUiThread(new Runnable() {
                             public void run() {
                                 Toast.makeText(getActivity(),
                                         "Error al obtener anuncios",
                                         Toast.LENGTH_SHORT).show();

                             }
                         });

                 }
                    return null;
                }




        }

        @Override
        /**
         * Pre: result!=null
         * Post: actualiza la UI cuando se han cargado los anuncios.
         */
        protected void onPostExecute(List<Anuncio> result) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            adaptadorAnuncios.clear();
            adaptadorAnuncios.addAll(result);
            adaptadorAnuncios.notifyDataSetChanged();

        }
    }
}
