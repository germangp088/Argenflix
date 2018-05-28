package com.example.germangp088.argenflix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PeliculaAdapter extends BaseAdapter {

    //Los atributos de la clase
    private Context context;
    private List<Pelicula> peliculaList;

    //Anulamos el posible uso del constructor por defecto
    private PeliculaAdapter() { }

    //El unico constructor habilitado para instanciar esta clase
    public PeliculaAdapter(Context context, List<Pelicula> peliculaList) {
        this.peliculaList = peliculaList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return peliculaList.size();
    }

    @Override
    public Object getItem(int i) {
        return peliculaList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return peliculaList.get(i).getNombre().hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View peliculaItem = layoutInflater.from(context).inflate(R.layout.pelicula_item, null, false);

        Pelicula pelicula = (Pelicula) getItem(i);

        ((TextView) peliculaItem.findViewById(R.id.nombre)).setText(pelicula.getNombre());

        ((ImageView) peliculaItem.findViewById(R.id.foto)).setImageResource(pelicula.getFoto());

        ((TextView) peliculaItem.findViewById(R.id.precio)).setText(String.valueOf(pelicula.getPrecio()));

        peliculaItem.setOnClickListener(new PeliculaOnclickListener(context, pelicula));

        return peliculaItem;
    }
}
