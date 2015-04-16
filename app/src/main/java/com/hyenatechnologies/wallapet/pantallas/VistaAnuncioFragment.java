package com.hyenatechnologies.wallapet.pantallas;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyenatechnologies.wallapet.Anuncio;
import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.VariablesComunes;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;

/**
 * Muestra un anuncio. Recibe por "bundle" el id del anuncio a mostrar.
 * Si no recibe nada, muestra el 1.
 */
public class VistaAnuncioFragment extends Fragment {

    //Variables
    TextView anuncioId;
    TextView anuncioEmail;
    TextView anuncioEstado;
    TextView anuncioDescripcion;
    TextView anuncioTipo;
    TextView anuncioPrecio;
    TextView anuncioTitulo;
    TextView anuncioEspecie;

    Anuncio actual;
    EditText id_a_cargar;
    Button botonVer;
    VariablesComunes variables;


    public VistaAnuncioFragment() {
    }

    ;

    public VistaAnuncioFragment(VariablesComunes v){
        this.variables = v;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_vista_anuncio, container, false);
        setHasOptionsMenu(true);

        //Estas dos lineas siguientes son para permitir el uso de la red
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ((PantallaPrincipal) getActivity()).setTitle("Anuncio");

        //Cargamos cuadro de id_a_cargar de id de anuncio
        id_a_cargar = (EditText) rootView.findViewById(R.id.verAnuncio);

        //Declaramos cuadros de id_a_cargar
        anuncioId = (TextView) rootView.findViewById(R.id.anuncioId);
        anuncioEmail = (TextView) rootView.findViewById(R.id.anuncioEmail);
        anuncioEstado = (TextView) rootView.findViewById(R.id.anuncioEstado);
        anuncioDescripcion = (TextView) rootView.findViewById(R.id.anuncioDescripcion);
        anuncioTipo = (TextView) rootView.findViewById(R.id.anuncioTipo);
        anuncioPrecio = (TextView) rootView.findViewById(R.id.anuncioPrecio);
        anuncioTitulo = (TextView) rootView.findViewById(R.id.anuncioTitulo);
        anuncioEspecie = (TextView) rootView.findViewById(R.id.anuncioEspecie);



        //Cargamos el anuncio que nos pasan por bundle, 1 en caso de que no nos pasen nada
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int a = bundle.getInt("anuncio", 1);
            cargarAnuncio(a);
        }
        return rootView;
    }


    /**
     * Muestra el anuncio en pantalla
     */
    public void mostrarAnuncio(Anuncio a) {
        anuncioId.setText("" + a.getIdAnuncio());
        anuncioEmail.setText(a.getEmail());
        anuncioEstado.setText(a.getEstado());
        anuncioDescripcion.setText(a.getDescripcion());
        anuncioTipo.setText(a.getTipoIntercambio());
        anuncioPrecio.setText("" + a.getPrecio() + "€");
        anuncioTitulo.setText(a.getTitulo());
        anuncioEspecie.setText(a.getEspecie());
    }

    /**
     * Carga de la base de datos el anuncio con id <idanuncio>
     * y lo muestra.
     *
     * @param idAnuncio
     */
    private void cargarAnuncio(int idAnuncio) {
            Anuncio b;
            try {
                b = Conexiones.getAnuncioById(idAnuncio);
                actual = b;
                mostrarAnuncio(b);
                Toast.makeText(getActivity().getApplicationContext(), "Anuncio cargado correctamente",
                        Toast.LENGTH_LONG).show();
            } catch (ServerException ex) {
                //El anuncio no existe
                switch (ex.getCode()) {
                    case 404:
                        Toast.makeText(getActivity().getApplicationContext(), "El anuncio indicado no existe",
                                Toast.LENGTH_LONG).show();
                        //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                        break;
                    case 500:
                        Toast.makeText(getActivity().getApplicationContext(), "Error al contactar con el servidor",
                                Toast.LENGTH_LONG).show();
                       // getFragmentManager().popBackStackImmediate();
                        break;
                    case 403:
                        Toast.makeText(getActivity().getApplicationContext(), "Error de permisos",
                                Toast.LENGTH_LONG).show();
                        //getFragmentManager().popBackStackImmediate();
                        break;

                }
            }

    }

    public void onCreateOptionsMenu(

            Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_vista_anuncio, menu);

    }

    /**
     * Lanza el fragment de actualizar anuncio, con los datos del anuncio cargado.
     */
    public void lanzarFragmentActualizar(){
        // do s.th.
        //Paso 1: Obtener la instancia del administrador de fragmentos
        FragmentManager fragmentManager = getFragmentManager();

        //Paso 2: Crear una nueva transacción
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //Paso 3: Crear un nuevo fragmento y añadirlo
        CrearModificarAnuncioFragment fragment = new CrearModificarAnuncioFragment();
        Bundle bundle = new Bundle();
        bundle.putString("anuncio", Anuncio.toJson(actual));
        fragment.setArguments(bundle);
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        //Paso 4: Confirmar el cambio
        transaction.commit();
    }

    /**
     * Borra de la base de datos el anuncio especificado
     * @param idAnuncio
     */
    public void borrarAnuncio(int idAnuncio){
        try {
            Conexiones.deleteAnuncio(idAnuncio);
            Toast.makeText(getActivity().getApplicationContext(), "El anuncio se ha borrado con éxito",
                    Toast.LENGTH_LONG).show();
        } catch (ServerException ex) {
            switch (ex.getCode()) {
                case 404:
                    Toast.makeText(getActivity().getApplicationContext(), "El anuncio indicado no existe",
                            Toast.LENGTH_LONG).show();
                    break;
                case 500:
                    Toast.makeText(getActivity().getApplicationContext(), "Error al contactar con el servidor",
                            Toast.LENGTH_LONG).show();
                    break;
                case 403:
                    Toast.makeText(getActivity().getApplicationContext(), "Error de permisos",
                            Toast.LENGTH_LONG).show();
                    break;

            }
        }
    }

    @Override
    /**
     * Controla las acciones del menu
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.actualizar_evento:
                lanzarFragmentActualizar();
                return true;
            case R.id.borrar_evento:
                // do s.th
                borrarAnuncio(actual.getIdAnuncio());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
