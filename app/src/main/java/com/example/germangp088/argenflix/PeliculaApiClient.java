package com.example.germangp088.argenflix;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PeliculaApiClient {
    private static Context context;
    private static PeliculaApi client;


    public static void init(Context con) {
        context = con;
    }

    private static PeliculaApi getClient(){
        if(client == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://my-json-server.typicode.com/avaudagna/pizzeria/") //URL BASE de la API
                    .addConverterFactory(GsonConverterFactory.create(new Gson())) //El GsonConverter, para parsear automaticamente las respuestas que vengan en objetos del modelo
                    .build();
            client = retrofit.create(PeliculaApi.class); //La interfaz que lista los metodos disponibles a consultar de nuestra API
        }
        return client;
    }

    //Obtiene la lista de peliculas del metodo pertinente de la API y la deveulve en un OnSuccessCallback
    public static void getPeliculas(final OnSuccessCallback callback) {
        //.enqueue() realiza el request asincronicamente, osea, en otro thread que no es el de UI (main thread)
        // Esto evita que el UI se tilde (!!!) y vuelve con su respuesta al mismo cuando se llama al metodo onResponse()
        // (La ejecucion del onResponse se realiza en el UI thread, por eso podemos interactuar con los elementos de UI, sino no podriamos)
        getClient().getPeliculas().enqueue(new Callback<List<Pelicula>>() {
            //Cuando no falla el request, entra aca
            @Override
            public void onResponse(Call<List<Pelicula>> call, Response<List<Pelicula>> response) {
                callback.execute(response.body());
            }
            //Cuando falla el request, entra aca
            @Override
            public void onFailure(Call<List<Pelicula>> call, Throwable throwable) {
                //Mensaje para mostar que hubo un error
                Toast.makeText(context, "Fallo al querer conectarse con el servidor", Toast.LENGTH_SHORT).show();
                //Cierro la app luego de un delay de 2 segundos para que el usuario llegue a leer el mensaje de error
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Cierre del programa (App)
                        System.exit(0);
                    }
                }, 2000); //Especifico un delay de 2 segundos ( 2000 milisegundos )
            }
        });
    }
}
