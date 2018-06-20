package com.tcv.peliculas.persistence;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tcv.peliculas.model.Pelicula;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "argenflix_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Favorito.CREATE_TABLE);
        initialize(db);
    }

    private void initialize(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertarNota(Pelicula pelicula, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(Favorito.COLUMN_PELICULA, pelicula.getId());
        insertContent(values, db);
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

    public Favorito getNota(int idPelicula) {
        //1º
        SQLiteDatabase db = this.getReadableDatabase();

        //2º
        Cursor cursor = db.query(Favorito.TABLE_NAME,
            new String[]{Favorito.COLUMN_ID, Favorito.COLUMN_PELICULA, Favorito.COLUMN_TIMESTAMP},
                Favorito.COLUMN_ID + "=?",
            new String[]{String.valueOf(idPelicula)},
            null, null, null, null);

        //3º
        Favorito favorito = null;
        if (cursor != null) {
            cursor.moveToFirst();
            favorito = new Favorito(
                    cursor.getInt(cursor.getColumnIndex(Favorito.COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndex(Favorito.COLUMN_PELICULA)));
        }

        // close the db connection
        cursor.close();
        db.close();
        return favorito;
    }

    public List<Favorito> getAllFavoritos() {
        //1º
        SQLiteDatabase db = this.getReadableDatabase();

        //2º
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                Favorito.TABLE_NAME, null);

        //3º
        List<Favorito> favoritos = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                favoritos.add(new Favorito(
                        cursor.getInt(cursor.getColumnIndex(Favorito.COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(Favorito.COLUMN_PELICULA))));
            } while (cursor.moveToNext());

            // close the db connection
            cursor.close();
            db.close();
        }
        return favoritos;
    }
}
