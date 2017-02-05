package com.hhsfbla.launch;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        //make <a> in the textview clickable
        ((TextView) launchHomePageView.findViewById(R.id.homepage_campaign_descrption)).setMovementMethod(LinkMovementMethod.getInstance());

//        TextViewExpandableAnimation tvExpand = (TextViewExpandableAnimation) launchHomePageView.findViewById(R.id.tv_expand);
//        tvExpand.setText("Description: Lorem jhafldsfhl ash kfhlajkd hfl k ajsh lfhaslkh fashd khslkfhkjsjdhfhskjhfkhdhfh hf hdk fdh kh jkhfdjkhkfhdk j  jdh kjdhkdhkfj hkdj hkjh dhfkjdhk jfhk hkjh hdhkjdh khkdhkjfhdjk dkfhkd fk jfhdjhkhkjh hd kh fkhfkj ipsum dolor sit amet, consectetur adipiscing elit. Proin felis mi, dapibus eget quam pharetra, tristique auctor elit. Proin non mollis sapien, eu maximus sem. Vestibulum aliquet pellentesque suscipit. Phasellus venenatis leo vitae massa mollis, sodales commodo massa aliquet. Aliquam malesuada lectus turpis, at consequat dui cursus at. ");
        return launchHomePageView;
    }
}
