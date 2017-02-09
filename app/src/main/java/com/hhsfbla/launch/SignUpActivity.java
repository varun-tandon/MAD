package com.hhsfbla.launch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // firebase told me to do this
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        final EditText fullnameInput = (EditText) findViewById(R.id.fullname);
        final EditText emailInput = (EditText) findViewById(R.id.email);
        final EditText passwordInput = (EditText) findViewById(R.id.password);
        Button signupButton = (Button) findViewById(R.id.signup_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fullnameInput.getText().length() > 0 && emailInput.getText().length() > 0 && passwordInput.length() > 0) {
                    mAuth.createUserWithEmailAndPassword(emailInput.getText().toString(), passwordInput.getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Sign up failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                                        DatabaseReference newReference = database.child("users").push();
                                        database.child("users").child(task.getResult().getUser().getUid()).child("full_name").setValue(fullnameInput.getText().toString());
                                        database.child("users").child(task.getResult().getUser().getUid()).child("email").setValue(emailInput.getText().toString());
//                                        database.child("users").child(task.getResult().getUser().getUid()).child("password").setValue(passwordInput.getText().toString());
                                        database.child("users").child(task.getResult().getUser().getUid()).child("user_fundraisers").setValue(new ArrayList<String>());
//                                        database.child("fish").setValue("Fish");
//                                        ((TextView) findViewById(R.id.nav_header_displayname)).setText(fullnameInput.getText().toString());
//                                        ((TextView) findViewById(R.id.nav_header_email)).setText(emailInput.getText().toString());
                                        UsernameAndPasswordStorage thisUser = new UsernameAndPasswordStorage();
                                        thisUser.setUsername(emailInput.getText().toString());
                                        thisUser.setPassword(passwordInput.getText().toString());
                                        thisUser.storeCredentials();
                                        Intent gotoMainIntent = new Intent(SignUpActivity.this, IntroductionScene.class);
                                        gotoMainIntent.putExtra("id", task.getResult().getUser().getUid());
                                        SignUpActivity.this.startActivity(gotoMainIntent);
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SignUpActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
