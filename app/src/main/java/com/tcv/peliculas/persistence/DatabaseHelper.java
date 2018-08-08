package com.tcv.peliculas.persistence;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tcv.peliculas.R;
import com.tcv.peliculas.model.Pelicula;
import com.tcv.peliculas.view.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "argenflix_db";
    private static String usuario;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        usuario = getUsuario(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Favorito.CREATE_TABLE);
        initialize(db);
    }

    private void initialize(SQLiteDatabase db) {
        boolean closeDb = false;
        if(db == null) {
            db = this.getWritableDatabase();
            closeDb = true;
        }
        db.delete(Favorito.TABLE_NAME, null, null);
        if(closeDb)
            db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        boolean closeDb = false;
        if(db == null) {
            db = this.getWritableDatabase();
            closeDb = true;
        }
        db.execSQL("DROP TABLE IF EXISTS " + Favorito.TABLE_NAME + ";");
        db.execSQL(Favorito.CREATE_TABLE);
        if(closeDb)
            db.close();
    }

    public void insertarFavorito(Pelicula pelicula, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        //Columna donde va el dato y el valor.
        values.put(Favorito.COLUMN_PELICULA_ID, pelicula.getId());
        values.put(Favorito.COLUMN_USUARIO, usuario);
        values.put(Favorito.COLUMN_PELICULA_TITULO, pelicula.getTitulo());
        values.put(Favorito.COLUMN_PELICULA_IMAGEN, pelicula.getImagen());
        insertContent(values, db);
    }

    public void deleteFavorito(int peliculaId, SQLiteDatabase db) {
        String[] whereArgs = new String[] { String.valueOf(peliculaId), usuario };
        deleteContent(whereArgs, db);
    }

    private void insertContent(ContentValues values, SQLiteDatabase db) {
        boolean closeDb = false;
        if(db == null) {
            db = this.getWritableDatabase();
            closeDb = true;
        }
        db.insert(Favorito.TABLE_NAME, null, values);
        if(closeDb)
            db.close();
    }

    private void deleteContent(String[] whereArgs, SQLiteDatabase db) {
        boolean closeDb = false;
        if(db == null) {
            db = this.getWritableDatabase();
            closeDb = true;
        }
        db.delete(Favorito.TABLE_NAME, Favorito.COLUMN_PELICULA_ID + "=? AND " + Favorito.COLUMN_USUARIO + "=?"   , whereArgs);
        if(closeDb)
            db.close();
    }

    public boolean getIfIsFavorito(int idPelicula) {
        //1º
        SQLiteDatabase db = this.getReadableDatabase();

        //2º
        Cursor cursor = db.rawQuery("SELECT COUNT(" + Favorito.COLUMN_PELICULA_ID + ") AS fav FROM "
                + Favorito.TABLE_NAME + " WHERE " + Favorito.COLUMN_PELICULA_ID + "=? AND " + Favorito.COLUMN_USUARIO + "=?;",
                new String[]{String.valueOf(idPelicula), usuario});

        //3º
        boolean result = false;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = (cursor.getInt(cursor.getColumnIndex("fav")) > 0);
        }

        // close the db connection
        cursor.close();
        db.close();
        return result;
    }

    public List<Favorito> getAllFavoritos() {
        //1º
        SQLiteDatabase db = this.getReadableDatabase();

        //2º
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                Favorito.TABLE_NAME + " WHERE " + Favorito.COLUMN_USUARIO + "=?;",
                new String[]{usuario});

        //3º
        List<Favorito> favoritos = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                favoritos.add(new Favorito(
                        cursor.getInt(cursor.getColumnIndex(Favorito.COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(Favorito.COLUMN_PELICULA_ID)),
                        cursor.getString(cursor.getColumnIndex(Favorito.COLUMN_USUARIO)),
                        cursor.getString(cursor.getColumnIndex(Favorito.COLUMN_PELICULA_TITULO)),
                        cursor.getString(cursor.getColumnIndex(Favorito.COLUMN_PELICULA_IMAGEN))));
            } while (cursor.moveToNext());

            // close the db connection
            cursor.close();
            db.close();
        }
        return favoritos;
    }

    public String getUsuario(Context context){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(
                        context.getString(R.string.app_name),Context.MODE_PRIVATE);
        return sharedPreferences.getString("usuario","usuario");
    }
}
