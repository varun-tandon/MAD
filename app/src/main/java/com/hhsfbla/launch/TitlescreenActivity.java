package com.hhsfbla.launch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TitlescreenActivity extends AppCompatActivity {

    private View mContentView;

    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titlescreen);
        hide();
        mVisible = true;

        final Button signupButton = (Button) findViewById(R.id.titlescreen_signup_btn);
        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent signupIntent = new Intent(TitlescreenActivity.this, SignUpActivity.class);
                TitlescreenActivity.this.startActivity(signupIntent);
            }
        });

        final Button loginButton = (Button) findViewById(R.id.titlescreen_login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent loginIntent = new Intent(TitlescreenActivity.this, LoginActivity.class);
                TitlescreenActivity.this.startActivity(loginIntent);
            }
        });
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;
    }

}
