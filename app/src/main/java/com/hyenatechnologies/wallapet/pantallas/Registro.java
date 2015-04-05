package com.hyenatechnologies.wallapet.pantallas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyenatechnologies.wallapet.R;


public class Registro extends Fragment {

    public Registro () {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_registro, container, false);
        return rootView;
    }
}