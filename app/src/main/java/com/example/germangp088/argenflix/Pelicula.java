package com.example.germangp088.argenflix;

import android.content.Context;

public class Pelicula {

    private transient Context context;
    private String nombre;
    private int foto;
    private int precio;

    //Para funcionar Gson necesita que el constructor sin parametros este definido en las
    //clases que serializa y deserializa ya que instancia un objeto con este constructor
    //y setea sus parametros con sus seters.
    public Pelicula() {}

    public Pelicula(Context context, String nombre, int foto, int precio){
        this.setContext(context);
        this.setNombre(nombre);
        this.setFoto(foto);
        this.setPrecio(precio);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
