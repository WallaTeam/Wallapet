package com.hyenatechnologies.wallapet;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * WallaPet Android App
 * Hyena Technologies ¢ 2015
 */

/**
 * Actividad principal. De momento permite ejecutar cosas de prueba
 * como ver anuncio por id o crear anuncil.
 */
public class PantallaPrincipal extends ActionBarActivity {


    //Variables
    EditText texto;
    Button botonVer;
    Button botonCrear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        //Cargamos cuadro de texto de id de anuncio
        texto = (EditText) findViewById(R.id.verAnuncio);
        //Cargamos botones
        botonVer = (Button) findViewById(R.id.verAnuncioBoton);
        botonCrear = (Button) findViewById(R.id.crearAnuncioBoton);
        //Establecemos comportamiento de boton de ver anuncio
        botonVer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Lanzamos la actividad de ver
                Intent i = new Intent(getApplicationContext(), VistaAnuncio.class);
                int idAnuncio = 1;
                if (!texto.getText().toString().equals("")) {
                    i.putExtra("idAnuncio", Integer.parseInt(texto.getText().toString()));
                    startActivity(i);
                }
            }
        });
        //establecemos comportamiento de boton de crear anuncio
        botonCrear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Lanzamos la actividad de crear
                Intent i = new Intent(getApplicationContext(), CrearAnuncio.class);

                startActivity(i);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pantalla_principal, menu);
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
