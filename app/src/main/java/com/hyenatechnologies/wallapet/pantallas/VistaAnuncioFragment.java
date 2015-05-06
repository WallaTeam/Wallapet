/** Copyright (C) 2015 Hyena Technologies
 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.
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

import com.hyenatechnologies.wallapet.Anuncio;
import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.ValorSesion;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;

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

    public VistaAnuncioFragment() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_vista_anuncio, container, false);



        //Estas dos lineas siguientes son para permitir el uso de la red
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        conexiones = new Conexiones(this.getActivity());
        ((PantallaPrincipal) getActivity()).setTitle("Ver anuncio");

        //Cargamos cuadro de id_a_cargar de id de anuncio
        id_a_cargar = (EditText) rootView.findViewById(R.id.verAnuncio);

        //Declaramos cuadros de id_a_cargar
        imagen = (ImageView) rootView.findViewById(R.id.ficha_imagen);
        anuncioEmail = (TextView) rootView.findViewById(R.id.anuncioEmail);
        anuncioEstado = (TextView) rootView.findViewById(R.id.anuncioEstado);
        anuncioDescripcion = (TextView) rootView.findViewById(R.id.anuncioDescripcion);
        anuncioTipo = (TextView) rootView.findViewById(R.id.anuncioTipo);
        anuncioPrecio = (TextView) rootView.findViewById(R.id.anuncioPrecio);
        anuncioTitulo = (TextView) rootView.findViewById(R.id.anuncioTitulo);
        anuncioEspecie = (TextView) rootView.findViewById(R.id.anuncioEspecie);
        botonComprar = (Button) rootView.findViewById(R.id.solicitarInformacion);
        botonCerrar = (Button) rootView.findViewById(R.id.cerrarAnuncio);
        lblPrecio = (TextView) rootView.findViewById(R.id.lblPrecio);


        //Cargamos el anuncio que nos pasan por bundle, 1 en caso de que no nos pasen nada
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int a = bundle.getInt("anuncio", 1);
            cargarAnuncio(a);

            //Miramos si el usuario logueado es el propietario. Si es así,
            //se muestran iconos de borrar y editar.
            if (actual.getEmail().equalsIgnoreCase(ValorSesion.getCuenta().getEmail())){
                setHasOptionsMenu(true);
                botonComprar.setVisibility(View.GONE);
            }
            else{
                setHasOptionsMenu(false);
                botonCerrar.setVisibility(View.GONE);
            }

            if(actual.getTipoIntercambio().contains("Adopci")){
                //es de tipo Adopcion ó Adopción, se quita el campo de precio
                anuncioPrecio.setVisibility(View.GONE);
                lblPrecio.setVisibility(View.GONE);
            }
        }

        botonCerrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(getActivity());
                final View prompt = li.inflate(R.layout.dialogofinalizaranuncio, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(prompt);

                // Mostramos el mensaje del cuadro de dialogo
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("FINALIZAR TRATO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Se cierra el anuncio
                                try{
                                    conexiones.cerrarAnuncio(actual.getIdAnuncio());
                                    Toast.makeText(getActivity().getApplicationContext(), "Anuncio cerrado con éxito",
                                            Toast.LENGTH_SHORT).show();
                                    //Cerrado con éxito, se vuelve atrás
                                    getFragmentManager().popBackStack();
                                }
                                catch(ServerException ex){
                                    switch(ex.getCode()){
                                        case 500:
                                            Toast.makeText(getActivity().getApplicationContext(), "Error al contactar con el servidor",
                                                    Toast.LENGTH_SHORT).show();
                                            break;
                                        case 403:
                                            Toast.makeText(getActivity().getApplicationContext(), "Error de permisos",
                                                    Toast.LENGTH_SHORT).show();
                                            break;
                                        case 404:
                                            Toast.makeText(getActivity().getApplicationContext(), "No existe el anuncio indicado.",
                                                    Toast.LENGTH_SHORT).show();
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
                                }
                            }
                        })

                        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });



                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
        return rootView;
    }


    /**
     * Muestra el anuncio en pantalla
     */
    public void mostrarAnuncio(Anuncio a) {
        if(a.getEmail().equalsIgnoreCase(ValorSesion.getCuenta().getEmail())){
            anuncioEmail.setText(a.getEmail() + " (TÚ)");
        }
        else{
            anuncioEmail.setText(a.getEmail());
        }

        anuncioEstado.setText(a.getEstado());
        anuncioDescripcion.setText(a.getDescripcion());
        anuncioTipo.setText(a.getTipoIntercambio());
        anuncioPrecio.setText("" + a.getPrecio() + "€");
        anuncioTitulo.setText(a.getTitulo());
        anuncioEspecie.setText(a.getEspecie());
        if (a.getRutaImagen()!=null && a.getRutaImagen().length()>0) {
            // show The Image
            new DownloadImageTask((ImageView) imagen)
                    .execute(a.getRutaImagen());
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

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

        protected void onPostExecute(Bitmap result) {
            Configuration configuration = getActivity().getResources().getConfiguration();
            int screenWidthDp = configuration.screenWidthDp;
            final float scale = getActivity().getResources().getDisplayMetrics().density;
            int p = (int) (screenWidthDp * scale + 0.5f);

            if(result!=null) {
                Bitmap b2 = Bitmap.createScaledBitmap(result, p, p, true);
                bmImage.setImageBitmap(b2);
            }
        }
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
                b = conexiones.getAnuncioById(idAnuncio);
                actual = b;
                mostrarAnuncio(b);

            } catch (ServerException ex) {
                //El anuncio no existe
                switch (ex.getCode()) {
                    case 404:
                        Toast.makeText(getActivity().getApplicationContext(), "El anuncio indicado no existe",
                                Toast.LENGTH_SHORT).show();
                        //getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                        break;
                    case 500:
                        Toast.makeText(getActivity().getApplicationContext(), "Error al contactar con el servidor",
                                Toast.LENGTH_SHORT).show();
                       // getFragmentManager().popBackStackImmediate();
                        break;
                    case 403:
                        Toast.makeText(getActivity().getApplicationContext(), "Error de permisos",
                                Toast.LENGTH_SHORT).show();
                        //getFragmentManager().popBackStackImmediate();
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
    public void borrarAnuncio(final int idAnuncio){

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View prompt = li.inflate(R.layout.dialogoborraranuncio, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(prompt);

        // Mostramos el mensaje del cuadro de dialogo
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("BORRAR ANUNCIO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {

                            conexiones.deleteAnuncio(idAnuncio);
                            Toast.makeText(getActivity().getApplicationContext(), "El anuncio se ha borrado con éxito",
                                    Toast.LENGTH_SHORT).show();
                            getFragmentManager().popBackStack();
                        } catch (ServerException ex) {
                            switch (ex.getCode()) {
                                case 404:
                                    Toast.makeText(getActivity().getApplicationContext(), "El anuncio indicado no existe",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity().getApplicationContext(), "Error al contactar con el servidor",
                                            Toast.LENGTH_SHORT).show();
                                    break;
                                case 403:
                                    Toast.makeText(getActivity().getApplicationContext(), "Error de permisos",
                                            Toast.LENGTH_SHORT).show();
                                    break;

                            }
                        }
                    }
                })

                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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
