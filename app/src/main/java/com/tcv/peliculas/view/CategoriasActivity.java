package com.tcv.peliculas.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tcv.peliculas.R;
import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.controller.Categorias.CategoriasListAdapter;
import com.tcv.peliculas.controller.Categorias.CategoriasViewModel;
import com.tcv.peliculas.model.Categoria;
import com.tcv.peliculas.model.Pelicula;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriasActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView categoriasRv;
    private CategoriasListAdapter categoriasAdapter;
    private List<Categoria> categorias;

    private CategoriasViewModel categoriasViewModel = new CategoriasViewModel(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Como es el layout de categorias
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Agarrar el recyclerview de categorias
        categorias = new ArrayList<>();
        categoriasRv = (RecyclerView) findViewById(R.id.categorias_rv);

        //Asociar un adapter de Categoria a ese recyclerview
        categoriasAdapter = new CategoriasListAdapter(categorias, this);
        categoriasRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        categoriasRv.setAdapter(categoriasAdapter);
        obtenerCategorias();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fav) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        }  else if (id == R.id.cerrar_sesion) {
            cerrarSesion();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        setProfile();

        MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();

        mSearch.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(CategoriasActivity.this,
                        BuscadorActivity.class);
                CategoriasActivity.this.startActivity(intent);
                CategoriasActivity.this.finish();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setProfile(){
        SharedPreferences sharedPreferences =
                CategoriasActivity.this.getSharedPreferences(
                        CategoriasActivity.this.getString(R.string.app_name),Context.MODE_PRIVATE);
        String usuario = sharedPreferences.getString("usuario","");
        String imagen = sharedPreferences.getString("profile_picture_" + usuario,"");

        ImageView avatar = (ImageView)findViewById(R.id.avatar);

        if(String.valueOf(imagen).isEmpty())
        {
            avatar.setImageResource(R.drawable.avatar);
        }
        else{
            avatar.setImageResource(Integer.parseInt(imagen));
        }

        TextView user = (TextView)findViewById(R.id.usuario);
        user.setText(usuario);
    }

    private void obtenerCategorias(){
        ApiClient.getClient(this).getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                categorias.clear();
                List<Categoria> categoriasResponse = response.body();
                categorias.addAll(categoriasResponse);
                categoriasAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable throwable) {
                Toast.makeText(CategoriasActivity.this, "Ocurrio un error al querer obtener la lista de peliculas.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void cerrarSesion() {
        SharedPreferences sharedPreferences =
            getSharedPreferences(getString(R.string.app_name),
                    Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
