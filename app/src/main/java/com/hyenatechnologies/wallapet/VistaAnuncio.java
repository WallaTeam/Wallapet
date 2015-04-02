package com.hyenatechnologies.wallapet;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Muestra un anuncio. Recibe por "intent" el id del anuncio a mostrar.
 * Si no recibe nada, muestra el 1.
 */
public class VistaAnuncio extends ActionBarActivity {

    //Variables
    TextView anuncioId;
    TextView anuncioEmail;
    TextView anuncioEstado;
    TextView anuncioDescripcion;
    TextView anuncioTipo;
    TextView anuncioPrecio;
    TextView anuncioTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Dibuja el layout en ventana
        setContentView(R.layout.activity_vista_anuncio);

        //Estas dos lineas siguientes son para permitir el uso de la red
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Declaramos cuadros de texto
        anuncioId = (TextView) findViewById(R.id.anuncioId);
        anuncioEmail = (TextView) findViewById(R.id.anuncioEmail);
        anuncioEstado = (TextView) findViewById(R.id.anuncioEstado);
        anuncioDescripcion = (TextView) findViewById(R.id.anuncioDescripcion);
        anuncioTipo = (TextView) findViewById(R.id.anuncioTipo);
        anuncioPrecio = (TextView) findViewById(R.id.anuncioPrecio);
        anuncioTitulo = (TextView) findViewById(R.id.anuncioTitulo);

        //Obtenemos el id de anuncio que nos han pasado desde el intent.
        //Si no nos han pasado nada, ponemos 1.
        Intent mIntent = getIntent();
        int idAnuncio = mIntent.getIntExtra("idAnuncio", 1);

        //El id del anuncio es real, obtenemos anuncio del servidor
        Anuncio a = Conexiones.getAnuncioById(idAnuncio);
        if(a!=null){
            Toast.makeText(getApplicationContext(), "Anuncio cargado correctamente",
                    Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Error al traer los datos del servidor",
                    Toast.LENGTH_LONG).show();
        }
        //Mostramos el anuncio
        mostrarAnuncio(a);

    }

    /** Muestra el anuncio en pantalla */
    public void mostrarAnuncio (Anuncio a){
        anuncioId.setText("" + a.getIdAnuncio());
        anuncioEmail.setText(a.getEmail());
        anuncioEstado.setText(a.getEstado());
        anuncioDescripcion.setText(a.getDescripcion());
        anuncioTipo.setText(a.getTipoIntercambio());
        anuncioPrecio.setText("" + a.getPrecio() + "€");
        anuncioTitulo.setText(a.getTitulo());
    }
}
