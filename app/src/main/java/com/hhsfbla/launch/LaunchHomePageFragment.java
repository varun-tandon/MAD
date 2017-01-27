package com.hhsfbla.launch;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

/**
 * Created by Varun on 1/25/2017.
 */

public class LaunchHomePageFragment extends Fragment {
    private View launchHomePageView;
    private CarouselView carouselView;

    private int[] sampleImages = { R.drawable.button_style, R.drawable.button_style, R.drawable.button_style};

    private ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        launchHomePageView = inflater.inflate(R.layout.activity_launch_home, container, false);
        carouselView = (CarouselView)launchHomePageView.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);
        return launchHomePageView;
    }
}
