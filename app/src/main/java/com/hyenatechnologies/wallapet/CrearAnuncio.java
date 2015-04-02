package com.hyenatechnologies.wallapet;

<<<<<<< HEAD
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
=======
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
>>>>>>> d5911b22822462a1e7377b48bcb3f3eb1e51c4ee
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Pantalla de crear un nuevo anuncio
 */
public class CrearAnuncio extends ActionBarActivity {


    //Variables
    EditText titulo;
    EditText email;
    EditText descripcion;
    EditText estado;
    EditText tipo;
    EditText especie;
    EditText precio;
    Button botonCrear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio);

        //Cargamos campos de texto
        titulo = (EditText) findViewById(R.id.crearAnuncioTitulo);
        email = (EditText) findViewById(R.id.crearAnuncioEmail);
        descripcion = (EditText) findViewById(R.id.crearAnuncioDescripcion);
        estado = (EditText) findViewById(R.id.crearAnuncioEstado);
        tipo = (EditText) findViewById(R.id.crearAnuncioTipo);
        precio = (EditText) findViewById(R.id.crearAnuncioPrecio);
        especie = (EditText) findViewById(R.id.crearAnuncioEspecie);

        //Cargamos boton
        botonCrear = (Button) findViewById(R.id.crearAnuncioOK);

        //Estas dos lineas siguientes son para permitir el uso de la red
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Establecemos que hace el boton al ser pulsado
        botonCrear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Recogemos datos de los campos de texto
                Anuncio a = new Anuncio();
                a.setTitulo(titulo.getText().toString());
                a.setEmail(email.getText().toString());
                a.setDescripcion(descripcion.getText().toString());
                a.setEstado(estado.getText().toString());
                a.setEspecie(especie.getText().toString());
                a.setTipoIntercambio(tipo.getText().toString());
                a.setPrecio(Double.parseDouble(precio.getText().toString()));

                //Guardamos el anuncio
                int exito = Conexiones.createAnuncio(a);
                if (exito == Cons.SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Anuncio creado correctamente",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error al crear el anuncio",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crear_anuncio, menu);
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
