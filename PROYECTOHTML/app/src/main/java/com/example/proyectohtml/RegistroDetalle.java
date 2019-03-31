package com.example.proyectohtml;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

public class RegistroDetalle extends AppCompatActivity {
    Button btnRegresar;
    ImageButton btnEliminar, btnModificar;
    private TextView id1, nombreR, DestinoR, RazonvisitaR;
    ImageView imageviewPlacas, imageviewINE;
    final String url = "https://mischief-making-stu.000webhostapp.com/registrosphp/eliminarregistro.php";
    private String sImagenPlacas, sImagenINE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_detalle);

        //***************** Ligar Componentes ******************
        btnRegresar = findViewById(R.id.btnRegresar);
        nombreR = findViewById(R.id.textViewNombre);
        DestinoR = findViewById(R.id.textViewDestino);
        RazonvisitaR= findViewById(R.id.textViewRazonVisita);
        imageviewPlacas = findViewById(R.id.imageviewPlacas);
        imageviewINE = findViewById(R.id.imageviewINE);
        id1 = findViewById(R.id.textViewId);

        getIncomigIntent();
    }


    private void eliminarRegistro() {


        RequestQueue queue = Volley.newRequestQueue(RegistroDetalle.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(RegistroDetalle.this, "Respuesta: " + response, Toast.LENGTH_SHORT).show();
                limpiarCampos();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistroDetalle.this, "Errorsillo" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", id1.getText().toString());
                    map.put("nombre", nombreR.getText().toString());

                return map;
            }
        };
        queue.add(request);
    }

    private void limpiarCampos() {
        id1.setText("");
        nombreR.setText("");
        DestinoR.setText("");
        RazonvisitaR.setText("");
        imageviewPlacas.setImageIcon(null);
        imageviewINE.setImageIcon(null);

    }


    private void getIncomigIntent() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("nombre") && getIntent().hasExtra("destino") &&
                getIntent().hasExtra("razonvisita")) {
            String id = getIntent().getStringExtra("id");
            String nombre = getIntent().getStringExtra("nombre");
            String destino = getIntent().getStringExtra("destino");
            String razonvisita = getIntent().getStringExtra("razonvisita");
            // String placas= getIntent().getStringExtra("placas");
           // String ine = getIntent().getStringExtra("ine");
            sImagenPlacas = nombre + " 1.jpg";
            sImagenINE = nombre + " 2.jpg";
            setDatos(id, nombre, destino, razonvisita);
        }else{
            Toast.makeText(this, "no hay extra", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDatos(String id, String nombre, String destino, String razonvisita) {
        TextView id1 = findViewById(R.id.textViewId);
        id1.setText(id);
        TextView nombre1 = findViewById(R.id.textViewNombre);
        nombre1.setText(nombre);
        TextView destino1 = findViewById(R.id.textViewDestino);
        destino1.setText(destino);
        TextView razonvisita1 = findViewById(R.id.textViewRazonVisita);
        razonvisita1.setText(razonvisita);
        //Parte de placas
        String url = "https://mischief-making-stu.000webhostapp.com/registrosphp/misfotos/" + nombre + " 1.jpg";
        Log.wtf("ERROR ", "url =  " +url );
        Glide.with(this).asBitmap().load(url).into(imageviewPlacas);

        String url2 = "https://mischief-making-stu.000webhostapp.com/registrosphp/misfotos/" + nombre + " 2.jpg";
        Glide.with(this).asBitmap().load(url2).into(imageviewINE);

    }

    //********************************************* METODOS PARA BOTONES ***************************************
    public void clickBotonRegresar(View v) {

    finish();

    }

    public void clickBotonEliminar(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(RegistroDetalle.this);
        builder.setMessage("¿Realmente desea elmiminarlo?");
        builder.setTitle("Confirmación de Eliminación");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(RegistroDetalle.this, "Operación eliminar aceptada", Toast.LENGTH_SHORT).show();
                eliminarRegistro();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(RegistroDetalle.this, "Operación eliminar cancelada", Toast.LENGTH_SHORT).show();
            }
        });
        //crear y mostrar el dialogo
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    public void clickBotonModificar(View v) {
        Intent intent = new Intent(RegistroDetalle.this, RegistroModificar.class);
        intent.putExtra("id", id1.getText().toString());
        intent.putExtra("nombre", nombreR.getText().toString());
        intent.putExtra("destino", DestinoR.getText().toString());
        intent.putExtra("razonvisita", RazonvisitaR.getText().toString());
        intent.putExtra("placas", sImagenPlacas);
        intent.putExtra("ine", sImagenINE);
      //  intent.putExtra("bitmap", bitmap);
      //  intent.putExtra("bitmap2", sImagenINE);
        startActivity(intent);

    }

    
    
    
}
