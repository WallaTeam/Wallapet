package com.hyenatechnologies.wallapet.pantallas;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hyenatechnologies.wallapet.Anuncio;
import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;

import java.util.ArrayList;
import java.util.List;


/**
 * Pantalla de crear un nuevo anuncio o modificar uno existente
 */
public class CrearModificarAnuncioFragment extends Fragment {

    private static final int MODO_CREAR = 1;
    private static final int MODO_ACTUALIZAR = 2;


    //Variables
    private EditText titulo;
    private EditText email;
    private EditText descripcion;
    private Spinner estado;
    private Spinner tipo;
    private EditText especie;
    private EditText precio;
    private Button botonCrear;
    int modo = MODO_CREAR;
    Anuncio modificando;
    private List<String> ListaEstados = new ArrayList<String>();
    private List<String> ListaTipos = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_crear_anuncio, container, false);

        //Cargamos campos de id_a_cargar
        titulo = (EditText) rootView.findViewById(R.id.crearAnuncioTitulo);
        email = (EditText) rootView.findViewById(R.id.crearAnuncioEmail);
        descripcion = (EditText) rootView.findViewById(R.id.crearAnuncioDescripcion);
        //spinner
        estado = (Spinner) rootView.findViewById(R.id.spinnerCrearAnuncioEstado);
        ListaEstados.add("Abierto");
        ListaEstados.add("Cerrado");
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ListaEstados);
        estado.setAdapter(adapter);
        //spinner
        tipo = (Spinner) rootView.findViewById(R.id.spinnerCrearAnuncioTipo);
        ListaTipos.add("Adopcion");
        ListaTipos.add("Venta");
        ArrayAdapter<String> adapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ListaTipos);
        tipo.setAdapter(adapter2);
        precio = (EditText) rootView.findViewById(R.id.crearAnuncioPrecio);
        especie = (EditText) rootView.findViewById(R.id.crearAnuncioEspecie);

        //Cargamos boton
        botonCrear = (Button) rootView.findViewById(R.id.crearAnuncioOK);

        //Estas dos lineas siguientes son para permitir el uso de la red
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //Si en el intent hay un anuncio JSON con nombre "anuncio",
        //es que estamos en modo actualizar. Si no, modo crear.
        Bundle bundle = this.getArguments();
        String jsonAnuncio = null;
        if (bundle != null) {
            jsonAnuncio = bundle.getString("anuncio");
        }


        if (jsonAnuncio == null) {
            modo = MODO_CREAR;
            ((PantallaPrincipal) getActivity()).setTitle("Crear anuncio");
            botonCrear.setText("Crear anuncio");
            //No nos pasan anuncio, nos piden crear
        } else {
            modo = MODO_ACTUALIZAR;
            ((PantallaPrincipal) getActivity()).setTitle("Actualizar anuncio");
            botonCrear.setText("Actualizar anuncio");
            //Nos pasan anuncio, modificamos
            Anuncio a = Anuncio.fromJson(jsonAnuncio);
            modificando = a;
            mostrarAnuncio(a);

        }


        //Establecemos que hace el boton al ser pulsado
        botonCrear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Recogemos datos de los campos de id_a_cargar
                Anuncio a = new Anuncio();

                a.setTitulo(titulo.getText().toString());
                a.setEmail(email.getText().toString());
                a.setDescripcion(descripcion.getText().toString());
                a.setEstado(estado.getSelectedItem().toString());
                a.setEspecie(especie.getText().toString());
                a.setTipoIntercambio(tipo.getSelectedItem().toString());
                a.setPrecio(Double.parseDouble(precio.getText().toString()));

                //Guardamos el anuncio
                try {
                    if (modo == MODO_CREAR) {
                        //Modo crear
                        Conexiones.createAnuncio(a);
                        Toast.makeText(getActivity().getApplicationContext(), "Anuncio creado correctamente",
                                Toast.LENGTH_LONG).show();
                    } else if (modo == MODO_ACTUALIZAR) {
                        //Modo actualizar, tenemos q poner el id del anuncio a modificar
                        a.setIdAnuncio(modificando.getIdAnuncio());
                        Conexiones.updateAnuncio(a);
                        Toast.makeText(getActivity().getApplicationContext(), "Anuncio actualizado correctamente",
                                Toast.LENGTH_LONG).show();
                    }

                } catch (ServerException ex) {
                    switch (ex.getCode()) {

                        case 500:
                            Toast.makeText(getActivity().getApplicationContext(), "Error al contactar con el servidor",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case 403:
                            Toast.makeText(getActivity().getApplicationContext(), "Error de permisos",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Toast.makeText(getActivity().getApplicationContext(), "No existe el anuncio indicado.",
                                    Toast.LENGTH_LONG).show();
                            break;

                    }

                }

            }
        });
        return rootView;
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_crear_anuncio, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Rellena los campos
     */
    public void mostrarAnuncio(Anuncio a) {

        email.setText(a.getEmail());
        if (a.getEstado().compareTo("Abierto") == 0) {
            estado.setGravity(0);
        } else {
            estado.setGravity(1);
        }
        descripcion.setText(a.getDescripcion());
        especie.setText(a.getEspecie());
        if (a.getTipoIntercambio().compareTo("Adopcion") == 0) {
            tipo.setGravity(0);
        } else {
            tipo.setGravity(1);
        }
        precio.setText("" + a.getPrecio());
        titulo.setText(a.getTitulo());
    }


}
