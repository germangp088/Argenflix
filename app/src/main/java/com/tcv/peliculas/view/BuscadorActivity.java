package com.tcv.peliculas.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tcv.peliculas.R;
import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.model.Pelicula;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscadorActivity extends AppCompatActivity{
    ArrayAdapter<String> mAdapter;
    ListView mListView;
    TextView mEmptyView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = (ListView) findViewById(R.id.list);
        mEmptyView = (TextView) findViewById(R.id.emptyView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(BuscadorActivity.this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mListView.setEmptyView(mEmptyView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

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
                    List<Pelicula> peliculasResponse = response.body();
                    ArrayList<String> peliculasFind = new ArrayList<String>();
                    for (Pelicula pelicula : peliculasResponse) {
                        if(pelicula.getTitulo().toLowerCase().contains(titulo.toLowerCase())){
                            peliculasFind.add(pelicula.getTitulo());
                        }
                    }
                    mAdapter = new ArrayAdapter<String>(BuscadorActivity.this,
                            android.R.layout.simple_list_item_1,
                            peliculasFind);
                    mListView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<Pelicula>> call, Throwable throwable) {
                    Toast.makeText(BuscadorActivity.this, "Ocurrio un error al querer obtener la lista de peliculas.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            mAdapter = new ArrayAdapter<String>(BuscadorActivity.this,
                    android.R.layout.simple_list_item_1,
                    new ArrayList<String>());
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }
}
