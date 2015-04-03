package com.hyenatechnologies.wallapet.pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyenatechnologies.wallapet.Anuncio;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.conexiones.ServerException;


/**
 * Pantalla de crear un nuevo anuncio o modificar uno existente
 */
public class CrearModificarAnuncio extends ActionBarActivity {

    private static final int MODO_CREAR = 1;
    private static final int MODO_ACTUALIZAR = 2;


    //Variables
    EditText titulo;
    EditText email;
    EditText descripcion;
    EditText estado;
    EditText tipo;
    EditText especie;
    EditText precio;
    Button botonCrear;
    int modo = MODO_CREAR;
    Anuncio modificando;
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


        //Si en el intent hay un anuncio JSON con nombre "anuncio",
        //es que estamos en modo actualizar. Si no, modo crear.
        Intent mIntent = getIntent();
        String jsonAnuncio = mIntent.getStringExtra("anuncio");


        if(jsonAnuncio == null){
            modo = MODO_CREAR;
            botonCrear.setText("Crear anuncio");
            //No nos pasan anuncio, nos piden crear
        }
        else{
            modo = MODO_ACTUALIZAR;
            botonCrear.setText("Actualizar anuncio");
            //Nos pasan anuncio, modificamos
            Anuncio a = Anuncio.fromJson(jsonAnuncio);
            modificando = a;
            mostrarAnuncio(a);

        }


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
                try{
                    if(modo == MODO_CREAR){
                            //Modo crear
                        Conexiones.createAnuncio(a);
                        Toast.makeText(getApplicationContext(), "Anuncio creado correctamente",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else if(modo == MODO_ACTUALIZAR){
                        //Modo actualizar, tenemos q poner el id del anuncio a modificar
                        a.setIdAnuncio(modificando.getIdAnuncio());
                        Conexiones.updateAnuncio(a);
                        Toast.makeText(getApplicationContext(), "Anuncio actualizado correctamente",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }

                }
                catch(ServerException ex){
                    switch (ex.getCode()){

                        case  500:
                            Toast.makeText(getApplicationContext(), "Error al contactar con el servidor",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case 403:
                            Toast.makeText(getApplicationContext(), "Error de permisos",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Toast.makeText(getApplicationContext(), "No existe el anuncio indicado.",
                                    Toast.LENGTH_LONG).show();
                            break;

                    }
                    finish();
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

    /**
     * Rellena los campos
     */
    public void mostrarAnuncio(Anuncio a) {

        email.setText(a.getEmail());
       estado.setText(a.getEstado());
        descripcion.setText(a.getDescripcion());
        especie.setText(a.getEspecie());
        tipo.setText(a.getTipoIntercambio());
        precio.setText("" + a.getPrecio());
        titulo.setText(a.getTitulo());
    }

}
