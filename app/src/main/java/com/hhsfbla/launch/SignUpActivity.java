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

/**
 * The Activity that allows for user signup, and requests for the email, the name, and the password of the user
 */

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private FirebaseAuth mAuth; //the Firebase Authenticator
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /**
         * Establishes the toolbar at the top of the screen
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance(); //initializes the Authentication reference

        /**
         * Checks whether the user is currently signed in, and outputs information accordingly
         */
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

        /**
         * Retrieves the various views in the Activity
         */
        final EditText fullnameInput = (EditText) findViewById(R.id.fullname);
        final EditText emailInput = (EditText) findViewById(R.id.email);
        final EditText passwordInput = (EditText) findViewById(R.id.password);
        Button signupButton = (Button) findViewById(R.id.signup_button);

        /**
         * Sets the functionality of the sign up button
         */
        signupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fullnameInput.getText().length() > 0 && emailInput.getText().length() > 0 && passwordInput.length() > 0) { //if all the required fields are not empty
                    mAuth.createUserWithEmailAndPassword(emailInput.getText().toString(), passwordInput.getText().toString())  //and the creation is successful on the Firebase side
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {                                       //then a new user has been created
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Sign up failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        DatabaseReference database = FirebaseDatabase.getInstance().getReference(); //a new Firebase reference to store the new user

                                        /**
                                         * Stores the date of the new user into Firebase
                                         */
                                        database.child("users").child(task.getResult().getUser().getUid()).child("full_name").setValue(fullnameInput.getText().toString());
                                        database.child("users").child(task.getResult().getUser().getUid()).child("email").setValue(emailInput.getText().toString());
                                        database.child("users").child(task.getResult().getUser().getUid()).child("user_fundraisers").setValue(new ArrayList<String>());

                                        /**
                                         * Allows for convenient future access to the username and password
                                         * Also allows for automatic login
                                         */
                                        UsernameAndPasswordStorage thisUser = new UsernameAndPasswordStorage();
                                        thisUser.setUsername(emailInput.getText().toString());
                                        thisUser.setPassword(passwordInput.getText().toString());
                                        thisUser.storeCredentials();

                                        /**
                                         * Creates an Intent that sends the user to the introduction sequence
                                         */
                                        Intent gotoMainIntent = new Intent(SignUpActivity.this, IntroductionScene.class);
                                        gotoMainIntent.putExtra("id", task.getResult().getUser().getUid());
                                        SignUpActivity.this.startActivity(gotoMainIntent);
                                    }
                                }
                            });
                } else {
                    //if the user has not filled out all the fields, he or she is requested to do so
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
