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

import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.ViewGroupUtils;

/**
 * Fragment que implementa el formulario de login.
 */
public class LoginFragment extends Fragment {

    public LoginFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        ((PantallaPrincipal) getActivity()).setTitle("Login");
        final View rootView = inflater.inflate(R.layout.activity_login, container, false);
        final View newView = inflater.inflate(R.layout.activity_registro, container, false);
        final Button button = (Button) rootView.findViewById(R.id.btnRegistro);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ViewGroupUtils.replaceView(rootView,newView);
                ((PantallaPrincipal) getActivity()).setTitle("Registro");
            }
        });
        return rootView;
    }
}
