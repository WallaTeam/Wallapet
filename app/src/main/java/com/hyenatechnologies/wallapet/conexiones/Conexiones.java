/** Copyright (C) 2015 Hyena Technologies
 This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 General Public License as published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.
 You should have received a copy of the GNU General Public License along with this program.
 If not, see http://www.gnu.org/licenses/.
 */
package com.hyenatechnologies.wallapet.conexiones;

import android.util.Log;

import com.hyenatechnologies.wallapet.Anuncio;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
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

    public static final String API_URL = "http://wallapet.com:8080/Wallapet/";

    /**
     * Obtiene del servidor un anuncio según ID.
     * En caso de error, lanza una ServerException.
     */
    public static Anuncio getAnuncioById(int id) throws ServerException {

        String json = realizarGET(API_URL + "verAnuncio.do?id=" + id);
        return Anuncio.fromJson(json);

    }

    /**
     * Crea el anuncio <a> en el servidor.
     * En caso de algún error, lanza una ServerException indicando el error.
     */
    public static void createAnuncio(Anuncio a) throws ServerException {
        String json = Anuncio.toJson(a);
        realizarPost(API_URL + "crearAnuncio.do","anuncio",json);
    }

    /* Actualiza el anuncio <a> en el servidor.
     *  En caso de algún error, lanza una ServerException indicando el error.
     */
    public static void updateAnuncio(Anuncio a) throws ServerException {
        String json = Anuncio.toJson(a);
        Log.d("Isma",json);
        realizarPost(API_URL + "actualizarAnuncio.do", "anuncio", json);
    }


    /**
     * Borra el anuncio identificado por <id> del servidor.
     *  En caso de algún error, lanza una ServerException indicando el error.
     */
    public static void deleteAnuncio(int id) throws ServerException {
            realizarGET(API_URL + "borrarAnuncio.do?id=" + id);
        //Nos da igual el texto de contenido.

    }

    /**
     * Realiza un GET a la URL indicada y devuelve, si el código
     * de la respuesta es 200 OK, el contenido de dicha respuesta.
     * En caso de error de comunicación o de acceso denegado,
     * lanza una ServerException con el codigo de Excepcion.
     * Sacado de StackOverflow.
     */
    public static String realizarGET(String url) throws ServerException {

        DefaultHttpClient client = new DefaultHttpClient();

        HttpGet getRequest = new HttpGet(url);

        try {

            HttpResponse getResponse = client.execute(getRequest);
            final int statusCode = getResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                //No es OK, lanzamos excepcion con el codigo
                Log.w("wallapet",
                        "Error " + statusCode + " for URL " + url);
                throw new ServerException(statusCode);
            } else {
                //Es OK, devolvemos contenido
                HttpEntity getResponseEntity = getResponse.getEntity();
                InputStream is = getResponseEntity.getContent();
                java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
                return s.hasNext() ? s.next() : "";

            }

        } catch (IOException e) {
            //Excepcion, lanzamos 500 server error
            getRequest.abort();
            Log.w("wallapet", "Error for URL " + url, e);
            throw new ServerException(500);
        }


    }


    /**
     * Realiza un POST a la dirección <url> con un parámetro
     * con clave <claveParam> y valor <valorParam>, y devuelve el contenido
     * de la respuesta como cadena si su código es 200 OK.
     * En caso contrario, lanza una excepción indicando el código de error.

     */
    public static String realizarPost(String url,String claveParam, String valorParam) throws ServerException{
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        try {
            //Añadimos el parametro
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair(claveParam, valorParam));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            //Ejecutamos el POST
            HttpResponse response = httpclient.execute(httppost);

            int code = response.getStatusLine().getStatusCode();
            if (code != HttpStatus.SC_OK) {
                throw new ServerException(code);
            }
            else{
                HttpEntity getResponseEntity = response.getEntity();
                InputStream is = getResponseEntity.getContent();
                java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
                return s.hasNext() ? s.next() : "";
            }
        } catch (IOException e) {
            //Error
            Log.d("wallapet", e.getMessage());
            throw new ServerException(500);
        }
    }
}
