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

/**
 * A series of screens that appears once the user has created a new account. Gives a brief overview of the
 * application, and shows the various functionalities that the app has.
 */

public class IntroductionScene extends AppCompatActivity {

    ViewFlipper vs; //helps flip between the introduction screens

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_scene);

        vs = (ViewFlipper) findViewById(R.id.view_switcher_intro);

        /**
         * The first introduction screen; shows the user a graphic of the basic use of the app
         */
        final ImageView intro1 = new ImageView(getBaseContext());
        intro1.setScaleType(ImageView.ScaleType.CENTER_CROP); //establishes how the screen will be resized
                                                              //here, it is resized using CENTER_CROP, meaning the width and the heigt are kept proportional
        intro1.setAdjustViewBounds(true); //allows the ImageView to be adjusted to fit various screen sizes
        intro1.setBackgroundResource(R.drawable.intro_page_1);

        /**
         * The second intrduction screen; shows the user how he or she can use the app
         */
        final ImageView intro2 = new ImageView(getBaseContext());
        intro2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        intro2.setAdjustViewBounds(true);
        intro2.setBackgroundResource(R.drawable.intro_page_2);

        /**
         * The third introduction screen; gives the user more details
         */
        final ImageView intro3 = new ImageView(getBaseContext());
        intro3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        intro3.setAdjustViewBounds(true);
        intro3.setBackgroundResource(R.drawable.intro_page_3);

        /**
         * Adds the three introduction screens to the ViewFlipper, in the order that they will flip in
         */
        vs.addView(intro1);
        vs.addView(intro2);
        vs.addView(intro3);

        /**
         * Retrieves the buttons that will allow the user to flip between the screens
         */
        final Button back = (Button) findViewById(R.id.back_button_intro);
        final Button next = (Button) findViewById(R.id.next_button_intro);
        final Button gotit = (Button) findViewById(R.id.gotit_button_intro);

        Window w = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //adjusts how the screens are displayed according to the available SDK functions
            w.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        //the user is encouraged to progress through all three introduction screens by removing the back button on the first screen
        if(vs.getCurrentView().equals(intro1) ){
            back.setVisibility(View.INVISIBLE);
            gotit.setVisibility(View.INVISIBLE);
        }

        /**
         * Establishes the functionality of the next button on the first and the second introduction screens
         */
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vs.getCurrentView().equals(intro1)) { //If the current screen is the first one, then the user is allowed to go to the second screen
                    vs.showNext(); //pushes the second screen to the front
                    back.setVisibility(View.VISIBLE);
                    gotit.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                }
                else if(vs.getCurrentView().equals(intro2)) { //If the current screen is the second one, then the user is allowed to go to the third and final screen
                    vs.showNext(); //pushes the third screen to the front
                    back.setVisibility(View.VISIBLE);
                    gotit.setVisibility(View.VISIBLE);
                    next.setVisibility(View.INVISIBLE);
                }
            }
        });

        /**
         * Establishes the functionality of the back button, which allows the user to flip back to previous introduction screens
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vs.getCurrentView().equals(intro2)) { //If the current screen is the second one, the user is allowed to go back to the first screen
                    vs.showNext(); //the showNext() function is called twice so the screen displayed loops back to the appropriate one
                    vs.showNext();
                    back.setVisibility(View.INVISIBLE);
                    gotit.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);

                }else if(vs.getCurrentView().equals(intro3)) { //If the current screen is the third one, the user is allowed to go back to the second screen
                    vs.showNext();
                    vs.showNext();
                    back.setVisibility(View.VISIBLE);
                    gotit.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);

                }
            }
        });

        /**
         * Establishes the functionality of the Got It button, and finishes the introduction of the app
         */
        gotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoMainIntent = new Intent(IntroductionScene.this, NavDrawerActivity.class); //sends the user to the home page of the app
                startActivity(gotoMainIntent);
                finish();
            }
        });

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            findViewById(R.id.view_switcher_intro).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

}
