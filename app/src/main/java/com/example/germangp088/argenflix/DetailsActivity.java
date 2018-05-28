package com.example.germangp088.argenflix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class DetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle extras = getIntent().getExtras();

        Pelicula pelicula = new Gson().fromJson(extras.getString("pelicula"), Pelicula.class);

        TextView nombretv = findViewById(R.id.nombre);
        nombretv.setText(pelicula.getNombre());

        TextView preciotv = findViewById(R.id.precio);
        preciotv.setText(String.valueOf(pelicula.getPrecio()));

        ImageView fototv = findViewById(R.id.foto);
        fototv.setImageResource(pelicula.getFoto());
    }
}
