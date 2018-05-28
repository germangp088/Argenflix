package com.example.germangp088.argenflix;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PeliculaApi {
    @GET("pizzas") //Verbo HTTP y nombre del metodo de la API
    public Call<List<Pelicula>> getPeliculas(); //El return type define como sera parseado automaticamente el response
}
