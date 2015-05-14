/**
 * Nombre:  RegistroActivity.java
 * Version: 1.0
 * Autor: Ismael Rodriguez
 * Fecha: 5-5-2015
 * Descripcion: Este fichero implementa la actividad de registrar usuario.
 */

package com.hyenatechnologies.wallapet.pantallas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;
import com.hyenatechnologies.wallapet.objetosDeDatos.Cuenta;
import com.hyenatechnologies.wallapet.objetosDeDatos.RespuestaRegistro;

public class RegistroActivity extends ActionBarActivity {

    private EditText txtUserName;
    private EditText txtEmail;
    private EditText txtPass;
    private EditText txtDNI;
    private EditText txtNombre;
    private EditText txtApellidos;
    private EditText txtTelefono;
    private EditText txtDireccion;
    private Button btnRegister;
    Conexiones conexiones;

    @Override
    /**
     * Pre: inflater != null && container != null && savedInstanceState != null.
     * Post: M�todo por defecto en la creaci�n de vista. Encargado de crear todos
     * los elementos de la pantalla e inicializarlos.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        conexiones = new Conexiones(this);

        txtUserName = (EditText) findViewById(R.id.txtUserName);

        txtUserName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                ValidacionRegistro.hasText(txtUserName);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });

        txtEmail = (EditText) findViewById(R.id.txtEmail);

        txtEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                ValidacionRegistro.validoEmailAddress(txtEmail,true);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });

        txtPass = (EditText) findViewById(R.id.txtPass);

        txtPass.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                ValidacionRegistro.validoPASS(txtPass,true);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });

        txtDNI = (EditText) findViewById(R.id.txtDNI);

        txtDNI.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                ValidacionRegistro.validoDNI(txtDNI,true);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });

        txtNombre = (EditText) findViewById(R.id.txtNombre);

        txtNombre.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                ValidacionRegistro.hasText(txtNombre);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });

        txtApellidos = (EditText) findViewById(R.id.txtApellidos);

        txtApellidos.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                ValidacionRegistro.hasText(txtApellidos);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });

        txtDireccion = (EditText) findViewById(R.id.txtDireccion);

        txtDireccion.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                ValidacionRegistro.hasText(txtDireccion);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });

        txtTelefono = (EditText) findViewById(R.id.txtTelefono);

        txtTelefono.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable editable) {
                ValidacionRegistro.validoTelefono(txtTelefono,true);
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });

        btnRegister = (Button) findViewById(R.id.btnRegister);


        /** Listener del boton de registro. Intenta registrar al usuario.
         * Si se produce algun error, lo indica.
         * Si se registra correctamente, envia a pantalla de login.
         */
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            if (comprobarCampos()) {
                new RegistroTask().execute("");
            }
            else{
                //Mostrar toast de rellene todos los campos
                RegistroActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(RegistroActivity.this.
                                        getApplicationContext(),
                                "Por favor, revise todos los campos",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            }
        });
    }

    @Override
    /**
     * Pre: menu!= null
     * Puebla el menu contextual, en este caso vacio.
     */
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    /**
     * Pre: item!=null
     * Post: controla acciones al pulsar menu contextual, en este caso no hace
     * nada pues no hay objetos en dicho menu.
     */
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    /**
     *Pre: cierto
     * Post: Devuelve cierto si y solo si todos los campos estan rellenados.
     * Falso en caso contrario.
     */

    private boolean comprobarCampos(){
        boolean ret = true;

        if(!ValidacionRegistro.hasText(txtUserName)) ret = false;
        if(!ValidacionRegistro.validoEmailAddress(txtEmail,true)) ret = false;
        if(!ValidacionRegistro.validoPASS(txtPass,true)) ret = false;
        if(!ValidacionRegistro.validoDNI(txtDNI,true)) ret = false;
        if(!ValidacionRegistro.hasText(txtNombre)) ret = false;
        if(!ValidacionRegistro.hasText(txtApellidos)) ret = false;
        if(!ValidacionRegistro.hasText(txtDireccion)) ret = false;
        if(!ValidacionRegistro.validoTelefono(txtTelefono,true)) ret = false;

        return ret;

    }

    /**
     * Clase que se encarga de registrar un usuario en segundo plano
     */
    private class RegistroTask extends AsyncTask<String, Void, RespuestaRegistro> {

        private ProgressDialog dialogo;

        public RegistroTask() {
            super();
            dialogo = new ProgressDialog(RegistroActivity.this);
        }

        /**
         * Pre: cierto
         * Post: muestra el dialogo de registrando.
         */
        protected void onPreExecute() {
            dialogo.setMessage("Registrando...");
            dialogo.show();
        }


        @Override
        /**
         * Pre: ninguno
         * Post: Registra al nuevo usuario en background
         * */
        protected RespuestaRegistro doInBackground(String... urls) {

                //Si los campos estan bien, procedemos
                Cuenta c = new Cuenta();
                c.setUsuario(txtUserName.getText().toString());
                c.setEmail(txtEmail.getText().toString());
                c.setContrasegna(txtPass.getText().toString());
                c.setDNI(txtDNI.getText().toString());
                c.setNombre(txtNombre.getText().toString());
                c.setApellido(txtApellidos.getText().toString());
                c.setDireccion(txtDireccion.getText().toString());
                if (!txtTelefono.getText().toString().equals("")) {
                    c.setTelefono(Integer.parseInt(txtTelefono.getText().
                            toString()));
                }
                try {
                    RespuestaRegistro rr = conexiones.registrar(c);
                    return rr;
                } catch (ServerException ex) {
                    //Error de conexion
                    switch (ex.getCode()) {

                        case 500:
                            RegistroActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Error al contactar con el servidor",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        default:
                            Toast.makeText(getApplicationContext(),
                                    "Error al contactar con el servidor",
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return null;
                }


        }


        /**
         * Pre: cierto
         * Post: actualiza la UI cuando se ha registrado el usuario
         */ @Override

        protected void onPostExecute(RespuestaRegistro result) {

            if (dialogo.isShowing()) {
                dialogo.dismiss();
            }
            if(result!=null) {

                //Ha habido resultado
                String codigo = result.getRespuestaRegistro();
                switch(codigo){
                    case "mail_o_DNI_o_nick_repetido":
                        Toast.makeText(getApplicationContext(),
                                "Mail o DNI en uso",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case "OK":
                        Toast.makeText(getApplicationContext(),
                                "Registro correcto",
                                Toast.LENGTH_SHORT).show();

                        //Si registro exitoso, guardamos login
                        SharedPreferences sharedPref =
                                getSharedPreferences("configuracion",
                                        Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("usuario",
                                txtEmail.getText().toString());
                        editor.putString("pass",
                                txtPass.getText().toString());
                        editor.commit();

                        //Se lanza actividad de commit
                        Intent myIntent = new Intent(RegistroActivity.this,
                                LoginActivity.class);
                        startActivity(myIntent);
                        finish();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),
                                "Respuesta desconocida del servidor",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        }


    }
}
