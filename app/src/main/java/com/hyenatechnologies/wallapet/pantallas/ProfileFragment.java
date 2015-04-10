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