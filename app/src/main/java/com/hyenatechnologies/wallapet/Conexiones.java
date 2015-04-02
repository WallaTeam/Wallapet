package com.hyenatechnologies.wallapet;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que contiene los métodos para realizar operaciones con el servidor Web.
 */
public class Conexiones {

    public static final String API_URL = "http://ismaro3.ddns.net:8080/WallaPet/";

    /**
     * Obtiene del servidor un anuncio según ID.
     * USA DATOS REALES.
     * Devuelve null si ha habido un error
     */
    public static Anuncio getAnuncioById(int id) {
        InputStream textoRecibido = retrieveStream(API_URL + "verAnuncio.do?id=" + id);
        if (textoRecibido != null) {
            String json = convertStreamToString(textoRecibido);
            return Anuncio.fromJson(json);
        } else return null;
    }

    /**
     * Crea el anuncio <a> en el servidor, devolviendo Cons.SUCCESS si éxito
     * ó Cons.FAIL si error.
     * USA DATOS REALES.
     */
    public static int createAnuncio(Anuncio a) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(API_URL + "crearAnuncio.do");
        String json = Anuncio.toJson(a);
        try {
            //Añadimos el parametro "anuncio" con el anuncio en JSON
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("anuncio", json));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            //Ejecutamos el POST
            HttpResponse response = httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
           //Error
            Log.d("wallapet", e.getMessage());
            return Cons.ERROR;
        } catch (IOException e) {
            //Error
            Log.d("wallapet", e.getMessage());
            return Cons.ERROR;
        }
        return Cons.SUCCESS;
    }

    /* Actualiza el anuncio <a> en el servidor, devolviendo Cons.SUCCESS si éxito
     * ó Cons.FAIL si error.
     * DE MOMENTO NO HACE NADA
     * */
    public static int updateAnuncio(Anuncio a) {
        //Actualiza el anuncio en la base de datos y devolverá el codigo q corresponda
        return Cons.SUCCESS;
    }

    /**
     * Borra de la base de datos el anuncio con id <id> y devuelve Cons.SUCCESS ó
     * Cons.FAIL.
     * DE MOMENTO NO HACE NADA
     * @param id
     * @return
     */
    public static int deleteAnuncio(int id) {
        return Cons.SUCCESS;
    }

    /**
     * Devuelve como InputStream el resultado de un get http.
     * Sacado de Stack Overflow.
     * @param url
     * @return
     */
    public static InputStream retrieveStream(String url) {

        DefaultHttpClient client = new DefaultHttpClient();

        HttpGet getRequest = new HttpGet(url);

        try {

            HttpResponse getResponse = client.execute(getRequest);
            final int statusCode = getResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w("wallapet",
                        "Error " + statusCode + " for URL " + url);
                return null;
            }

            HttpEntity getResponseEntity = getResponse.getEntity();
            return getResponseEntity.getContent();

        } catch (IOException e) {
            getRequest.abort();
            Log.w("wallapet", "Error for URL " + url, e);
        }

        return null;

    }

    /**
     * Convierte un InputStream en String.
     * Sacado de StackOverFlow.
     * @param is
     * @return
     */
    static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
