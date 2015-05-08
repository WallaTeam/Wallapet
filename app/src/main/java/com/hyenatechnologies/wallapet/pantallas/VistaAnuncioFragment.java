/**
 * Nombre:  VistaAnuncioFragment.java
 * Version: 2.0
 * Autor: Sergio Soro, Raul Piraces, Ismael Rodriguez
 * Fecha: 5-4-2015
 * Descripcion: Este fichero implementa el fragmento de mostrar informacion
 * de un anuncio.
 */

package com.hyenatechnologies.wallapet.pantallas;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.clasesEstaticas.ValorSesion;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;
import com.hyenatechnologies.wallapet.objetosDeDatos.Anuncio;

import java.io.InputStream;

/**
 * Muestra un anuncio. Recibe por "bundle" el id del anuncio a mostrar.
 * Si no recibe nada, muestra el 1.
 */
public class VistaAnuncioFragment extends Fragment {

    //Variables
    TextView anuncioEmail;
    TextView anuncioEstado;
    TextView anuncioDescripcion;
    TextView anuncioTipo;
    TextView anuncioPrecio;
    TextView anuncioTitulo;
    TextView anuncioEspecie;
    TextView lblPrecio;
    Conexiones conexiones;
    Anuncio actual;
    EditText id_a_cargar;
    Button botonComprar;
    Button botonCerrar;
    ImageView imagen;


    @Override
    /**
     *Pre: inflater != null && container != null && savedInstanceState != null.
     * Post: Método por defecto en la creación de vista. Encargado de crear todos
     * los elementos de la pantalla e inicializarlos.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(
                R.layout.fragment_vista_anuncio, container, false);
        ((PantallaPrincipalActivity) getActivity()).setTitle("Ver anuncio");


        //Estas dos lineas siguientes son para permitir el uso de la red
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        conexiones = new Conexiones(this.getActivity());




        //Declaramos cuadros de id_a_cargar
        imagen = (ImageView) rootView.findViewById(R.id.ficha_imagen);
        anuncioEmail = (TextView) rootView.findViewById(R.id.anuncioEmail);
        anuncioEstado = (TextView) rootView.findViewById(R.id.anuncioEstado);
        anuncioDescripcion = (TextView) rootView.findViewById(
                R.id.anuncioDescripcion);
        anuncioTipo = (TextView) rootView.findViewById(R.id.anuncioTipo);
        anuncioPrecio = (TextView) rootView.findViewById(R.id.anuncioPrecio);
        anuncioTitulo = (TextView) rootView.findViewById(R.id.anuncioTitulo);
        anuncioEspecie = (TextView) rootView.findViewById(R.id.anuncioEspecie);
        botonComprar = (Button) rootView.findViewById(R.id.solicitarInformacion);
        botonCerrar = (Button) rootView.findViewById(R.id.cerrarAnuncio);
        lblPrecio = (TextView) rootView.findViewById(R.id.lblPrecio);


        /*Cargamos el anuncio que nos pasan por bundle, 1 en caso de que no nos
        pasen nada*/
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int a = bundle.getInt("anuncio", 1);
            cargarAnuncio(a);

            /*Miramos si el usuario logueado es el propietario. Si es así,
            se muestran iconos de borrar y editar.*/
            if (actual.getEmail().
                    equalsIgnoreCase(ValorSesion.getCuenta().getEmail())) {
                setHasOptionsMenu(true);
                botonComprar.setVisibility(View.GONE);
            } else {
                setHasOptionsMenu(false);
                botonCerrar.setVisibility(View.GONE);
            }

            if (actual.getTipoIntercambio().contains("Adopci")) {

                //es de tipo Adopcion ó Adopción, se quita el campo de precio
                anuncioPrecio.setVisibility(View.GONE);
                lblPrecio.setVisibility(View.GONE);
            }
        }

        /**
         * Click listener del boton de realizar trato.En caso de error se mostrara.
         */
        botonComprar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(getActivity());
                final View prompt = li.inflate(R.layout.dialog_comprar,
                        null);

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(prompt);

                // Mostramos el mensaje del cuadro de dialogo
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("CONTACTAR", new DialogInterface.
                                OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //Se cierra el anuncio
                                try {
                                    conexiones.realizarTrato(actual.getIdAnuncio());
                                    Toast.makeText(getActivity().
                                                    getApplicationContext(),
                                            "PDFs de contacto enviados",
                                            Toast.LENGTH_SHORT).show();

                                    //Cerrado con éxito, se vuelve atrás
                                    getFragmentManager().popBackStack();
                                } catch (ServerException ex) {
                                    switch (ex.getCode()) {
                                        case 500:
                                            Toast.makeText(getActivity().
                                                            getApplicationContext(),
                                                    "Error al contactar con server",
                                                    Toast.LENGTH_SHORT).show();
                                            break;
                                        case 403:
                                            Toast.makeText(getActivity().
                                                            getApplicationContext(),
                                                    "Error de permisos",
                                                    Toast.LENGTH_SHORT).show();
                                            break;
                                        case 404:
                                            Toast.makeText(getActivity().
                                                            getApplicationContext(),
                                                    "No existe el anuncio indicado.",
                                                    Toast.LENGTH_SHORT).show();
                                            break;
                                        case 405:
                                            //No hay sesion iniciada, vamos al login
                                            Toast.makeText(getActivity().
                                                            getApplicationContext(),
                                                    "Sesión caducada",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent myIntent = new Intent(
                                                    getActivity(),
                                                    LoginActivity.class);
                                            startActivity(myIntent);
                                            getActivity().finish();
                                            break;
                                    }
                                }
                            }
                        })

                        .setNegativeButton("CANCELAR",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int id) {

                                        //En caso de cancelar no se hace nada
                                        d.cancel();
                                    }
                                });

                //Se muestra el dialogo
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
        /**
         * Click listener del boton de cerrar anuncio. Se cerrara el anuncio si el
         * que clicka es el propietario. En caso de error se mostrara.
         */
        botonCerrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(getActivity());
                final View prompt = li.inflate(R.layout.dialog_finalizar_anuncio_confirmar,
                        null);

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(prompt);

                // Mostramos el mensaje del cuadro de dialogo
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("FINALIZAR TRATO", new DialogInterface.
                                OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //Se cierra el anuncio
                                try {
                                    conexiones.cerrarAnuncio(actual.getIdAnuncio());
                                    Toast.makeText(getActivity().
                                                    getApplicationContext(),
                                            "Anuncio cerrado con éxito",
                                            Toast.LENGTH_SHORT).show();

                                    //Cerrado con éxito, se vuelve atrás
                                    getFragmentManager().popBackStack();
                                } catch (ServerException ex) {
                                    switch (ex.getCode()) {
                                        case 500:
                                            Toast.makeText(getActivity().
                                                            getApplicationContext(),
                                                    "Error al contactar con server",
                                                    Toast.LENGTH_SHORT).show();
                                            break;
                                        case 403:
                                            Toast.makeText(getActivity().
                                                            getApplicationContext(),
                                                    "Error de permisos",
                                                    Toast.LENGTH_SHORT).show();
                                            break;
                                        case 404:
                                            Toast.makeText(getActivity().
                                                            getApplicationContext(),
                                                    "No existe el anuncio indicado.",
                                                    Toast.LENGTH_SHORT).show();
                                            break;
                                        case 405:
                                            //No hay sesion iniciada, vamos al login
                                            Toast.makeText(getActivity().
                                                            getApplicationContext(),
                                                    "Sesión caducada",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent myIntent = new Intent(
                                                    getActivity(),
                                                    LoginActivity.class);
                                            startActivity(myIntent);
                                            getActivity().finish();
                                            break;
                                    }
                                }
                            }
                        })

                        .setNegativeButton("CANCELAR",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface d, int id) {

                                        //En caso de cancelar no se hace nada
                                        d.cancel();
                                    }
                                });

                //Se muestra el dialogo
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
        return rootView;
    }


    /**
     * Pre: a!=null
     * Post: Muestra el anuncio a en pantalla.
     */
    public void mostrarAnuncio(Anuncio a) {

        if (a.getEmail().equalsIgnoreCase(ValorSesion.getCuenta().getEmail())) {

            //El anuncio visualizado pertenece al logueado
            anuncioEmail.setText(a.getEmail() + " (TÚ)");
        } else {
            anuncioEmail.setText(a.getEmail());
        }

        anuncioEstado.setText(a.getEstado());
        anuncioDescripcion.setText(a.getDescripcion());
        anuncioTipo.setText(a.getTipoIntercambio());
        anuncioPrecio.setText("" + a.getPrecio() + "€");
        anuncioTitulo.setText(a.getTitulo());
        anuncioEspecie.setText(a.getEspecie());

        if (a.getRutaImagen() != null && a.getRutaImagen().length() > 0) {

            /* Hay imagen en el anuncio que estamos cargando, cargamos la imagen
            de forma asincrona */
            new DownloadImageTask((ImageView) imagen)
                    .execute(a.getRutaImagen());
        }
    }


    /**
     * Pre: idAnuncio >=0
     * Post: Carga de la base de datos el anuncio con id <idanuncio> y lo muestra,
     * indicando error si se produce algun problema.
     */
    private void cargarAnuncio(int idAnuncio) {
        Anuncio b;
        try {

            //Se intenta obtener el anuncio
            b = conexiones.getAnuncioById(idAnuncio);
            actual = b;
            mostrarAnuncio(b);

        } catch (ServerException ex) {

            //El anuncio no existe
            switch (ex.getCode()) {
                case 404:
                    Toast.makeText(getActivity().getApplicationContext(),
                            "El anuncio indicado no existe",
                            Toast.LENGTH_SHORT).show();

                    break;
                case 500:
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Error al contactar con el servidor",
                            Toast.LENGTH_SHORT).show();

                    break;
                case 403:
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Error de permisos",
                            Toast.LENGTH_SHORT).show();

                    break;
                case 405:
                    //No hay sesion iniciada, vamos al login...
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Sesión caducada",
                            Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(getActivity(),
                            LoginActivity.class);
                    startActivity(myIntent);
                    getActivity().finish();
                    break;
            }
        }
    }

    /**
     * Pre: menu!=null e inflater!=null
     * Post: Infla los objetos del menu para usarse en el action bar.
     * En este caso, infla el menu de vista de anuncio.
     */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_vista_anuncio, menu);

    }

    /**
     * Pre: cierto
     * Post: Lanza el fragment de actualizar anuncio, con los datos del anuncio
     * cargado.
     */
    public void lanzarFragmentActualizar() {

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
     * Pre: idAnuncio >=0
     * Post: Borra de la base de datos el anuncio especificado. Si no existe o se
     * produce algun error de servidor o permisos, lo indica en pantalla.
     * Muestra un dialogo de confirmacion antes de borrar.
     */
    public void borrarAnuncio(final int idAnuncio) {

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View prompt = li.inflate(R.layout.dialog_borrar_anuncio_confirmar, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setView(prompt);

        // Mostramos el mensaje del cuadro de dialogo
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("BORRAR ANUNCIO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //Pulsa boton de borrar anuncio
                        try {

                            conexiones.deleteAnuncio(idAnuncio);

                            Toast.makeText(getActivity().getApplicationContext(),
                                    "El anuncio se ha borrado con éxito",
                                    Toast.LENGTH_SHORT).show();
                            //Volvemos atras
                            getFragmentManager().popBackStack();
                        } catch (ServerException ex) {
                            switch (ex.getCode()) {
                                case 404:
                                    Toast.makeText(getActivity().
                                                    getApplicationContext(),
                                            "El anuncio indicado no existe",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity().
                                                    getApplicationContext(),
                                            "Error al contactar con el servidor",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                case 403:
                                    Toast.makeText(getActivity().
                                                    getApplicationContext(),
                                            "Error de permisos",
                                            Toast.LENGTH_SHORT).show();
                                    break;

                            }
                        }
                    }
                })

                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //Cancelar borrado
                        dialog.cancel();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    /**
     * Pre: item!=null
     * Post: Controla las acciones del menu
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actualizar_evento:

                //Actualizar anuncio
                lanzarFragmentActualizar();
                return true;
            case R.id.borrar_evento:

                //Borrar anuncio
                borrarAnuncio(actual.getIdAnuncio());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Clase que implementa la descarga asincrona de imagen de anuncio.
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        /**
         * Pre: bmImage != null
         * Post: ha inicialiado el objeto
         */
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        /**
         * Pre: urls es una lista con al menos una url
         * Post: Ha descargado la imagen de la url de forma asincrona.
         */
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        /**
         * Pre: result!=null y se ha descargado la imagen
         * Post: Pone la imagen en la GUI, cambiandole el tamaño antes
         * para evitar lags.
         */
        protected void onPostExecute(Bitmap result) {
            Configuration configuration = getActivity().
                    getResources().getConfiguration();
            int screenWidthDp = configuration.screenWidthDp;
            final float scale = getActivity().getResources().
                    getDisplayMetrics().density;
            int p = (int) (screenWidthDp * scale + 0.5f);

            if (result != null) {
                Bitmap b2 = Bitmap.createScaledBitmap(result, p, p, true);
                bmImage.setImageBitmap(b2);
            }
        }
    }


}
