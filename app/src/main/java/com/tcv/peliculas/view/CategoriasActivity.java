package com.tcv.peliculas.view;
//package com.radefffactory.location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tcv.peliculas.R;
import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.controller.Categorias.CategoriasListAdapter;
import com.tcv.peliculas.controller.Categorias.CategoriasViewModel;
import com.tcv.peliculas.model.Categoria;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriasActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ObservableScrollView.OnScrollChangedListener{
    private static final String IMAGE_DIRECTORY = "/argenflix";
    private static final int CAMERA = 0;
    private static final int GALLERY = 1;
    private RecyclerView categoriasRv;
    private CategoriasListAdapter categoriasAdapter;
    private List<Categoria> categorias;
    private ObservableScrollView mScrollView;
    private View imgContainer;
    private ImageView mImageView;

    private static final int MY_PERMISSION_REQUEST_LOCATION = 1;


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

        mScrollView = (ObservableScrollView)findViewById(R.id.scroll_view);
        mScrollView.setOnScrollChangedListener(this);
        imgContainer = findViewById(R.id.img_container);
        mImageView =  findViewById(R.id.img);

        //Agarrar el recyclerview de categorias
        categorias = new ArrayList<>();
        categoriasRv = (RecyclerView) findViewById(R.id.categorias_rv);

        //Asociar un adapter de Categoria a ese recyclerview
        categoriasAdapter = new CategoriasListAdapter(categorias, this);
        categoriasRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        categoriasRv.setAdapter(categoriasAdapter);
        obtenerCategorias();
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {

        //Easy version.
        int scrollY = mScrollView.getScrollY();
        // Add parallax effect
        imgContainer.setTranslationY(scrollY * 0.75f);


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
            avatar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    showPictureDialog();
                }
            });
        }
        else{
            File imgFile = new  File(imagen);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                avatar.setImageBitmap(myBitmap);
            }
        }

        TextView user = (TextView)findViewById(R.id.usuario);
        user.setText(usuario);

        TextView ciudad = (TextView) findViewById(R.id.ciudad);
        if (ContextCompat.checkSelfPermission(CategoriasActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CategoriasActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION))
            {
                ActivityCompat.requestPermissions (CategoriasActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
            }
            else
            {
                ActivityCompat.requestPermissions (CategoriasActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
            }
        }
        else
        {
            LocationManager locationManager = (LocationManager) getSystemService (Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation (LocationManager.NETWORK_PROVIDER);
            try
            {
                ciudad.setText ("Ciudad: " + GetHereLocation(location.getLatitude(), location.getLongitude()));
            }
            catch (Exception e)
            {
                e.printStackTrace ();
                Toast.makeText (CategoriasActivity.this, "Not found!", Toast.LENGTH_SHORT).show();
                ciudad.setText("Ciudad no encontrada");
            }
        }
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Obtener imagen");
        String[] pictureDialogItems = {
                "Camara",
                "Galeria" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case CAMERA:
                                int cameraP = checkSelfPermission(Manifest.permission.CAMERA);
                                int readP = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                                int writeP = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                if (cameraP != PackageManager.PERMISSION_GRANTED ||
                                        readP != PackageManager.PERMISSION_GRANTED ||
                                        writeP != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            CAMERA);
                                    break;
                                }
                                else {
                                    takePhotoFromCamera();
                                }
                                break;
                            case GALLERY:
                                choosePhotoFromGallary();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhotoFromCamera();
                } else {
                    Toast.makeText(CategoriasActivity.this, "Acceso denegado a la camara.", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case MY_PERMISSION_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(CategoriasActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        LocationManager locationManager = (LocationManager) getSystemService (Context.LOCATION_SERVICE);
                        Location location = locationManager.getLastKnownLocation (LocationManager.NETWORK_PROVIDER);
                        TextView ciudad = (TextView) findViewById(R.id.ciudad);
                        try
                        {
                            ciudad.setText ("Ciudad: " + GetHereLocation(location.getLatitude(), location.getLongitude()));
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace ();
                            Toast.makeText (CategoriasActivity.this, "Not found!", Toast.LENGTH_SHORT).show();
                            ciudad.setText ("Ciudad no encontrada");
                        }
                    }
                }
                else
                {
                    Toast.makeText (this, "No permission granted!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void choosePhotoFromGallary() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageView imageView = findViewById(R.id.avatar);
        String path="";
        switch(requestCode) {
            case CAMERA:
                if(resultCode == RESULT_OK){
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    imageView.setImageBitmap(photo);
                    path = saveImage(photo);
                }

                break;
            case GALLERY:
                if(resultCode == RESULT_OK){
                    Uri selectedImageUri = imageReturnedIntent.getData();
                    path=getPath(selectedImageUri);
                    imageView.setImageURI(selectedImageUri);
                }
                break;
        }
        persistirAvatar(path);
    }

    private String getPath(Uri uri)
    {
        String[] projection={MediaStore.Images.Media.DATA};
        Cursor cursor=managedQuery(uri,projection,null,null,null);
        int column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void persistirAvatar(String path)
    {
        SharedPreferences sharedPreferences =
                CategoriasActivity.this.getSharedPreferences(getString(R.string.app_name),
                        Context.MODE_PRIVATE);
        String usuario = sharedPreferences.getString("usuario","");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profile_picture_" + usuario ,path);
        editor.commit();
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
        sharedPreferences.edit().remove("usuario").commit();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // get closest city name
    private String GetHereLocation (double lat, double lon)
    {
        String curCity = "";
        Geocoder geocoder = new Geocoder (CategoriasActivity.this, Locale.getDefault());
        List<Address> addressList;
        try
        {
            addressList = geocoder.getFromLocation (lat, lon, 1);
            if (addressList.size() > 0)
            {
                curCity = addressList.get(0).getLocality();
            }
            else
            {
                curCity = "City not found!";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return curCity;
    }
}

