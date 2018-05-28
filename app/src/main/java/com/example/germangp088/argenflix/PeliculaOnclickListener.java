package com.example.germangp088.argenflix;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

public class PeliculaOnclickListener implements View.OnClickListener{

    private Context context;
    private Pelicula pelicula;

    public PeliculaOnclickListener(Context context, Pelicula pelicula){
        this.context = context;
        this.pelicula = pelicula;
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(context, pelicula.getNombre(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(context, DetailsActivity.class);

        String peliculaSerializada = new Gson().toJson(pelicula);

        intent.putExtra("pelicula", peliculaSerializada);

        context.startActivity(intent);
    }
}
