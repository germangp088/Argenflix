package com.tcv.peliculas.controller.Buscador;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcv.peliculas.R;
import com.tcv.peliculas.model.Pelicula;
import com.tcv.peliculas.view.PeliculaDetailsActivity;

import java.util.List;

public class BuscadorListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Pelicula> peliculas;
    private Context context;

    public BuscadorListAdapter(List<Pelicula> peliculas, Context context) {
        this.peliculas = peliculas;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BuscadorViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.buscador_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((BuscadorViewHolder) viewHolder).bind(peliculas.get(position));
    }

    @Override
    public int getItemCount() {
        return peliculas.size();
    }

    private class BuscadorViewHolder extends RecyclerView.ViewHolder {
        private final TextView tituloTv;
        private final ImageView imagenIv;

        public BuscadorViewHolder(View itemView) {
            super(itemView);
            tituloTv = itemView.findViewById(R.id.titulo);
            imagenIv = itemView.findViewById(R.id.imagen);
        }

        public void bind(final Pelicula pelicula) {
            tituloTv.setText(pelicula.getTitulo());
            imagenIv.setImageResource(pelicula.getImagenMini());
            //Glide.with(context).load(pelicula.getImagen()).into(imagenIv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PeliculaDetailsActivity.class);
                    intent.putExtra("pelicula", new Gson().toJson(pelicula));
                    context.startActivity(intent);
                }
            });
        }
    }
}
