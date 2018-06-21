package com.tcv.peliculas.controller.Favoritos;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcv.peliculas.R;
import com.tcv.peliculas.controller.Peliculas.PeliculasListAdapter;
import com.tcv.peliculas.controller.Peliculas.PeliculasViewModel;
import com.tcv.peliculas.model.Categoria;
import com.tcv.peliculas.persistence.Favorito;
import com.tcv.peliculas.view.PeliculaDetailsActivity;

import java.util.List;

public class FavoritosListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Favorito> favoritos;
    private Context context;

    public FavoritosListAdapter(List<Favorito> favoritos, Context context) {
        this.favoritos = favoritos;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoritoViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.favorito_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((FavoritoViewHolder) viewHolder).bind(favoritos.get(position));
    }

    @Override
    public int getItemCount() {
        return favoritos.size();
    }

    private class FavoritoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tituloTv;
        private final ImageView imagenTv;

        public FavoritoViewHolder(View itemView) {
            super(itemView);
            tituloTv = itemView.findViewById(R.id.titulo);
            imagenTv = itemView.findViewById(R.id.imagen);
        }

        public void bind(final Favorito favorito) {
            tituloTv.setText(favorito.getTituloPelicula());
            //imagenTv.setImageResource(favorito.getImagenPelicula());
            //Glide.with(context).load(pelicula.getImagen()).into(imagenIv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PeliculaDetailsActivity.class);
                    intent.putExtra("pelicula", new Gson().toJson(favorito));
                    context.startActivity(intent);
                }
            });
        }
    }
}
