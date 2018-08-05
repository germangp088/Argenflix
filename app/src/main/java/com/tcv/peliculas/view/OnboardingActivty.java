package com.tcv.peliculas.view;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tcv.peliculas.R;

import com.tcv.peliculas.controller.Onboarding.SlideAdapter;

/*OnboardingActivty*/
public class OnboardingActivty extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private TextView[] mDots;

    // contiene los puntos por cada slide
    private SlideAdapter slideAdapter;

    private Button mNextButton;
    private Button mBackButton;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_layout);

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        mBackButton = (Button) findViewById(R.id.button_back);
        mNextButton = (Button) findViewById(R.id.button_next);

        //Inicializando el slideAdapter creado previamente
        slideAdapter = new SlideAdapter(this);
        // se agrega el adapter a la viewPager
        mSlideViewPager.setAdapter(slideAdapter);

        addDotsIndicator(0);
        // agregamos al OnPageChange el listener
        mSlideViewPager.addOnPageChangeListener(viewListener);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage + 1 < mDots.length) {
                    mSlideViewPager.setCurrentItem(mCurrentPage + 1);
                } else {
                    Intent intent = new Intent(OnboardingActivty.this,
                            CategoriasActivity.class);
                    OnboardingActivty.this.startActivity(intent);
                    OnboardingActivty.this.finish();
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage - 1 >= 0){
                    mSlideViewPager.setCurrentItem(mCurrentPage - 1);
                }
            }
        });
    }

    // cuenta la cantidad de items que necesitamos y los crea
    public void addDotsIndicator(int position){
        mDots = new TextView[4];
        mDotLayout.removeAllViews();

        for (int i=0; i<mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(40);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0)
        {
            mDots[position].setTextSize(45);
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    // gestionar el cambio de slide en los dots
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            // setea el currentPage a la pagina actual abierta
            mCurrentPage = position;

            if(position == 0){
                mNextButton.setEnabled(true);
                mNextButton.setVisibility(View.VISIBLE);
                mNextButton.setText("Next");

                mBackButton.setEnabled(false);
                mBackButton.setVisibility(View.INVISIBLE);
                mBackButton.setText("");
            } else if (position == mDots.length-1) {
                mBackButton.setText("Back");
                mNextButton.setText("Continuar");
            } else {
                mBackButton.setEnabled(true);
                mBackButton.setVisibility(View.VISIBLE);
                mBackButton.setText("Back");

                mNextButton.setEnabled(true);
                mNextButton.setEnabled(true);
                mNextButton.setText("Next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
