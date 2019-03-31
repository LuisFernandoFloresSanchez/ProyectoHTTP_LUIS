package com.example.proyectohtml;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ConsultaRegistros extends AppCompatActivity {
    private String url = "https://mischief-making-stu.000webhostapp.com/registrosphp/consultajson.php";
    private RecyclerView mList;
    private DividerItemDecoration dividerItemDecoration;
    private LinearLayoutManager linearLayoutManager;
    private List<Registro> registroList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_registros);


        mList = findViewById(R.id.main_List);
        registroList = new ArrayList<>();
        adapter = new RegistroAdapter(getApplicationContext(), registroList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

    }

    //Cuando regresemos a la consulta despues de eliminar o modificar se limpia la lista y luego se toman de nuevo los datos
    @Override
    protected void onStart() {
        super.onStart();
        registroList.clear();
        adapter.notifyDataSetChanged();
        getData();
    }

    private void getData() {
        final ProgressDialog pdElemento = new ProgressDialog(this);
        pdElemento.setMessage("Cargando...");
        pdElemento.show();
        JsonArrayRequest jarElemento = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jObjeto = response.getJSONObject(i);
                        Registro rRegistros = new Registro();
                        rRegistros.setIdregistro(jObjeto.getInt("idregistro"));

                        rRegistros.setNombre(jObjeto.getString("nombre"));
                        rRegistros.setDestino(jObjeto.getString("destino"));
                        rRegistros.setRazonvisita(jObjeto.getString("razonvisita"));
                        registroList.add(rRegistros);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        pdElemento.dismiss();

                    }
                }
                adapter.notifyDataSetChanged();
                pdElemento.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                pdElemento.dismiss();
            }
        });
        RequestQueue rqElemento = Volley.newRequestQueue(this);
        rqElemento.add(jarElemento);
    }
}
