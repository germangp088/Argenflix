package com.tcv.peliculas.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tcv.peliculas.R;
import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.controller.Buscador.BuscadorListAdapter;
import com.tcv.peliculas.controller.Buscador.BuscadorViewModel;
import com.tcv.peliculas.model.Pelicula;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscadorActivity extends AppCompatActivity{
    RecyclerView buscador_rv;
    private BuscadorListAdapter buscadorListAdapter;
    private List<Pelicula> peliculas;
    private BuscadorViewModel buscadorViewModel = new BuscadorViewModel(this);

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Agarrar el recyclerview de categorias
        peliculas = new ArrayList<>();
        buscador_rv = (RecyclerView) findViewById(R.id.buscador_rv);

        //Asociar un adapter de Buscador a ese recyclerview
        buscadorListAdapter = new BuscadorListAdapter(peliculas, this);
        buscador_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        buscador_rv.setAdapter(buscadorListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Buscar titulo...");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String titulo) {
                search(titulo);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void search(final String titulo){
        if (!titulo.equals("")){
            ApiClient.getClient(this).getPeliculas().enqueue(new Callback<List<Pelicula>>() {
                @Override
                public void onResponse(Call<List<Pelicula>> call, Response<List<Pelicula>> response) {
                    peliculas.clear();
                    List<Pelicula> peliculasResponse = response.body();
                    List<Pelicula> peliculasFind = new ArrayList<Pelicula>();
                    for (Pelicula pelicula : peliculasResponse) {
                        if(pelicula.getTitulo().toLowerCase().contains(titulo.toLowerCase())){
                            peliculasFind.add(pelicula);
                        }
                    }
                    peliculas.addAll(peliculasFind);
                }

                @Override
                public void onFailure(Call<List<Pelicula>> call, Throwable throwable) {
                    Toast.makeText(BuscadorActivity.this, "Ocurrio un error al querer obtener la lista de peliculas.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            peliculas.clear();
        }
        buscadorListAdapter.notifyDataSetChanged();
    }
}
