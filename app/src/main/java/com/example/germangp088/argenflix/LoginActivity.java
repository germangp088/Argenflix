package com.example.germangp088.argenflix;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private Context context;
    private SharedPreferences sharedPreferences;
    Boolean logueado;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        logueado = sharedPreferences.getBoolean("logueado",false);

        if (logueado){
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
        else{
            Button btn = findViewById(R.id.enter_btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isLoginSuccessful()) {
                        sharedPreferences.edit().putBoolean("logueado",true).apply();
                        gotoPeliculaMenu();
                    }
                }
            });
        }
    }
    /**
     * Redirige hacia la vista de menu de peliculas.
     */
    private void gotoPeliculaMenu() {
        //Llamo al ciclo de cierre del LoginActivity.
        finish();
        //Redirijo hacia el MainActivity.
        startActivity(new Intent(context, MainActivity.class));
    }

    private boolean isLoginSuccessful() {
        TextView userTxt = findViewById(R.id.usuario_et);
        TextView passTxt = findViewById(R.id.password_et);
        return userTxt.getText().toString().equals("german") && passTxt.getText().toString().equals("1234");
    }
}
