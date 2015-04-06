package com.hyenatechnologies.wallapet.pantallas;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyenatechnologies.wallapet.R;

/**
 * Fragment que implementa el formulario de login.
 */
public class LoginFragment extends Fragment {

    public LoginFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        ((PantallaPrincipal) getActivity()).setTitle("Login");
        View rootView = inflater.inflate(R.layout.activity_login, container, false);
        return rootView;
    }
}
