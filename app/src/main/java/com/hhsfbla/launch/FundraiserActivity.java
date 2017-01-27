package com.hhsfbla.launch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

/**
 * Created by zhenfangchen on 1/26/17.
 */

public class FundraiserActivity extends AppCompatActivity{

    private CarouselView carouselView;
    private int[] images = {R.drawable.button_style, R.drawable.button_style, R.drawable.button_style};

    protected void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);

        setContentView(R.layout.activity_fundraiser);

        carouselView = (CarouselView) findViewById(R.id.fundraiserCarousel);
        carouselView.setPageCount(images.length);

        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(images[position]);
            }
        });


    }

}
