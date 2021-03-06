/**
 * Nombre:  ProfileFragment.java
 * Version: 1.0
 * Autor: Ismael Rodriguez
 * Fecha: 5-5-2015
 * Descripcion: Este fichero implementa el fragmento de ver perfil de usuario.
 */

package com.hyenatechnologies.wallapet.pantallas;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.clasesEstaticas.ValorSesion;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;
import com.hyenatechnologies.wallapet.objetosDeDatos.Cuenta;


public class ProfileFragment extends Fragment {

    TextView usuario;
    TextView nombre;
    TextView apellidos;
    TextView email;
    TextView telefono;
    TextView direccion;
    TextView DNI;
    Button btnBorrar;
    Conexiones conexiones;

    @Override
    /**
     *Pre: inflater != null && container != null && savedInstanceState != null.
     * Post: Método por defecto en la creación de vista. Encargado de crear todos
     * los elementos de la pantalla e inicializarlos.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        rootView = inflater.inflate(R.layout.fragment_perfil, container, false);
        conexiones = new Conexiones(getActivity());
        ((PantallaPrincipalActivity) getActivity()).setTitle("Perfil de usuario");

        //Cargamos elementos de GUI
        usuario = (TextView) rootView.findViewById(R.id.perfil_usuario);
        nombre = (TextView) rootView.findViewById(R.id.perfil_nombre);
        apellidos = (TextView) rootView.findViewById(R.id.perfil_apellidos);
        email = (TextView) rootView.findViewById(R.id.perfil_email);
        telefono = (TextView) rootView.findViewById(R.id.perfil_telefono);
        direccion = (TextView) rootView.findViewById(R.id.perfil_direccion);
        DNI = (TextView) rootView.findViewById(R.id.perfil_DNI);
        btnBorrar = (Button) rootView.findViewById(R.id.borrarCuenta);


        Cuenta c = ValorSesion.getCuenta(); //Cuenta logueada
        if(c!=null){

            //Hay alguien logueado, mostramos sus datos
            usuario.setText(c.getUsuario());
            nombre.setText(c.getNombre());
            apellidos.setText(c.getApellido());
            email.setText(c.getEmail());
            telefono.setText("" + c.getTelefono());
            direccion.setText(c.getDireccion());
            DNI.setText(c.getDNI());
        }
        else{

            //No hay sesion iniciada, vamos al login...
            Toast.makeText(getActivity().getApplicationContext(), "Sesión caducada",
                    Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(myIntent);
            getActivity().finish();

        }
        /**
         * Click listener del boton borrar. Lanza un dialogo que pide confirmacion
         * para borrar la cuenta. si se dice que no, no pasa nada.
         * Si se dice que si, borra la cuenta y todos sus anuncios.
         * si hay un error, lo muestra.
         */
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                final View prompt = li.inflate(R.layout.dialog_borrar_usuario_confirmar, null);

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(prompt);
                alertDialogBuilder.setInverseBackgroundForced(true);
                // Mostramos el mensaje del cuadro de dialogo
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("BORRAR CUENTA", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new DeleteProfileTask().execute("");

                            }
                        })

                        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //No ocurre nada
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
     * Clase que se encarga del borrado de perfil
     */
    private class DeleteProfileTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialogo;

        public DeleteProfileTask() {
            super();
            dialogo = new ProgressDialog(getActivity());
        }

        /**
         * Pre: cierto
         * Post: muestra el dialogo de borrando...
         */
        protected void onPreExecute() {
            dialogo.setCancelable(false);
            dialogo.setMessage("Borrando perfil...");
            dialogo.show();
        }


        @Override
        /**
         * Pre: ninguno
         * Post: Borra el anuncio en background
         * */
        protected String doInBackground(String... urls) {
            //Borrar cuenta
            try{
                conexiones.borrarUsuario(
                        ValorSesion.getCuenta().getEmail());
                return "";


            }
            catch(ServerException ex){
                switch (ex.getCode()) {
                    case 500:
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity().
                                                getApplicationContext(),
                                        "Error al contactar con server",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                        break;
                    case 403:
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getActivity().
                                                getApplicationContext(),
                                        "Error de permisos",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 405:
                        //No hay sesion iniciada, vamos al login...
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                //Cerramos el dialogo en curso
                                if (dialogo.isShowing()) {
                                    dialogo.dismiss();
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
                }
                return null;
            }


        }


        /**
         * Pre: cierto
         * Post: actualiza la UI cuando se ha borrado la cuenta.
         */ @Override

        protected void onPostExecute(String result) {

            if (dialogo.isShowing()) {
                dialogo.dismiss();
            }
            if(result!=null) {
                //Se ha borrado con éxito
                //Nos vamos a pantalla de login
                Toast.makeText(getActivity().
                                getApplicationContext(),
                        "Cuenta borrada con exito",
                        Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getActivity(),
                        LoginActivity.class);
                startActivity(myIntent);
                getActivity().finish();
            }

        }


    }
}

