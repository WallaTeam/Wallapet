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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyenatechnologies.wallapet.Cuenta;
import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.ValorSesion;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;

/*  Fragment para seccion perfil */
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        rootView = inflater.inflate(R.layout.activity_perfil, container, false);

        ((PantallaPrincipal) getActivity()).setTitle("Perfil de usuario");
        usuario = (TextView) rootView.findViewById(R.id.perfil_usuario);
        nombre = (TextView) rootView.findViewById(R.id.perfil_nombre);
        apellidos = (TextView) rootView.findViewById(R.id.perfil_apellidos);
        email = (TextView) rootView.findViewById(R.id.perfil_email);
        telefono = (TextView) rootView.findViewById(R.id.perfil_telefono);
        direccion = (TextView) rootView.findViewById(R.id.perfil_direccion);
        DNI = (TextView) rootView.findViewById(R.id.perfil_DNI);
        btnBorrar = (Button) rootView.findViewById(R.id.borrarCuenta);

        conexiones = new Conexiones(getActivity());
        Cuenta c = ValorSesion.getCuenta();
        if(c!=null){
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
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getActivity());
                final View prompt = li.inflate(R.layout.dialogoborrarusuario, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(prompt);

                // Mostramos el mensaje del cuadro de dialogo
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("BORRAR CUENTA", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try{
                                    conexiones.borrarUsuario(ValorSesion.getCuenta().getEmail());
                                    //Se ha borrado con éxito
                                    //Nos vamos a pantalla de login
                                    Toast.makeText(getActivity().getApplicationContext(), "Cuenta borrada con exito",
                                            Toast.LENGTH_SHORT).show();
                                    Intent myIntent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(myIntent);
                                    getActivity().finish();
                                }
                                catch(ServerException ex){
                                    switch (ex.getCode()) {

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
        });
        return rootView;
    }
}

