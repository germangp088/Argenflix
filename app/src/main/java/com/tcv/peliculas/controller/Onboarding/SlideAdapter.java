package com.tcv.peliculas.controller.Onboarding;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tcv.peliculas.R;

public class SlideAdapter extends PagerAdapter{

    Context context;
    LayoutInflater layoutInflater;

    public SlideAdapter (Context context){
        this.context = context;
    }

    //Array - todas las imágenes que utilizará el onboarding
    public int[] slide_images = {
            R.drawable.argenflix_icon,
            R.drawable.catalogo_icon,
            R.drawable.estreno_icon,
            R.drawable.argenflix_icon
    };

    //Array - todos los títulos que se utilizarán
    public String[] slide_headings = {
            "BIENVENIDO",
            "ENTRETENIMIENTO DIARIO",
            "ESTRENOS SEMANALES",
            "COMIENZA YA"
    };

    //Array - todas las descripciones que se utilizarán
    public String[] slide_description = {
            "A la mejor aplicación para tu celular",
            "Encuentra un amplio catálogo de películas en nuestra app",
            "Disfruta de la película estreno de cada semana",
            "Continua para iniciar tu experiencia"
    };


    @Override
    public int getCount (){
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject (View view, Object o){
        return view == (RelativeLayout) o;
    }

    //Instancia los items
    @Override
    public Object instantiateItem (ViewGroup container, int position){
        // se crea un layoutInflater para usar el servicio del inflate
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        // se inicializan las imágenes y textos
        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_description);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_description[position]);

        container.addView(view);
        // se devuelve en vista cada slide
        return view;
    }

    @Override
    public void destroyItem (ViewGroup container, int position, Object object){
        container.removeView((RelativeLayout)object);
    }
}