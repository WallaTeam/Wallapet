package com.hyenatechnologies.wallapet.pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyenatechnologies.wallapet.Anuncio;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.conexiones.ServerException;

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
    TextView anuncioEspecie;
    Button botonBorrar;
    Button botonActualizar;
    Anuncio actual;
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
        anuncioEspecie = (TextView) findViewById(R.id.anuncioEspecie);
        botonBorrar = (Button) findViewById(R.id.botonBorrar);
        botonActualizar = (Button) findViewById(R.id.botonActualizar);
        //Obtenemos el id de anuncio que nos han pasado desde el intent.
        //Si no nos han pasado nada, ponemos 1.
        Intent mIntent = getIntent();
        int idAnuncio = mIntent.getIntExtra("idAnuncio", 1);

        //El id del anuncio es real, obtenemos anuncio del servidor
        Anuncio a;
        try {
            //El anuncio existe
            a = Conexiones.getAnuncioById(idAnuncio);
            actual = a;
            Toast.makeText(getApplicationContext(), "Anuncio cargado correctamente",
                    Toast.LENGTH_LONG).show();
            //Mostramos el anuncio
            mostrarAnuncio(a);

        } catch (ServerException ex) {
            //El anuncio no existe
            switch (ex.getCode()) {
                case 404:
                    Toast.makeText(getApplicationContext(), "El anuncio indicado no existe",
                            Toast.LENGTH_LONG).show();
                    break;
                case 500:
                    Toast.makeText(getApplicationContext(), "Error al contactar con el servidor",
                            Toast.LENGTH_LONG).show();
                    break;
                case 403:
                    Toast.makeText(getApplicationContext(), "Error de permisos",
                            Toast.LENGTH_LONG).show();
                    break;

            }
            finish();

        }

    //Accion al pulsar el boton de borrar
       botonBorrar.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               //Borramos el anuncio

               try {
                   Conexiones.deleteAnuncio(actual.getIdAnuncio());
                   Toast.makeText(getApplicationContext(), "El anuncio se ha borrado con éxito",
                           Toast.LENGTH_LONG).show();
                   finish();
               }
               catch(ServerException ex){
                   switch (ex.getCode()) {
                       case 404:
                           Toast.makeText(getApplicationContext(), "El anuncio indicado no existe",
                                   Toast.LENGTH_LONG).show();
                           break;
                       case 500:
                           Toast.makeText(getApplicationContext(), "Error al contactar con el servidor",
                                   Toast.LENGTH_LONG).show();
                           break;
                       case 403:
                           Toast.makeText(getApplicationContext(), "Error de permisos",
                                   Toast.LENGTH_LONG).show();
                           break;

                   }
               }

           }
       });

        //Accion al pulsar el boton de actualizar
        botonActualizar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Actualizamos el anuncio
                Intent i = new Intent(getApplicationContext(), CrearModificarAnuncio.class);

                i.putExtra("anuncio",Anuncio.toJson(actual));
                startActivity(i);

            }
        });
    }




    /**
     * Muestra el anuncio en pantalla
     */
    public void mostrarAnuncio(Anuncio a) {
        anuncioId.setText("" + a.getIdAnuncio());
        anuncioEmail.setText(a.getEmail());
        anuncioEstado.setText(a.getEstado());
        anuncioDescripcion.setText(a.getDescripcion());
        anuncioTipo.setText(a.getTipoIntercambio());
        anuncioPrecio.setText("" + a.getPrecio() + "€");
        anuncioTitulo.setText(a.getTitulo());
        anuncioEspecie.setText(a.getEspecie());
    }
}
