package com.hyenatechnologies.wallapet.pantallas;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
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

import com.hyenatechnologies.wallapet.AdaptadorAnuncios;
import com.hyenatechnologies.wallapet.Anuncio;
import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 18/04/2015.
 */
public class BusquedaAnunciosNew extends Fragment {


    private Conexiones conexiones;

    String especie = "";
    String tipo = "";
    String palabras = "";
    Spinner spinnerEspecie;
    Spinner spinnerTipo;
    AdaptadorAnuncios miAdaptador;
    ListView yourListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        conexiones = new Conexiones(this.getActivity());
        View rootView = inflater.inflate(R.layout.activity_buscar_anuncio_new, container, false);
        spinnerEspecie = (Spinner) rootView.findViewById(R.id.spinnerEspecie);
        spinnerTipo = (Spinner) rootView.findViewById(R.id.spinnerTipo);
        //Rellenamos la lista
        yourListView = (ListView) rootView.findViewById(R.id.listaAnuncios);
        List<Anuncio> lista = new ArrayList<>();
        try {
            lista = conexiones.getAnuncios("", "", "");
        } catch (ServerException ex) {
            Toast.makeText(this.getActivity().getApplicationContext(), "Error obteniendo la lista",
                    Toast.LENGTH_LONG).show();
        }

        miAdaptador = new AdaptadorAnuncios(this.getActivity(), 0, lista);
        yourListView.setAdapter(miAdaptador);

        yourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new VistaAnuncioFragment();
                Bundle args = new Bundle();
                args.putInt("anuncio", (int) id);
                fragment.setArguments(args);
                //el addToBackStack sirve para poder volver atras con boton back
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();


            }
        });

        spinnerEspecie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position == 0) {
                    especie = "";
                } else {
                    especie = getResources().getStringArray(R.array.array_especies)[position];
                }
                List<Anuncio> lista = new ArrayList<>();
                try {
                    lista = conexiones.getAnuncios(tipo, especie, palabras);

                } catch (ServerException ex) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error obteniendo la lista",
                            Toast.LENGTH_LONG).show();
                }

                miAdaptador.clear();
                miAdaptador.addAll(lista);
                miAdaptador.notifyDataSetChanged();

            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position == 0) {
                    tipo = "";
                } else {
                    tipo = getResources().getStringArray(R.array.array_tipos)[position];
                }
                List<Anuncio> lista = new ArrayList<>();
                try {
                    lista = conexiones.getAnuncios(tipo, especie, palabras);

                } catch (ServerException ex) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error obteniendo la lista",
                            Toast.LENGTH_LONG).show();
                }

                miAdaptador.clear();
                miAdaptador.addAll(lista);
                miAdaptador.notifyDataSetChanged();

            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar

        inflater.inflate(R.menu.menu_lista, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.crear_anuncio:
                // Se va a la pantalla de crear anuncio
                Fragment fragment = new CrearModificarAnuncioFragment();
                FragmentManager fragmentManager = getFragmentManager();
                //el addToBackStack sirve para poder volver atras con boton back
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

                return true;
            case R.id.buscar_anuncio:
                LayoutInflater li = LayoutInflater.from(this.getActivity());
               final View prompt = li.inflate(R.layout.dialogopalabras, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
                alertDialogBuilder.setView(prompt);

                // Mostramos el mensaje del cuadro de dialogo
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("BUSCAR", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText e =(EditText) prompt.findViewById(R.id.palabrasClave);
                                //Aqui tenemos que buscar por las palabras clave
                                palabras = e.getText().toString();
                                List<Anuncio> lista = new ArrayList<>();
                                try {
                                    lista = conexiones.getAnuncios(tipo, especie, palabras);

                                } catch (ServerException ex) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Error obteniendo la lista",
                                            Toast.LENGTH_LONG).show();
                                }

                                miAdaptador.clear();
                                miAdaptador.addAll(lista);
                                miAdaptador.notifyDataSetChanged();
                            }
                        })

                        .setNegativeButton("BORRAR FILTRO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                palabras = "";
                                List<Anuncio> lista = new ArrayList<>();
                                try {
                                    lista = conexiones.getAnuncios(tipo, especie, palabras);

                                } catch (ServerException ex) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Error obteniendo la lista",
                                            Toast.LENGTH_LONG).show();
                                }

                                miAdaptador.clear();
                                miAdaptador.addAll(lista);
                                miAdaptador.notifyDataSetChanged();
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


}
