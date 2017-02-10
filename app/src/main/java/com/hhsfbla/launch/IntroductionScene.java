package com.hhsfbla.launch;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

public class IntroductionScene extends AppCompatActivity {
    ViewFlipper vs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_scene);

        vs = (ViewFlipper) findViewById(R.id.view_switcher_intro);

        hideSystemUI();

        final ImageView intro1 = new ImageView(getBaseContext());
        intro1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        intro1.setAdjustViewBounds(true);

        intro1.setBackgroundResource(R.drawable.intro_page_1);
        final ImageView intro2 = new ImageView(getBaseContext());
        intro2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        intro2.setAdjustViewBounds(true);
        intro2.setBackgroundResource(R.drawable.intro_page_2);
        final ImageView intro3 = new ImageView(getBaseContext());
        intro3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        intro3.setAdjustViewBounds(true);

        intro3.setBackgroundResource(R.drawable.intro_page_3);

        vs.addView(intro1);
        vs.addView(intro2);
        vs.addView(intro3);
        final Button back = (Button) findViewById(R.id.back_button_intro);
        final Button next = (Button) findViewById(R.id.next_button_intro);
        final Button gotit = (Button) findViewById(R.id.gotit_button_intro);
        Window w = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            w.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        if(vs.getCurrentView().equals(intro1) ){
            back.setVisibility(View.INVISIBLE);
            gotit.setVisibility(View.INVISIBLE);
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vs.getCurrentView().equals(intro1)) {
                    vs.showNext();
                    back.setVisibility(View.VISIBLE);
                    gotit.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                }
                else if(vs.getCurrentView().equals(intro2)) {
                    vs.showNext();
                    back.setVisibility(View.VISIBLE);
                    gotit.setVisibility(View.VISIBLE);
                    next.setVisibility(View.INVISIBLE);

                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vs.getCurrentView().equals(intro2)) {
                    vs.showNext();
                    vs.showNext();
                    back.setVisibility(View.INVISIBLE);
                    gotit.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);

                }else if(vs.getCurrentView().equals(intro3)) {
                    vs.showNext();
                    vs.showNext();
                    back.setVisibility(View.VISIBLE);
                    gotit.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);

                }
            }
        });
        gotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoMainIntent = new Intent(IntroductionScene.this, NavDrawerActivity.class);
                startActivity(gotoMainIntent);
                finish();
            }
        });



//        vs.showNext();


    }
    private void hideSystemUI(){
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        findViewById(R.id.view_switcher_intro).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
