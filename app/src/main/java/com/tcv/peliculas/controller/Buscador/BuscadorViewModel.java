package com.tcv.peliculas.controller.Buscador;


import android.content.Context;
import android.content.SharedPreferences;

import com.tcv.peliculas.R;

public class BuscadorViewModel {

    private Context context;

    public BuscadorViewModel(Context context) {
        this.context = context;
    }

    public String getUsername() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name),
                Context.MODE_PRIVATE);
        return sharedPreferences.getString("usuario", "");
    }
}
