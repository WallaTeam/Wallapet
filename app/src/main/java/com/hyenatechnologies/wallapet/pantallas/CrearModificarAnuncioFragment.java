/**
 * Nombre:  CrearModificarAnuncioFragment.java
 * Version: 4.5
 * Autor: Raúl Piracés, Ismael Rodriguez, Sergio Soro.
 * Fecha: 6-5-2015
 * Descripcion: Este fichero implementa la pantalla de crear un nuevo anuncio o
 * modificar uno existente.
 */


package com.hyenatechnologies.wallapet.pantallas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hyenatechnologies.wallapet.R;
import com.hyenatechnologies.wallapet.conexiones.Conexiones;
import com.hyenatechnologies.wallapet.conexiones.ServerException;
import com.hyenatechnologies.wallapet.objetosDeDatos.Anuncio;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Random;


/**
 *La clase proporciona la pantalla para la creación y modificación de anuncios,
 * incluyendo los metodos correspondientes a estas acciones.
 */

public class CrearModificarAnuncioFragment extends Fragment {

    private static final int MODO_CREAR = 1;
    private static final int MODO_ACTUALIZAR = 2;

    // Variables asociadas a la cámara.
    // "Activity request codes", para la llamada a aplicaciones externas.
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 200;

    // Nombre del directorio asociado a las imagenes capturadas por Wallapet.
    private static final String IMAGE_DIRECTORY_NAME = "WallapetCamera";
    int modo = MODO_CREAR;
    Anuncio modificando;
    // Variables para el tratamiento de imágenes.
    private Uri fileUri;
    private ImageView imgPreview;
    private Button botonImagen, botonGaleria;
    private String currentImagePath;
    private String currentURL;
    // Variables globales de la clase.
    private EditText titulo;
    private EditText descripcion;
    private Spinner tipo;
    private Spinner especie;
    private EditText precio;
    private Button botonCrear;
    private Conexiones conexiones;
    private TextView lblPrecio;
    private List<String> ListaTipos = new ArrayList<String>();
    private List<String> ListaEspecies = new ArrayList<String>();
    DownloadImageTask imageTask;
    /**
     * Pre: file != null.
     * Post: Devuelve el hash correspondiente al fichero representado por file.
     * El hash devuelto en un string está codificado en SHA1.
     */
    private static String getHash(final File file) throws NoSuchAlgorithmException,
            IOException {
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        Random random = new Random();
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            final byte[] buffer = new byte[1024];
            for (int read = 0; (read = is.read(buffer)) != -1; ) {
                messageDigest.update(buffer, 0, read);
            }
        } catch (Exception ex) {
            Long l = random.nextLong();
            return l.toString();
        }
        // Convert the byte to hex format.
        try {
            Formatter formatter = new Formatter();
            for (final byte b : messageDigest.digest()) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        } catch (Exception ex) {
            Long l = random.nextLong();
            return l.toString();
        }
    }

    /**
     * Pre: file != null.
     * Post: Devuelve la extensión del fichero representado por file.
     */
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    /**
     * Pre: inflater != null && container != null && savedInstanceState != null.
     * Post: Método por defecto en la creación de vista. Encargado de crear todos
     * los elementos de la pantalla e inicializarlos.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_crear_anuncio, container,
                false);

        //Cargamos campos de id_a_cargar.
        titulo = (EditText) rootView.findViewById(R.id.crearAnuncioTitulo);
        tipo = (Spinner) rootView.findViewById(R.id.spinnerCrearAnuncioTipo);
        descripcion = (EditText) rootView.findViewById(R.id.crearAnuncioDescripcion);
        lblPrecio = (TextView) rootView.findViewById(R.id.lblPrecio);


        ListaTipos.add("Adopcion");
        ListaTipos.add("Venta");
        ArrayAdapter<String> adapter2 =
                new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,
                        ListaTipos);
        tipo.setAdapter(adapter2);
        precio = (EditText) rootView.findViewById(R.id.crearAnuncioPrecio);

        especie = (Spinner) rootView.findViewById(R.id.spinnerCrearAnuncioEspecie);
        ListaEspecies.add("Mamiferos");
        ListaEspecies.add("Reptiles");
        ListaEspecies.add("Anfibios");
        ListaEspecies.add("Artropodos");
        ListaEspecies.add("Otros");
        ArrayAdapter<String> adapter3 = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, ListaEspecies);
        especie.setAdapter(adapter3);

        //Cargamos botones.
        botonCrear = (Button) rootView.findViewById(R.id.crearAnuncioOK);
        botonImagen = (Button) rootView.findViewById(R.id.anadirImagen);
        botonGaleria = (Button) rootView.findViewById(R.id.anadirImagen2);
        imgPreview = (ImageView) rootView.findViewById(R.id.imgPreview);
        imgPreview.setVisibility(View.GONE);



        conexiones = new Conexiones(this.getActivity());

        //Si en el intent hay un anuncio JSON con nombre "anuncio",
        //es que estamos en modo actualizar. Si no, modo crear.
        Bundle bundle = this.getArguments();
        String jsonAnuncio = null;
        if (bundle != null) {
            jsonAnuncio = bundle.getString("anuncio");
        }


        if (jsonAnuncio == null) {

            //No nos pasan anuncio, nos piden crear.
            modo = MODO_CREAR;
            ((PantallaPrincipalActivity) getActivity()).setTitle("Crear anuncio");
            botonCrear.setText("Crear anuncio");


        } else {
            modo = MODO_ACTUALIZAR;
            ((PantallaPrincipalActivity) getActivity()).setTitle("Actualizar anuncio");
            botonCrear.setText("Actualizar anuncio");

            //Nos pasan anuncio, modificamos.
            Anuncio a = Anuncio.fromJson(jsonAnuncio);
            modificando = a;
            mostrarAnuncio(a);

            switch(a.getEspecie()){

                case "Mamiferos":
                    especie.setSelection(0);
                    break;
                case "Reptiles":
                    especie.setSelection(1);
                    break;
                case "Anfibios":
                    especie.setSelection(2);
                    break;
                case "Artropodos":
                    especie.setSelection(3);
                    break;
                case "Otros":
                    especie.setSelection(4);
                    break;
                default:
                    especie.setSelection(4);
                    break;
            }
            switch(a.getTipoIntercambio()){
                case "Adopcion":
                    tipo.setSelection(0);
                    break;
                default:
                    tipo.setSelection(1);
            }

        }

        tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id){

                // Si es tipo adopcion,quitamos el campo de precio, si no lo ponemos.
                if (position == 0) {
                    precio.setVisibility(View.GONE);
                    lblPrecio.setVisibility(View.GONE);
                } else {
                    precio.setVisibility(View.VISIBLE);
                    lblPrecio.setVisibility(View.VISIBLE);
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

                // No ocurre nada.
            }

        });
        botonImagen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Captura la imagen correspondiente.
                captureImage();
            }
        });

        botonGaleria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takeFromGallery();
            }
        });
        //Establecemos que hace el boton al ser pulsado.
        botonCrear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
new CreateAnuncioTask().execute("");
            }
        });

        if (modo == MODO_ACTUALIZAR) {
            if (modificando.getRutaImagen() != null &&
                    modificando.getRutaImagen().length() > 0) {
                imageTask = new DownloadImageTask((ImageView) imgPreview);
                        imageTask.execute(modificando.getRutaImagen());
            }
        }
        return rootView;
    }

    /**
     * Pre: cierto.
     * Post: manejar el menu de opciones y elementos.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement.
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Pre: cierto.
     * Post: rellena los campos correspondientes del anuncio.
     */
    public void mostrarAnuncio(Anuncio a) {


        descripcion.setText(a.getDescripcion());
        String especieAnuncio = a.getEspecie();
        if (especieAnuncio.compareTo("Cualquiera") == 0) {
            especie.setSelection(0);
        } else if (especieAnuncio.compareTo("Mamíferos") == 0) {
            especie.setSelection(1);
        } else if (especieAnuncio.compareTo("Reptiles") == 0) {
            especie.setSelection(2);
        } else if (especieAnuncio.compareTo("Anfibios") == 0) {
            especie.setSelection(3);
        } else if (especieAnuncio.compareTo("Artrópodos") == 0) {
            especie.setSelection(4);
        } else if (especieAnuncio.compareTo("Otros") == 0) {
            especie.setSelection(5);
        }
        if (a.getTipoIntercambio().compareTo("Adopcion") == 0) {
            tipo.setSelection(0);
        } else {
            tipo.setSelection(1);
        }
        precio.setText("" + a.getPrecio());
        titulo.setText(a.getTitulo());
    }

    /**
     * Pre: cierto.
     * Post: lanza la acticidad encargada de capturar una imagen (camara) para el
     * anuncio.
     */
    private void captureImage() {
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagesFolder = new File(Environment.getExternalStorageDirectory(),
                IMAGE_DIRECTORY_NAME);
        imagesFolder.mkdirs();
        File image = new File(imagesFolder, "toUpload.jpg");
        fileUri = Uri.fromFile(image);
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(imageIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Pre: cierto.
     * Post: este metodo se encarga de llamar a la actividad correspondiente para
     * coger una imagen de la galeria.
     */
    private void takeFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALLERY_IMAGE_REQUEST_CODE);
    }

    /**
     * Pre: cierto.
     * Post: recibe el resultado de la actividad
     * (sera llamado tras cerrar la camara).
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Si el resultado pertenece a la captura de imagen.
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // vista previa de imagen.
                previewCapturedImage();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // captura cancelada por usuario.
                Toast.makeText(getActivity().getApplicationContext(),
                        "El usuario ha cancelado la captura de imagen.",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                // fallo al capturar la imagen.
                Toast.makeText(getActivity().getApplicationContext(),
                        "¡Fallo al capturar la imagen! Compruebe el estado de la " +
                                "camara y contacte con el desarrollador",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // vista previa de imagen.
                previewGalleryImage(data);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // captura cancelada por usuario.
                Toast.makeText(getActivity().getApplicationContext(),
                        "El usuario ha cancelado la selección de imagen.",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                // fallo al capturar la imagen.
                Toast.makeText(getActivity().getApplicationContext(),
                        "¡Fallo al obtener la imagen!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * Pre: cierto.
     * Post: muestra una determinada imagen desde una ruta interna del dispositivo.
     */
    private void previewCapturedImage() {
        try {

            imgPreview.setVisibility(View.VISIBLE);

            // bimatp factory.
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            imgPreview.setImageBitmap(bitmap);
            currentImagePath = fileUri.getPath();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pre: cierto.
     * Post: muestra una vista previa de la imagen desde la galeria a un ImageView.
     */
    private void previewGalleryImage(Intent data) {
        try {
            imgPreview.setVisibility(View.VISIBLE);
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(picturePath,
                    options);

            imgPreview.setImageBitmap(bitmap);
            currentImagePath = picturePath;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pre: cierto.
     * Post: sube la imagen seleccionada al server mediante una petición POST.
     */
    private void uploadImage() {
     /*
      Crear el objeto de subida.
     */
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;
        String pathToOurFile = currentImagePath;
        String urlServer = "http://wallapet.com/upload.php?submit=";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 25 * 1024 * 1024;

        try {
            File file = new File(pathToOurFile);
            String uploadedfile = getHash(file);
            String ext = getFileExtension(file);
            uploadedfile = uploadedfile + "." + ext;
            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            // Aceptar inputs y outputs.
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // HTTP -> POST.
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data;" +
                    " name=\"uploadedfile\";filename=\"" + uploadedfile + "\"" +
                    lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Leer el fichero.
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Respuesta del servidor (mensaje y codigo).
            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

            if (serverResponseCode != 200) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "¡Fallo al subir la imagen!", Toast.LENGTH_SHORT)
                        .show();
            } else {
                currentURL = "http://wallapet.com/uploads/" + uploadedfile;
            }
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "¡Fallo al subir la imagen! La imagen no puede superar 25MB.",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Pre: cierto.
     * Post: clase que implementa la descarga de imagen correspondiente a la vista y
     * modificación de un anuncio.
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        /**
         * Constructor por defecto de la clase.
         * @param bmImage
         */
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        /**
         * Pre: cierto.
         * Post: metodo en segundo plano encargado de recuperar la imagen.
         * @param urls en la que se encuentra la imagen.
         * @return Bitmap con la imagen descargada.
         */
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }



        /**
         * Pre: cierto.
         * Post: metodo llamado tras la ejecución del metodo de descarga
         * en segundo plano.
         * @param result con la imagen obtenida.
         */
        protected void onPostExecute(Bitmap result) {
            Configuration configuration = getActivity().getResources().
                    getConfiguration();
            int screenWidthDp = configuration.screenWidthDp;
            final float scale = getActivity().getResources().
                    getDisplayMetrics().density;
            int p = (int) (screenWidthDp * scale + 0.5f);

            if (result != null) {
                Bitmap b2 = Bitmap.createScaledBitmap(result, p, p, true);
                bmImage.setVisibility(View.VISIBLE);
                bmImage.setImageBitmap(b2);
            }
        }
    }

    /**
     * Clase que se encarga de creacion y actualizacion de login en background
     */
    private class CreateAnuncioTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

        public CreateAnuncioTask(){
            super();
            dialog = new ProgressDialog(getActivity());
        }

        /**
         * Pre: cierto
         * Post: muestra el dialogo de creando....
         */
        protected void onPreExecute() {
            dialog.setMessage("Creando anuncio...");
            dialog.show();
        }
        @Override
        /**
         * Pre: ninguno
         * Post: Realiza la creacion o actualizacion del anuncio en background
         */
        protected String doInBackground(String... urls) {
        //Recogemos datos de los campos de id_a_cargar.
            Anuncio a = new Anuncio();
            try {

                a.setTitulo(titulo.getText().toString());
                a.setEmail("NO IMPORTA");
                a.setDescripcion(descripcion.getText().toString());
                a.setEstado("NO IMPORTA");
                a.setEspecie(especie.getSelectedItem().toString());
                a.setTipoIntercambio(tipo.getSelectedItem().toString());
                if (tipo.getSelectedItemPosition() == 1) {
                    a.setPrecio(Double.parseDouble(precio.getText().toString()));
                } else {
                    //Es de adopcion, da igual el precio.
                    a.setPrecio(0.0);
                }


                if (currentImagePath != null && currentImagePath.length() != 0) {
                    uploadImage();
                    a.setRutaImagen(currentURL);
                } else if (modo == MODO_ACTUALIZAR) {
                    a.setRutaImagen(modificando.getRutaImagen());
                }
                //Guardamos el anuncio.
                try {
                    if (modo == MODO_CREAR) {
                        //Modo crear.

                        conexiones.createAnuncio(a);


                    } else if (modo == MODO_ACTUALIZAR) {
                            /* Modo actualizar, tenemos q poner el id del anuncio a
                            modificar. */
                        a.setIdAnuncio(modificando.getIdAnuncio());

                        conexiones.updateAnuncio(a);

                    }
                } catch (ServerException ex) {
                    switch (ex.getCode()) {

                        case 500:
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getActivity(),
                                            "Error al contactar con el servidor",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                            break;
                        case 403:
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getActivity(),
                                            "Error de permisos",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        case 404:
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getActivity(),
                                            "No existe el anuncio indicado",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        case 405:
                            //No hay sesion iniciada, vamos al login...
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    //Cerramos el dialogo en curso
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    //Indicamos mensaje
                                    Toast.makeText(getActivity(),
                                            "Sesion caducada",
                                            Toast.LENGTH_SHORT).show();

                                    //Lanzamos actividad
                                    Intent myIntent = new Intent(getActivity(),
                                            LoginActivity.class);
                                    startActivity(myIntent);
                                    getActivity().finish();

                                    //Matamos asynktask
                                    cancel(true);
                                }
                            });

                            break;

                    }

                }
                return "";
            } catch (Exception ex) {

                Toast.makeText(getActivity().getApplicationContext(),
                        "Debe rellenar todos los campos (excepto la imagen)",
                        Toast.LENGTH_SHORT).show();
                return null;
            }
            //No hacemos caso del url


        }
        /**
         * Pre: cierto
         * Post: Actualiza la UI tras creacion o actualizacion de anuncio.
         * Si result==null no hace nada, pues ha habido un error.
         *
         */
        @Override
        protected void onPostExecute(String result) {

            if(result!=null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (modo == MODO_CREAR) {
                    //Modo crear.


                    Toast.makeText(getActivity().getApplicationContext(),
                            "Anuncio creado correctamente",
                            Toast.LENGTH_SHORT).show();

                    //Se va a la seccion de busquedas.
                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = new BusquedaAnunciosFragment();
                    fragmentManager.beginTransaction().
                            replace(R.id.content_frame, fragment).
                            addToBackStack(null).commit();

                } else if (modo == MODO_ACTUALIZAR) {
                            /* Modo actualizar, tenemos q poner el id del anuncio a
                            modificar. */

                    Toast.makeText(getActivity().getApplicationContext(),
                            "Anuncio actualizado correctamente",
                            Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();
                }

            }
        }
    }

    @Override
    /**
     * Pre: cierto
     * Post: llamado al cerrar la actividad, se encarga de matar el hilo
     * de carga de imagen.
     */
    public void onStop() {

        super.onStop();


        if(imageTask != null && imageTask.getStatus() == AsyncTask.Status.RUNNING)
            imageTask.cancel(true);
        Log.d("isma","Cancelando task de imagen");
    }
}

