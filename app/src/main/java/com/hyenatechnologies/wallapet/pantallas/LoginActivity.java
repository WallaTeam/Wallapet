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

import com.hyenatechnologies.wallapet.Cuenta;
import com.hyenatechnologies.wallapet.DatosLogin;
import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;

public class LoginActivity extends ActionBarActivity {

    EditText txtEmail;
    EditText txtPass;
    Button btnLogin;
    Button btnRegister;
    Conexiones conexiones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        conexiones = new Conexiones(this);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPass = (EditText) findViewById(R.id.txtPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegistro);
        //Estas dos lineas siguientes son para permitir el uso de la red
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Rellenamos cajas con valores guardados de ultimo login exitoso
        SharedPreferences sharedPref = this.getSharedPreferences("configuracion", Context.MODE_PRIVATE);
        txtEmail.setText(sharedPref.getString("usuario", ""));
        txtPass.setText(sharedPref.getString("pass", ""));


        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(LoginActivity.this, RegistroActivity.class);

               startActivity(myIntent);
                //finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatosLogin dl = new DatosLogin();
                dl.setMail(txtEmail.getText().toString());
                dl.setPass(txtPass.getText().toString());
                try {
                    Cuenta c = conexiones.login(dl);
                    Toast.makeText(getApplicationContext(), "Autentificacion correcta. Bienvenido " + c.getNombre() + " " + c.getApellido(),
                            Toast.LENGTH_LONG).show();

                    //Si login exitoso, guardamos login para proxima vez
                    SharedPreferences sharedPref = getSharedPreferences("configuracion", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("usuario", txtEmail.getText().toString());
                    editor.putString("pass", txtPass.getText().toString());
                    editor.commit();

                    Intent myIntent = new Intent(LoginActivity.this, PantallaPrincipal.class);

                    startActivity(myIntent);
                } catch (ServerException ex) {
                    switch (ex.getCode()) {

                        case 500:
                            Toast.makeText(getApplicationContext(), "Error al contactar con el servidor",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case 403:
                            Toast.makeText(getApplicationContext(), "Usuario o pass incorrectos",
                                    Toast.LENGTH_LONG).show();
                            break;


                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
