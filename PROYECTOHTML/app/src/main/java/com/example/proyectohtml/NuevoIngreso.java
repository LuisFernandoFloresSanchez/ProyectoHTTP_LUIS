package com.example.proyectohtml;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class NuevoIngreso extends AppCompatActivity {

    TextView txtNombre, txtDestino, txtRazon;
    Button btnCargarImagen, btnCargarImagen2;
    ImageButton btnAgregar;
    ImageView imvPlacas, imvINE;
    private final String CARPETA_RAIZ = "misimagenesprueba/";
    private final String RUTA_IMAGEN = CARPETA_RAIZ + "misfotos";
    private String sNombre, sDestino, sRazon, sPlacas, sIne;
    String path1;
    String path2;
    int iTipo;
    Bitmap bitmap, bitmap2;
    final String URL = "https://mischief-making-stu.000webhostapp.com/registrosphp/agregarregistro.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_ingreso);

        imvPlacas = findViewById(R.id.imvPlacas);
        imvINE = findViewById(R.id.imvINE);
        btnCargarImagen = findViewById(R.id.btnCargarImagen);
        btnCargarImagen2 = findViewById(R.id.btnCargarImagen2);
        btnAgregar = findViewById(R.id.btnAgregar);
        txtNombre = findViewById(R.id.txtNombre);
        txtDestino = findViewById(R.id.txtDestino);
        txtRazon = findViewById(R.id.txtRazon);

        if (validarPermisos()) {
            btnCargarImagen.setEnabled(true);

        } else {
            btnCargarImagen.setEnabled(false);
        }

    }

    //Para pedir permisos al usuario y validarlos
    private boolean validarPermisos() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if ((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {
            cargarDialogoRecomendacion();
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
        }

        return false;
    }

    //Dialogo de recomendacion de datos
    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(NuevoIngreso.this);
        dialogo.setTitle("Permisos desactivados");
        dialogo.setMessage("Debes aceptar los permisos para que funcione esta acción");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
            }
        });
        dialogo.show();
    }
    //ESTE SE CARGA CON EL GENERATE, OVERRIDE METHODE, ONREQUIESTPERMISSIONRESULT
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                btnCargarImagen.setEnabled(true);
            } else {
                solicitarPermisosManuales();
            }
        }
    }

    //Permisos manuales
    private void solicitarPermisosManuales() {
        final CharSequence[] opciones = {"Si", "No"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(NuevoIngreso.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (opciones[i].equals("Si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(NuevoIngreso.this, "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        alertOpciones.show();
    }




    //Metodo para tomar la foto
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

    //Botones para cargar las fotos
    public void clickBotonCargarImagen(View v) {
        iTipo = 1;
        cargarImagen();

    }

    public void clickBotonCargarImagen2(View v) {
        iTipo = 2;
        cargarImagen();

    }

    // Boton para agregar datos a la base de datos
    public void clickBotonAgregar(View v) {

        final ProgressDialog pdElemento = new ProgressDialog(this);
        pdElemento.setMessage("Cargando..");
        pdElemento.show();
        sNombre = txtNombre.getText().toString();
        sDestino = txtDestino.getText().toString();
        sRazon = txtRazon.getText().toString();
        sPlacas = convertirImgString(bitmap);
        sIne = convertirImgString(bitmap2);

        RequestQueue queue = Volley.newRequestQueue(NuevoIngreso.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(NuevoIngreso.this, "Agregado con exito!", Toast.LENGTH_SHORT).show();
                pdElemento.hide();
                txtNombre.setText("");
                txtDestino.setText("");
                txtRazon.setText("");
                imvPlacas.setImageIcon(null);
                imvINE.setImageIcon(null);
                           }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NuevoIngreso.this, "Errorsillo", Toast.LENGTH_SHORT).show();
                pdElemento.hide();
            }
        }) {
            //Preparar datos enviados con POST al servidor
            //este metodo se sobre escribe con solo escribir getParams
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("nombre", sNombre);
                map.put("destino", sDestino);
                map.put("razonvisita", sRazon);
                map.put("placas", sPlacas);
                map.put("ine", sIne);
                return map;
            }

        };
        //Se realiza la peticion HTTP
        queue.add(request);

    }

    private String convertirImgString(Bitmap bit) {

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);

        return imagenString;
    }

    private void cargarImagen() {
        final CharSequence[] opciones = {"Tomar Foto", "Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(NuevoIngreso.this);
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

    //Metodo para ver el resultado del alertOptions para ver 10 si se toma la foto o 20 si se agarra del telefono
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 10:
                    try {
                        if (iTipo == 1) {
                            Uri miPath = data.getData();
                            imvPlacas.setImageURI(miPath);
                            Toast.makeText(this, "miPath = " + miPath, Toast.LENGTH_SHORT).show();
                            bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), miPath);
                            imvPlacas.setImageBitmap(bitmap);
                        } else if (iTipo == 2) {
                            Uri miPath2 = data.getData();
                            Toast.makeText(this, "miPath = " + miPath2, Toast.LENGTH_SHORT).show();

                            imvINE.setImageURI(miPath2);
                            bitmap2 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), miPath2);
                            imvINE.setImageBitmap(bitmap2);
                        }
                    } catch (IOException e) {
                        Toast.makeText(this, "cae en catch", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    break;
                case 20:
                    if (iTipo == 1) {
                        MediaScannerConnection.scanFile(this, new String[]{path1}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {


                                    @Override
                                    public void onScanCompleted(String s, Uri uri) {
                                        Log.i("Ruta de almacenamiento", "Path: " + path1);
                                    }
                                });
                        bitmap = BitmapFactory.decodeFile(path1);
                        imvPlacas.setImageBitmap(bitmap);
                    } else if (iTipo == 2) {
                        MediaScannerConnection.scanFile(this, new String[]{path2}, null,
                                new MediaScannerConnection.OnScanCompletedListener() {


                                    @Override
                                    public void onScanCompleted(String s, Uri uri) {
                                        Log.i("Ruta de almacenamiento", "Path: " + path2);
                                    }
                                });
                        imvINE.setImageBitmap(bitmap2);
                        bitmap2 = BitmapFactory.decodeFile(path2);
                    }
                    break;
            }
        }
    }
}
