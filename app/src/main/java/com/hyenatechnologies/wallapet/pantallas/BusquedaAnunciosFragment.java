package com.hyenatechnologies.wallapet.pantallas;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hyenatechnologies.wallapet.Anuncio;
import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Raul on 18/04/2015.
 */
public class BusquedaAnunciosFragment extends Fragment {

    private TextView titulo;
    private TextView tipoS;
    private TextView especieS;
    private TextView wordS;
    private TextView titulo2;
    private ListView listResultados;
    private Spinner tipo;
    private Spinner especie;
    private Button botonBuscar;
    private EditText palabras;
    private List<String> ListaTipos = new ArrayList<String>();
    private List<String> ListaEspecies = new ArrayList<String>();
    private List<Anuncio> ListaAnuncios = new ArrayList<Anuncio>();
private Conexiones conexiones;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        conexiones = new Conexiones(this.getActivity());
        View rootView = inflater.inflate(R.layout.activity_buscar_anuncio, container, false);
        titulo = (TextView) rootView.findViewById(R.id.busquedaText);
        titulo2 = (TextView) rootView.findViewById(R.id.resultados);
        listResultados = (ListView) rootView.findViewById(R.id.listResultados);
        tipoS = (TextView) rootView.findViewById(R.id.seleccionTipo);
        especieS = (TextView) rootView.findViewById(R.id.seleccionEspecie);
        wordS = (TextView) rootView.findViewById(R.id.seleccionPalabras);
        tipo = (Spinner) rootView.findViewById(R.id.spinnerBusquedaTipo);
        ListaTipos.add("Cualquiera");
        ListaTipos.add("Venta");
        ListaTipos.add("Adopción");
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ListaTipos);
        tipo.setAdapter(adapter);
        especie = (Spinner) rootView.findViewById(R.id.spinnerBusquedaEspecie);
        ListaEspecies.add("Cualquiera");
        ListaEspecies.add("Mamiferos");
        ListaEspecies.add("Reptiles");
        ListaEspecies.add("Anfibios");
        ListaEspecies.add("Artropodos");
        ListaEspecies.add("Otros");
        ArrayAdapter<String> adapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ListaEspecies);
        especie.setAdapter(adapter2);
        palabras = (EditText) rootView.findViewById(R.id.palabras);
        botonBuscar = (Button) rootView.findViewById(R.id.botonBuscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Realizar búsqueda
                ListaAnuncios = realizarBusqueda();
                if(ListaAnuncios != null && !ListaAnuncios.isEmpty()) {
                    List<String> ListaTitulos = new ArrayList<String>();
                    for(Anuncio anuncio : ListaAnuncios){
                        ListaTitulos.add(anuncio.getIdAnuncio() + " - " + anuncio.getTitulo());
                    }
                    ArrayAdapter<String> adapter3 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ListaTitulos);
                    listResultados.setAdapter(adapter3);
                    cambiarVista();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Su búsqueda no produjo ningún resultado",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        listResultados.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String selected = listResultados.getItemAtPosition(arg2).toString();
                Scanner scan = new Scanner(selected);
                Integer id = new Integer(scan.next());
                scan.close();
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new VistaAnuncioFragment();
                Bundle args = new Bundle();
                args.putInt("anuncio", id);
                fragment.setArguments(args);
                //el addToBackStack sirve para poder volver atras con boton back
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });
        return rootView;
    }

    private List<Anuncio> realizarBusqueda(){
        String especieS = especie.getSelectedItem().toString();
        String tipoS = tipo.getSelectedItem().toString();
        if (tipoS.equals("Cualquiera")){
            tipoS = "";
        }
        if (especieS.equals("Cualquiera")){
            especieS = "";
        }
        String palabrasClave = palabras.getText().toString();
        try {
            Toast.makeText(getActivity().getApplicationContext(), "Buscando anuncios... Espere por favor.",
                    Toast.LENGTH_LONG).show();
            List<Anuncio> lista = conexiones.getAnuncios(tipoS, especieS, palabrasClave);
            return lista;
        } catch(ServerException ex){
            switch(ex.getCode()){
                case 500:
                    Toast.makeText(getActivity().getApplicationContext(), "No se pudo establecer conexión con el servidor",
                            Toast.LENGTH_LONG).show();
                    break;
                case 405:
                    //No hay sesion iniciada, vamos al login...
                    Toast.makeText(getActivity().getApplicationContext(), "Sesión caducada",
                            Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(myIntent);
                    getActivity().finish();
                    break;
            }

            return null;
        }
    }

    private void cambiarVista(){
        titulo.setVisibility(View.GONE);
        tipoS.setVisibility(View.GONE);
        especieS.setVisibility(View.GONE);
        wordS.setVisibility(View.GONE);
        tipo.setVisibility(View.GONE);
        especie.setVisibility(View.GONE);
        botonBuscar.setVisibility(View.GONE);
        palabras.setVisibility(View.GONE);
        titulo2.setVisibility(View.VISIBLE);
        listResultados.setVisibility(View.VISIBLE);
    }
}
