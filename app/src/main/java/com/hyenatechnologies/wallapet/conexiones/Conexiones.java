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

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hyenatechnologies.wallapet.Anuncio;
import com.hyenatechnologies.wallapet.Cuenta;
import com.hyenatechnologies.wallapet.DatosLogin;
import com.hyenatechnologies.wallapet.RespuestaRegistro;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
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

    private Context context;
    public Conexiones(Context c){
        this.context = c;
    }
    public static final String API_URL = "http://192.168.1.120:8080/Wallapet/";

    /**
     * Obtiene del servidor un anuncio según ID.
     * En caso de error, lanza una ServerException.
     */
    public  Anuncio getAnuncioById(int id) throws ServerException {

        String json = realizarGET(API_URL + "verAnuncio.do?id=" + id);
        return Anuncio.fromJson(json);

    }

    /**
     * Loguea y devuelve la cuenta. Si ha habido un error lanza una excepcion con el codigo
     * @param dl
     * @return
     * @throws ServerException
     */
    public Cuenta login(DatosLogin dl) throws ServerException{
        String json = realizarPOST(API_URL + "loginUsuario.do", "login", DatosLogin.toJson(dl));
        return Cuenta.fromJson(json);
    }

    public RespuestaRegistro registrar(Cuenta c) throws ServerException{
        String json = realizarPOST(API_URL + "registrarUsuario.do","usuario",Cuenta.toJson(c));
        return RespuestaRegistro.fromJson(json);
    }

    public void logout() throws ServerException{
        realizarGET(API_URL + "logout.do");
    }

    /**
     * Obtiene del servidor una busqueda de anuncios.
     * En caso de error, lanza una ServerException.
     */
    public  List<Anuncio> getAnuncios(String tipo, String especie, String palabras) throws ServerException {
        String json = realizarGET(API_URL + "buscarAnuncios.do?tipoAnuncio=" + tipo + "&" + "especie="
                + especie + "&" + "palabrasClave=" + palabras);
        return Anuncio.fromJsonList(json);
    }

    /**
     * Crea el anuncio <a> en el servidor.
     * En caso de algún error, lanza una ServerException indicando el error.
     */
    public  void createAnuncio(Anuncio a) throws ServerException {
        String json = Anuncio.toJson(a);
        realizarPOST(API_URL + "crearAnuncio.do", "anuncio", json);
    }

    /* Actualiza el anuncio <a> en el servidor.
     *  En caso de algún error, lanza una ServerException indicando el error.
     */
    public  void updateAnuncio(Anuncio a) throws ServerException {
        String json = Anuncio.toJson(a);
        Log.d("Updating anuncio",json);
        realizarPOST(API_URL + "ActualizarAnuncio", "anuncio", json);
    }


    /**
     * Borra el anuncio identificado por <id> del servidor.
     *  En caso de algún error, lanza una ServerException indicando el error.
     */
    public  void deleteAnuncio(int id) throws ServerException {
            realizarPOST(API_URL + "BorrarAnuncio?id=" + id, null, null);
        //Nos da igual el texto de contenido.

    }

    /**
     * Realiza un GET a la URL indicada y devuelve, si el código
     * de la respuesta es 200 OK, el contenido de dicha respuesta.
     * En caso de error de comunicación o de acceso denegado,
     * lanza una ServerException con el codigo de Excepcion.
     * Sacado de StackOverflow.
     */
    public String realizarGET(String url) throws ServerException {

        DefaultHttpClient client = new DefaultHttpClient();

        HttpGet getRequest = new HttpGet(url);
        addJSESSIONID(getRequest);

        try {


            HttpResponse getResponse = client.execute(getRequest);
            parseResponseJSESSION(getResponse);
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
    public String realizarPOST(String url, String claveParam, String valorParam) throws ServerException{
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        addJSESSIONID(httppost);

        try {
            // Verificación de parametros
            if (claveParam!=null && valorParam!=null) {
                //Añadimos el parametro
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair(claveParam, valorParam));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }

            //Ejecutamos el POST
            HttpResponse response = httpclient.execute(httppost);
            parseResponseJSESSION(response);
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


    private  void parseResponseJSESSION(HttpResponse response){

        try {

            Header header = response.getFirstHeader("Set-Cookie");

            String value = header.getValue();
            if (value.contains("JSESSIONID")) {
                int index = value.indexOf("JSESSIONID=");

                int endIndex = value.indexOf(";", index);

                String sessionID = value.substring(
                        index + "JSESSIONID=".length(), endIndex);
                SharedPreferences sharedPref = context.getSharedPreferences("configuracion", Context.MODE_PRIVATE);
                String id= sharedPref.getString("JSESSIONID","0");

                if(id.equalsIgnoreCase(sessionID)){
                    //Nos mantenemos en la misma sesión
                    Log.d("SESION", "La sesion se mantiene");

                }
                else{
                    //Ha cambiado la sesion, la guardamos
                    Log.d("SESION", "New session id: " + sessionID);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("JSESSIONID",sessionID);
                    editor.commit();
                }



            }
        } catch (Exception e) {
        }

    }


    private void addJSESSIONID(HttpRequest httpRequest){

        SharedPreferences sharedPref = context.getSharedPreferences("configuracion", Context.MODE_PRIVATE);
        String id= sharedPref.getString("JSESSIONID","0");
        httpRequest.setHeader("Cookie", "JSESSIONID=" + id);
    }
}
