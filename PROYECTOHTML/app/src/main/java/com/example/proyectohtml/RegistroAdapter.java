package com.example.proyectohtml;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class RegistroAdapter extends RecyclerView.Adapter<RegistroAdapter.ViewHolder> {
    private Context context;
    private List<Registro> list;


    public RegistroAdapter(Context context, List<Registro> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public RegistroAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_adaptador_registro, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RegistroAdapter.ViewHolder viewHolder, int i) {
        final Registro registro = list.get(i);
        viewHolder.textId.setText(String.valueOf(registro.getIdregistro()));
        viewHolder.textNombre.setText(String.valueOf(registro.getNombre()));
        viewHolder.textDestino.setText(String.valueOf(registro.getDestino()));
        viewHolder.textRazonvisita.setText(String.valueOf(registro.getRazonvisita()));
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegistroDetalle.class);
                intent.putExtra("id", String.valueOf(registro.getIdregistro()));
                intent.putExtra("nombre", registro.getNombre());
                intent.putExtra("destino", registro.getDestino());
                intent.putExtra("razonvisita", registro.getRazonvisita());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textId, textNombre, textDestino,textRazonvisita;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.main_id);
            textNombre = itemView.findViewById(R.id.main_nombre);
            textDestino = itemView.findViewById(R.id.main_destino);
            textRazonvisita = itemView.findViewById(R.id.main_razonvisita);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }

    }
}
