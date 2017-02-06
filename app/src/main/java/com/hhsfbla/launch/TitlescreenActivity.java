package com.hhsfbla.launch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import android.Manifest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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
//        setContentView(R.layout.splash_screen_view);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    420);
        }
        File f = new File("/sdcard/saveUserData.bin");
        if(f.exists()){
            UsernameAndPasswordStorage storage = new UsernameAndPasswordStorage();
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                storage = (UsernameAndPasswordStorage) ois.readObject();


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(storage.getUsername(),storage.getPassword())
                    .addOnCompleteListener(TitlescreenActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            Log.d("tag", "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(TitlescreenActivity.this, "Login failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else {

                                Intent gotoMainIntent = new Intent(TitlescreenActivity.this, NavDrawerActivity.class);
                                gotoMainIntent.putExtra("id", task.getResult().getUser().getUid());
                                TitlescreenActivity.this.startActivity(gotoMainIntent);
                            }
                        }
                    });
        }else {
            setContentView(R.layout.activity_titlescreen);
            Window window = this.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.rgb(40, 61, 72));
            }
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
        }}

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;
    }

}
