package com.example.proyectohtml;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Intent inConsultar, inIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void clickBotonIngresar(View v) {
        inIngresar = new Intent(getApplicationContext(), NuevoIngreso.class);
        startActivity(inIngresar);
    }


    public void clickBotonConsultar(View v) {
        inConsultar = new Intent(getApplicationContext(),ConsultaRegistros.class);
        startActivity(inConsultar);
    }
}
