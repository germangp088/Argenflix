package com.tcv.peliculas.model;


import java.util.List;

public class Pelicula {
    private int id;
    private String titulo;
    private String genero;
    private int imagen;
    private int imagenMini;
    private String lanzamiento;
    private int duracion;
    private List<String> artistas;
    private double puntuacion;
    private String video_url;


    public Pelicula(String titulo,
                    String genero, int imagen) {
        this.titulo = titulo;
        this.genero = genero;
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
    }

    public int getImagen() {
        return imagen;
    }

    public int getId() {
        return id;
    }

    public String getLanzamiento() {
        return lanzamiento;
    }

    public List<String> getArtistas() {
        return artistas;
    }

    public int getDuracion() {
        return duracion;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public int getImagenMini() {
        return imagenMini;
    }

    public void setImagenMini(int imagenMini) {
        this.imagenMini = imagenMini;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
