/**
 * Nombre:  PantallaPrincipal.java
 * Version: 1.2
 * Autor: Sergio Soro
 * Fecha: 10-4-2015
 * Descripcion: Este fichero implementa la actividad que soporta a los fragments
 * que tienen un drawer lateral.
 */

package com.hyenatechnologies.wallapet.pantallas;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hyenatechnologies.wallapet.Item_objct;
import com.hyenatechnologies.wallapet.NavigationAdapter;
import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.ValorSesion;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;

import java.util.ArrayList;


public class PantallaPrincipal  extends ActionBarActivity{


    public final static String TAG =
            CrearModificarAnuncioFragment.class.getSimpleName();
    private String[] titulos;
    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private TypedArray NavIcons;
    private NavigationAdapter NavAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private static DrawerLayout NavDrawerLayout;
    private static int idAnuncio = 0;
    FragmentManager fragmentManager;


    @Override
    /**
     * Pre: inflater != null && container != null && savedInstanceState != null.
     * Post: Método por defecto en la creación de vista. Encargado de crear todos
     * los elementos de la pantalla e inicializarlos.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        fragmentManager = getFragmentManager();


        NavDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavList = (ListView) findViewById(R.id.lista);

        //Declaramos el header el caul sera el layout de header.xml
        View header = getLayoutInflater().inflate(R.layout.header, null);

        //Establecemos header
        NavList.addHeaderView(header);

        //Tomamos listado  de imgs desde drawable
        NavIcons = getResources().obtainTypedArray(R.array.navigation_iconos);

        //Tomamos listado  de titulos desde el string-array de los recursos
        titulos = getResources().getStringArray(R.array.nav_options);

        //Listado de titulos de barra de navegacion
        NavItms = new ArrayList<Item_objct>();

        /*Mostramos opcion de perfil*/
        if(ValorSesion.getCuenta() == null) {
            NavItms.add(new Item_objct(titulos[0], NavIcons.getResourceId(0, -1)));
        }
        else{
            NavItms.add(new Item_objct(titulos[0] + " - " +
                    ValorSesion.getCuenta().getUsuario(),
                    NavIcons.getResourceId(0, -1)));
        }

        //Crear
        NavItms.add(new Item_objct(titulos[1], NavIcons.getResourceId(1, -1)));

        //Buscar
        NavItms.add(new Item_objct(titulos[2], NavIcons.getResourceId(2, -1)));

        //Compartir
        NavItms.add(new Item_objct(titulos[3], NavIcons.getResourceId(4, -1)));

        //Cerrar sesión
        NavItms.add(new Item_objct(titulos[4], NavIcons.getResourceId(5, -1)));


        //Declaramos y seteamos adaptador al cual le pasamos el array con los titulos
        NavAdapter= new NavigationAdapter(this,NavItms);
        NavList.setAdapter(NavAdapter);

        mTitle = mDrawerTitle = getTitle();

        //Declaramos el mDrawerToggle y las imgs a utilizar
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                NavDrawerLayout,
                R.drawable.ic_drawer,
                R.string.app_name,
                R.string.hello_world
        ) {

            /** Llamado cuando el drawer se cierra por completo. */
            public void onDrawerClosed(View view) {
               // Log.e("Cerrado completo", "!!");
            }

            /** Llamado cuando el drawer se abre por completo */
            public void onDrawerOpened(View drawerView) {
               // Log.e("Apertura completa", "!!");
            }
        };

        //Establecemos que mDrawerToggle declarado anteriormente sea el DrawerListener
        NavDrawerLayout.setDrawerListener(mDrawerToggle);

        //Establecemos que el ActionBar muestre el Boton Home
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        /*Establecemos la accion al clickear sobre cualquier item del menu.
        De la misma forma que hariamos en una app comun con un listview.*/
        NavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long id) {
                if(position<=0){
                    MostrarFragment(1);
                }
                else{
                    MostrarFragment(position);
                }

            }
        });

        //Cuando la aplicacion cargue por defecto mostrar la opcion "Buscar"
        MostrarFragment(3);

    }

    /* Pre: position >=1
     *Post: Muestra el fragment correspondiente a la posicion indicada en el drawer.
     */
    public void MostrarFragment(int position) {

        Fragment fragment = null;
        switch (position) {
            case 1:
                fragment = new ProfileFragment();
                break;
            case 2:
                fragment = new CrearModificarAnuncioFragment();
                break;
            case 3:
                fragment = new BusquedaAnunciosFragment();
                break;
            case 4:

                //Opción de compartir
                break;
            case 5:
                Conexiones c = new Conexiones(this);
                try{

                    //Se desloguea y envia a pantalla de login
                    c.logout();
                    Toast.makeText(getApplicationContext(), "Sesión cerrada",
                            Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(PantallaPrincipal.this,
                            LoginActivity.class);
                    startActivity(myIntent);
                    finish();
                    break;
                }
                catch(ServerException ex){
                    //Error al desloguear, se indica.
                    Toast.makeText(getApplicationContext(), "Error al hacer logout,"+
                            " no estas logueado?", Toast.LENGTH_SHORT).show();
                }
                break;
            default:

                //si no esta la opcion mostrara un toast y nos mandara a Home
                Toast.makeText(getApplicationContext(), "Opcion " +
                        titulos[position - 1] + " no disponible!",
                        Toast.LENGTH_SHORT).show();
                break;
        }

        //Validamos si el fragment no es nulo
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();

            //el addToBackStack sirve para poder volver atras con boton back
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                    .addToBackStack(null).commit();

            // Actualizamos el contenido segun la opcion elegida
            NavList.setItemChecked(position, true);
            NavList.setSelection(position);

            //Cerramos el menu deslizable
            NavDrawerLayout.closeDrawer(NavList);
        } else {

            //Si el fragment es nulo mostramos un mensaje de error.
            Log.e("Error  ", "MostrarFragment"+position);
        }
    }

    @Override
    /**
     * Pre: cierto
     * Post: Sincroniza el toggle state tras onRestoreInstanceState.
     */
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    /**
     * Pre: newConfig != null
     * Post: Llamado cuando cambia la configuracion
     */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    /**
     * Pre: cierto.
     * Post: manejar el menu de opciones y elementos.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.e("mDrawerToggle pushed", "x");
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    /**
     * Pre: cierto
     * Post: Establece idAnuncio al parametro pasado.
     */
    public void setAnuncioID (int i){
        idAnuncio = i;
    }

    @Override
    /**
     * Pre: cierto
     * Post: ejecutado al pulsar el boton de atras
     */
    public void onBackPressed() {
        if(fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
