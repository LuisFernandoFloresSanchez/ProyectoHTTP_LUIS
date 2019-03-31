package com.example.proyectohtml;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistroModificar extends AppCompatActivity {
    private EditText Nombre1, Destino1, Razonvisita1;
    private TextView id1;
    ImageView imvPlacas, imvINE;
    private String sId, sNombre, sDestino, sRazonvisita, sPlacas, sIne;
    private String url = "https://mischief-making-stu.000webhostapp.com/registrosphp/modificarregistro.php";
    private final String CARPETA_RAIZ = "misimagenesprueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misfotos";
    String path1;
    String path2;
    int iTipo;
    boolean bTipo1, bTipo2;
    Bitmap bitmapModificar, bitmap2Modificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_modificar);
        id1 = findViewById(R.id.id);
        Nombre1 = findViewById(R.id.txtNombreComun);
        Destino1 = findViewById(R.id.txtNombreCientifico);
        Razonvisita1 = findViewById(R.id.txtDescripcion);
        imvPlacas = findViewById(R.id.imvPlacas2);
        imvINE = findViewById(R.id.imvINE2);

        getIncomingIntent();
    }


    //************************************************** Tomar el intento **************************************************
    private void getIncomingIntent() {
        id1.setText(getIntent().getStringExtra("id"));
        Nombre1.setText(getIntent().getStringExtra("nombre"));
        Destino1.setText(getIntent().getStringExtra("destino"));
        Razonvisita1.setText(getIntent().getStringExtra("razonvisita"));
        // bitmapModificar = getIntent().getStringExtra("bitmap1");
        String nombre = getIntent().getStringExtra("nombre");
        //Parte de placas
        String url = "https://mischief-making-stu.000webhostapp.com/registrosphp/misfotos/" + nombre + " 1.jpg";
        Glide.with(this).asBitmap().load(url).into(imvPlacas);
        //Parte de INE
        String url2 = "https://mischief-making-stu.000webhostapp.com/registrosphp/misfotos/" + nombre + " 2.jpg";
        Glide.with(this).asBitmap().load(url2).into(imvINE);
    }

    //**************************************************Acción de botones **************************************************
    public void clickBotonCargarImagen(View v) {
        iTipo = 1;
        cargarImagen();

    }

    public void clickBotonCargarImagen2(View v) {
        iTipo = 2;
        cargarImagen();

    }

    public void clickBotonModificar(View v) {
        //Obtener los datos modificados
        final ProgressDialog pdElemento = new ProgressDialog(this);
        pdElemento.setMessage("Cargando...");
        pdElemento.show();
        sId = id1.getText().toString();
        sNombre = Nombre1.getText().toString();
        sDestino = Destino1.getText().toString();
        sRazonvisita = Razonvisita1.getText().toString();
        if (bTipo1 == true) {
            sPlacas = convertirImgString(bitmapModificar);
        }
        if (bTipo2 == true) {
            sIne = convertirImgString(bitmap2Modificar);
        }

        //Construir peticion HTTP
        RequestQueue queue = Volley.newRequestQueue(RegistroModificar.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pdElemento.hide();
                Toast.makeText(RegistroModificar.this, "si funcionó", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdElemento.hide();
                Toast.makeText(RegistroModificar.this, "Errorsillo", Toast.LENGTH_SHORT).show();
            }
        }) {
            //Preparar datos enviados con POST al servidor
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", sId);
                map.put("nombre", sNombre);
                map.put("destino", sDestino);
                map.put("razonvisita", sRazonvisita);
                if (bTipo1 == true) {
                    map.put("placas", sPlacas);
                    Log.wtf("REVISION", "placas = " + sPlacas );
                }
                if (bTipo2 == true) {
                    map.put("ine", sIne);
                    Log.wtf("REVISION", "ine = " + sIne);
                }


                return map;
            }

        };
        queue.add(request);
        //Se realiza la peticion HTTP

    }

    public void clickCancelar(View v) {
        finish();
    }

    //****************************************************************************************************
    private void cargarImagen() {

        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RegistroModificar.this);
        alertOpciones.setTitle("Seleccione una opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (opciones[i].equals("Tomar Foto")) {
                    tomarFotografia();
                } else if (opciones[i].equals("Cargar Imagen")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), 10);

                } else {
                    dialog.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void tomarFotografia() {


        if (iTipo == 1) {
            File fileImagen1 = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
            boolean creada1 = fileImagen1.exists();
            String nombreImagen1 = "";
            if (creada1 == false) {
                creada1 = fileImagen1.mkdirs();
            }
            if (creada1 == true) {
                nombreImagen1 = (System.currentTimeMillis() / 100) + ".jpg";
            }
            File imagen1 = null;
            path1 = Environment.getExternalStorageDirectory() + File.separator + RUTA_IMAGEN + File.separator + nombreImagen1;
            imagen1 = new File(path1);
            Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (Build.VERSION.SDK_INT >= 24) {
                String authorities = getApplicationContext().getPackageName() + ".provider";
                Uri imagenUri = FileProvider.getUriForFile(this, authorities, imagen1);
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri);

            } else {
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen1));
            }
            startActivityForResult(intent1, 20);
        } else if (iTipo == 2) {
            File fileImagen2 = new File(Environment.getExternalStorageDirectory(), RUTA_IMAGEN);
            boolean creada2 = fileImagen2.exists();
            String nombreImagen2 = "";
            if (creada2 == false) {
                creada2 = fileImagen2.mkdirs();
            }
            if (creada2 == true) {
                nombreImagen2 = (System.currentTimeMillis() / 100) + ".jpg";
            }
            File imagen2 = null;
            path2 = Environment.getExternalStorageDirectory() + File.separator + RUTA_IMAGEN + File.separator + nombreImagen2;
            imagen2 = new File(path2);
            Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (Build.VERSION.SDK_INT >= 24) {
                String authorities = getApplicationContext().getPackageName() + ".provider";
                Uri imagenUri = FileProvider.getUriForFile(this, authorities, imagen2);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri);

            } else {
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen2));
            }
            startActivityForResult(intent2, 20);

        }


    }

    private String convertirImgString(Bitmap bit) {

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);

        return imagenString;
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 10:
                    try {
                        if (iTipo == 1) {
                            bTipo1 = true;
                            Uri miPath = data.getData();
                            imvPlacas.setImageURI(miPath);
                            bitmapModificar = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), miPath);
                            imvPlacas.setImageBitmap(bitmapModificar);
                        } else if (iTipo == 2) {
                            bTipo2 = true;
                            Uri miPath2 = data.getData();
                            imvINE.setImageURI(miPath2);
                            bitmap2Modificar = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), miPath2);
                            imvINE.setImageBitmap(bitmap2Modificar);
                        }
                    } catch (IOException e) {
                        Toast.makeText(this, "cae en catch", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    break;
                case 20:


                    if (iTipo == 1) {
                        bTipo1 = true;
                        MediaScannerConnection.scanFile(this, new String[]{path1}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {


                                    @Override
                                    public void onScanCompleted(String s, Uri uri) {
                                        Log.i("Ruta de almacenamiento", "Path: " + path1);
                                    }
                                });
                        bitmapModificar = BitmapFactory.decodeFile(path1);
                        imvPlacas.setImageBitmap(bitmapModificar);
                    } else if (iTipo == 2) {
                        bTipo2 = true;
                        MediaScannerConnection.scanFile(this, new String[]{path2}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {


                                    @Override
                                    public void onScanCompleted(String s, Uri uri) {
                                        Log.i("Ruta de almacenamiento", "Path: " + path2);
                                    }
                                });
                        imvINE.setImageBitmap(bitmap2Modificar);
                        bitmap2Modificar = BitmapFactory.decodeFile(path2);
                    }
                    break;
            }

        }
    }

}
