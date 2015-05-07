/**
 * Nombre:  RegistroActivity.java
 * Version: 1.0
 * Autor: Ismael Rodriguez
 * Fecha: 5-5-2015
 * Descripcion: Este fichero implementa la actividad de registrar usuario.
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
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;
import com.hyenatechnologies.wallapet.objetosDeDatos.Cuenta;
import com.hyenatechnologies.wallapet.objetosDeDatos.RespuestaRegistro;

public class RegistroActivity extends ActionBarActivity {

    EditText txtUserName;
    EditText txtEmail;
    EditText txtPass;
    EditText txtDNI;
    EditText txtNombre;
    EditText txtApellidos;
    EditText txtTelefono;
    EditText txtDireccion;
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
        setContentView(R.layout.activity_registro);

        //Estas dos lineas siguientes son para permitir el uso de la red
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        conexiones = new Conexiones(this);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPass = (EditText) findViewById(R.id.txtPass);
        txtDNI = (EditText) findViewById(R.id.txtDNI);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellidos = (EditText) findViewById(R.id.txtApellidos);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        btnRegister = (Button) findViewById(R.id.btnRegister);


        /** Listener del boton de registro. Intenta registrar al usuario.
         * Si se produce algun error, lo indica.
         * Si se registra correctamente, envia a pantalla de login.
         */
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(comprobarCampos()){

                    Cuenta c = new Cuenta();
                    c.setUsuario(txtUserName.getText().toString());
                    c.setEmail(txtEmail.getText().toString());
                    c.setContrasegna(txtPass.getText().toString());
                    c.setDNI(txtDNI.getText().toString());
                    c.setNombre(txtNombre.getText().toString());
                    c.setApellido(txtApellidos.getText().toString());
                    c.setDireccion(txtDireccion.getText().toString());
                    if(!txtTelefono.getText().toString().equals("")) {
                        c.setTelefono(Integer.parseInt(txtTelefono.getText().
                                toString()));
                    }

                    try {

                        RespuestaRegistro rr = conexiones.registrar(c);
                        String codigo = rr.getRespuestaRegistro();
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

                    } catch (ServerException ex) {
                        switch (ex.getCode()) {

                            case 500:
                                Toast.makeText(getApplicationContext(),
                                        "Error al contactar con el servidor",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(),
                                        "Error al contactar con el servidor",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Por favor rellene todos los campos",
                            Toast.LENGTH_SHORT).show();
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

        boolean a =txtUserName.getText().toString().length() > 0;
        boolean b =txtEmail.getText().toString().length() > 0;
        boolean c =txtPass.getText().toString().length() > 0;
        boolean d =txtDNI.getText().toString().length() > 0;
        boolean e =txtNombre.getText().toString().length() > 0;
        boolean f =txtApellidos.getText().toString().length() > 0;
        boolean g =txtDireccion.getText().toString().length() > 0;
        boolean h = txtTelefono.getText().toString().length() > 0;
        return a&&b&&c&&d&&e&&f&&g&&h;

    }
}
