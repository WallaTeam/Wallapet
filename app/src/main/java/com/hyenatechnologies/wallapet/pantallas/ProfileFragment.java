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

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hyenatechnologies.wallapet.R;

/*  Fragment para seccion perfil */
public class ProfileFragment extends Fragment {

    Button boton;
    EditText texto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        rootView = inflater.inflate(R.layout.profile, container, false);

        boton = (Button) rootView.findViewById(R.id.verAnuncioBoton);
        texto = (EditText) rootView.findViewById(R.id.verAnuncio);

        /** al pulsar el boton se lanza el fragment de ver anuncio
        boton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!texto.getText().toString().equals("")) {
                    int id = Integer.parseInt(texto.getText().toString());
                    FragmentManager fragmentManager = getFragmentManager();

                    //Paso 2: Crear una nueva transacción
                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                    //Paso 3: Crear un nuevo fragmento y añadirlo
                    VistaAnuncioFragment vista = new VistaAnuncioFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("anuncio",id);
                    vista.setArguments(bundle);
                    transaction.replace(R.id.content_frame, vista);
                    transaction.addToBackStack(null);
                    //Paso 4: Confirmar el cambio
                    transaction.commit();

                }

            }
        });
        */
        return rootView;
    }
}