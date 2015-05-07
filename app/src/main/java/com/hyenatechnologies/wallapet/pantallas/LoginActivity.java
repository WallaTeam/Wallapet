/**
 * Nombre:  LoginActivity.java
 * Version: 2.0
 * Autor: Raúl Piracés, Ismael Rodriguez, David Vergara.
 * Fecha: 10-4-2015
 * Descripcion:Este fichero implementa la actividad de login.
 */
package com.hyenatechnologies.wallapet.pantallas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.clasesEstaticas.ValorSesion;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;
import com.hyenatechnologies.wallapet.objetosDeDatos.Cuenta;
import com.hyenatechnologies.wallapet.objetosDeDatos.DatosLogin;

public class LoginActivity extends ActionBarActivity {

    EditText txtEmail;
    EditText txtPass;
    Button btnLogin;
    Button btnRegister;
    Conexiones conexiones;

    @Override
    /**
     * Pre: inflater != null && container != null && savedInstanceState != null.
     * Post: Método por defecto en la creación de vista. Encargado de crear todos
     * los elementos de la pantalla e inicializarlos.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Estas dos lineas siguientes son para permitir el uso de la red
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        conexiones = new Conexiones(this);

        //Inicializamos componentes de vista
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPass = (EditText) findViewById(R.id.txtPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegistro);

        //Rellenamos cajas con valores guardados de ultimo login exitoso
        SharedPreferences sharedPref =
                this.getSharedPreferences("configuracion", Context.MODE_PRIVATE);
        txtEmail.setText(sharedPref.getString("usuario", ""));
        txtPass.setText(sharedPref.getString("pass", ""));


        /**
         * Click listener del boton de registro. Abre actividad de registro.
         */
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(LoginActivity.this,
                        RegistroActivity.class);

               startActivity(myIntent);
            }
        });

        /**
         * Click listener del boton de login. Obtiene los datos introducidos por el
         * cliente e intenta loguearse con ellos, devolviendo un error en caso de que
         * no haya habido exito, o pasando a la pantalla de busqueda si lo ha habido.
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatosLogin dl = new DatosLogin();
                dl.setMail(txtEmail.getText().toString());
                dl.setPass(txtPass.getText().toString());
                try {
                    Cuenta c = conexiones.login(dl);
                    ValorSesion.setCuenta(c);
                    Toast.makeText(getApplicationContext(),
                            "Autentificacion correcta. Bienvenido " +
                                    c.getNombre() + " " + c.getApellido(),
                            Toast.LENGTH_SHORT).show();

                    //Si login exitoso, guardamos login para proxima vez
                    SharedPreferences sharedPref = getSharedPreferences(
                            "configuracion", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("usuario", txtEmail.getText().toString());
                    editor.putString("pass", txtPass.getText().toString());
                    editor.commit();

                    //Vamos a pantalla principal, que sera la de busqueda
                    Intent myIntent = new Intent(LoginActivity.this,
                            PantallaPrincipalActivity.class);
                    startActivity(myIntent);
                } catch (ServerException ex) {
                    switch (ex.getCode()) {
                        case 500:
                            Toast.makeText(getApplicationContext(),
                                    "Error al contactar con el servidor",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
                            Toast.makeText(getApplicationContext(),
                                    "Usuario o pass incorrectos",
                                    Toast.LENGTH_SHORT).show();
                            break;

                    }
                }
            }
        });
    }

    @Override
    /**
     * Pre: menu!=null
     * Post: Infla los objetos del menu para usarse en el action bar.
     * En esta pantalla no hay.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    /**
     * Pre: item!=null
     * Post: Controla si se ha pulsado una opcion de menu. En
     * esta pantalla no hay.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
