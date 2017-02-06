package com.hhsfbla.launch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

/**
 * Created by zhenfangchen on 1/26/17.
 */

public class FundraiserActivity extends AppCompatActivity{

    private CarouselView carouselView;
    private int[] images = {R.drawable.button_style, R.drawable.button_style, R.drawable.button_style};

    protected String fid;

    protected String uid;

    protected String organizationName;
    protected String purpose;
    protected int goal;
    protected String deadline;
    protected String description;
    protected boolean hasImage;
    protected int amountRaised;

    protected Bitmap imageBitmap;

    protected void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);

        setContentView(R.layout.activity_launch_home);

        carouselView = (CarouselView) findViewById(R.id.fundraiserCarousel);
        carouselView.setPageCount(images.length);

        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(images[position]);
            }
        });

        Intent intent = getIntent();
        fid = intent.getStringExtra("fid");
        uid = intent.getStringExtra("uid");
        organizationName = intent.getStringExtra("organizationName");
        purpose = intent.getStringExtra("purpose");
        deadline = intent.getStringExtra("deadline");
        description = intent.getStringExtra("description");

        goal = intent.getIntExtra("goal", 0);
        amountRaised = intent.getIntExtra("amountRaised", 0);

        hasImage = intent.getBooleanExtra("hasImage", false);


    }

}
