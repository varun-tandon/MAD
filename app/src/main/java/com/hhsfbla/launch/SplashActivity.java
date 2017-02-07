package com.hhsfbla.launch;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Varun on 2/5/2017.
 */

public class SplashActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(40, 61, 72));
        }
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                    .addOnCompleteListener(SplashActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            Log.d("tag", "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.


                                Intent gotoMainIntent = new Intent(SplashActivity.this, NavDrawerActivity.class);
                                gotoMainIntent.putExtra("id", task.getResult().getUser().getUid());
                                SplashActivity.this.startActivity(gotoMainIntent);

                        }
                    });
        }

        else {
            Intent intent = new Intent(this, TitlescreenActivity.class);
            startActivity(intent);
            finish();

        }
    }
}
