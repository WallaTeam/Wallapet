package com.hyenatechnologies.wallapet.pantallas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyenatechnologies.wallapet.R;

/**
 * Fragmento que implementa el formulario de registro
 */
public class RegistroFragment extends Fragment {

    public RegistroFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        ((PantallaPrincipal) getActivity()).setTitle("Registro");
        View rootView = inflater.inflate(R.layout.activity_registro, container, false);
        return rootView;
    }
}
