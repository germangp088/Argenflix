package com.tcv.peliculas.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.tcv.peliculas.R;
import com.tcv.peliculas.controller.Categorias.CategoriasListAdapter;
import com.tcv.peliculas.controller.Favoritos.FavoritosListAdapter;
import com.tcv.peliculas.controller.Favoritos.FavoritosViewModel;
import com.tcv.peliculas.model.Categoria;
import com.tcv.peliculas.persistence.DatabaseHelper;
import com.tcv.peliculas.persistence.Favorito;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView favoritosRv;
    private FavoritosListAdapter favoritosListAdapter;
    private List<Favorito> favoritos;
    private FavoritosViewModel favoritosViewModel = new FavoritosViewModel(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        List<Favorito> favoritos = dbHelper.getAllFavoritos();
        //Agarrar el recyclerview de categorias
        favoritosRv = (RecyclerView) findViewById(R.id.favoritos_rv);

        //Asociar un adapter de Categoria a ese recyclerview
        favoritosListAdapter = new FavoritosListAdapter(favoritos, this);
        favoritosRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        favoritosRv.setAdapter(favoritosListAdapter);
        favoritosListAdapter.notifyDataSetChanged();
    }
}
