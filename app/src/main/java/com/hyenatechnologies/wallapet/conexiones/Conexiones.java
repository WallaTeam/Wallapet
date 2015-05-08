/*
* Nombre: Conexiones.java
* Version: 1.0
* Autor: Ismael Rodriguez
* Fecha: 15­4­2015
* Descripcion: Este fichero implementa las funciones de conexion con el servidor.
*/
package com.hyenatechnologies.wallapet.conexiones;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hyenatechnologies.wallapet.objetosDeDatos.Anuncio;
import com.hyenatechnologies.wallapet.objetosDeDatos.Cuenta;
import com.hyenatechnologies.wallapet.objetosDeDatos.DatosLogin;
import com.hyenatechnologies.wallapet.objetosDeDatos.RespuestaRegistro;

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
 *Clase que contiene los métodos para realizar operaciones con el servidor Web.
 */

public class Conexiones {

    private Context context;
    public Conexiones(Context c){
        this.context = c;
    }
    public static final String API_URL = "http://10.1.29.116:8080/Wallapet/";

    /*
     * Pre: id >= 0
     * Post: Devuelve el anuncio que tiene como identificador idAnuncio si existe.
     * En caso de que no existe o haya un error de servidor,
     * lanzará una ServerException.
     */
    public  Anuncio getAnuncioById(int id) throws ServerException {

        String json = realizarGET(API_URL + "verAnuncio.do?id=" + id);
        return Anuncio.fromJson(json);

    }

    /*
     * Pre: dl != null y contiene los datos de login a verificar.
     * Post: Loguea al usuario indicado con la contraseña indicada y devuelve el
     * objeto Cuenta correspondiente al usuario logueado. Si ha habido un error
     * lanza una excepción ServerException.
     */
    public Cuenta login(DatosLogin dl) throws ServerException{
        String json = realizarPOST(API_URL + "loginUsuario.do", "login",
                DatosLogin.toJson(dl));
        return Cuenta.fromJson(json);
    }

    /*
     * Pre: c!=null
     * Post: Registra al usuario indicado en el sistema, devolviendo un objeto
     * RespuestaRegistro que indica si se ha registrado correctamente, o hay algún
     * dato que ya está registrado (DNI, mail o nick). Si se produce algún error de
     * servidor, o falta algún dato por rellenar, lanza una ServerException.
     */
    public RespuestaRegistro registrar(Cuenta c) throws ServerException{
        String json = realizarPOST(API_URL + "registrarUsuario.do", "usuario",
                Cuenta.toJson(c));
        return RespuestaRegistro.fromJson(json);
    }

    /*
    * Pre: ninguno
    * Post: Desloguea al usuario logueado. Si se produce un error del servidor o no
    * había usuario logueado, lanza una ServerException.
    */
    public void logout() throws ServerException{
        realizarGET(API_URL + "logout.do");
    }

    /*
    * Pre: mail != null
    * Post: Borra el usuario indicado sí y sólo si el usuario que está logueado es el
    * mismo que la cuenta indicada. Lanza ServerException en caso de que no sea así o
    * error del servidor.
    */
    public void borrarUsuario(String mail) throws ServerException{
        realizarGET(API_URL + "borrarUsuario.do?mail=" + mail );
    }

    /*
     * Pre: id >= 0
    * Post: Cierra el anuncio indicado si existe y es propiedad del usuario logueado.
    * Si no es así o se produce un error en el servidor, lanza una ServerException.
    */
    public void cerrarAnuncio(int id) throws ServerException{
        realizarPOST(API_URL + "cerrarAnuncio.do","id","" + id);
    }

     /*
     * Pre: tipo, especie y palabras != null
    * Post: Obtiene y devuelve del servidor el resultado de una búsqueda de anuncios
    * definidos por el tipo, especie y palabras siempre que no sean cadenas vacías.
    * Si ocurre algún error lanza una ServerException.
    */
    public  List<Anuncio> getAnuncios(String tipo, String especie, String palabras)
            throws ServerException {

        String URL = API_URL + "buscarAnuncios.do?";
        if(tipo.length()> 0){
            URL+="tipoAnuncio=" + tipo + "&";
        }
        if(especie.length()>0){
            URL+="especie=" + especie + "&";
        }
        if(palabras.length()>0){
            URL+="palabrasClave=" + palabras;
        }
        String json = realizarGET(URL);
        return Anuncio.fromJsonList(json);
    }


    /*
   * Pre: a!=null
  * Post: Crea el anuncio indicado, con el estado "abierto" y con el "mail" del
  * usuario logueado.
  * Si hay un error, se lanza ServerException.
  */
    public  void createAnuncio(Anuncio a) throws ServerException {
        String json = Anuncio.toJson(a);
        realizarPOST(API_URL + "crearAnuncio.do", "anuncio", json);
    }
    /*
      * Pre: a!=null
     * Post: Actualiza el anuncio <a> en el servidor, salvo el mail del creador y el
     * estado del anuncio.
     * En caso de algún error, lanza una ServerException, indicando el error
     */
    public  void updateAnuncio(Anuncio a) throws ServerException {
        String json = Anuncio.toJson(a);
        Log.d("Updating anuncio",json);
        realizarPOST(API_URL + "ActualizarAnuncio", "anuncio", json);
    }



     /*
   * Pre: id >= 0
  * Post: Borra el anuncio con id indicado si el usuario creador es el logueado.
  * En caso contrario o en caso de otro error, lanza ServerException
  */
    public  void deleteAnuncio(int id) throws ServerException {
            realizarPOST(API_URL + "BorrarAnuncio?id=" + id, null, null);
    }

    /**
     * Pre: url != null
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
     * Pre: url!=null, claveParam!=null y claveParam.length > 0,valorParam!=null y
     * valorParam.length >0
     * Realiza un POST a la dirección <url> con un parámetro
     * con clave <claveParam> y valor <valorParam>, y devuelve el contenido
     * de la respuesta como cadena si su código es 200 OK.
     * En caso contrario, lanza una excepción indicando el código de error.

     */
    public String realizarPOST(String url, String claveParam, String valorParam)
            throws ServerException{

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


    /**
     * Pre: response es una respuesta de POST o GET
     * Post: Comprueba que la sesión actual sigue teniendo vigor.
     * Si no, la actualiza.
     */
    private  void parseResponseJSESSION(HttpResponse response){

        try {

            Header header = response.getFirstHeader("Set-Cookie");

            String value = header.getValue();
            if (value.contains("JSESSIONID")) {
                int index = value.indexOf("JSESSIONID=");

                int endIndex = value.indexOf(";", index);

                String sessionID = value.substring(
                        index + "JSESSIONID=".length(), endIndex);
                SharedPreferences sharedPref =
                        context.getSharedPreferences("configuracion",
                                Context.MODE_PRIVATE);
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


    /**
     * Pre: httpRequest es una petición HTTP POST o GET
     * Post: añade el JsessionID a la petición POST o GET para identificar sesión.
     */
    private void addJSESSIONID(HttpRequest httpRequest){

        SharedPreferences sharedPref =
                context.getSharedPreferences("configuracion", Context.MODE_PRIVATE);
        String id= sharedPref.getString("JSESSIONID","0");
        httpRequest.setHeader("Cookie", "JSESSIONID=" + id);
    }
}
