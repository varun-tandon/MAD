package com.hhsfbla.launch;

import android.content.Intent;
import android.os.Bundle;
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


/**
 * An Activity that allows the user to login in using his or her email and password
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth; //the Firebase Authentication reference used to verify the user supplied credentials
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        /**
         * Sets the toolbar at the top of the screen
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance(); //initializes the Authentication reference

        /**
         * Checks whether the user is currently signed in, and outputs information accordingly
         */
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser(); //retrieves the current signed in user
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
        final EditText emailInput = (EditText) findViewById(R.id.login_email);
        final EditText passwordInput = (EditText) findViewById(R.id.login_password);
        final Button loginButton = (Button) findViewById(R.id.login_button);

        /**
         * Sets the functionality of the button
         */
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (emailInput.getText().length() > 0 && passwordInput.getText().length() > 0) {
                    mAuth.signInWithEmailAndPassword(emailInput.getText().toString(), passwordInput.getText().toString()) //if neither of the credentials fields are empty
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {             //and Firebase verifies that the inputted credentials are correct
                                @Override
                                public void onComplete(Task<AuthResult> task) {                                           //the login is a success
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Login failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        /**
                                         * Creates a helper object to simplify username and password storage for the current user
                                         */
                                        UsernameAndPasswordStorage thisUser = new UsernameAndPasswordStorage();
                                        thisUser.setUsername(emailInput.getText().toString());
                                        thisUser.setPassword(passwordInput.getText().toString());
                                        thisUser.storeCredentials(); //pushes the credentials of the current user into his or her device; allows for the user to be automatically signed back in

                                        Intent gotoMainIntent = new Intent(LoginActivity.this, NavDrawerActivity.class); //the user is sent to the home page of the app
                                        gotoMainIntent.putExtra("id", task.getResult().getUser().getUid());
                                        LoginActivity.this.startActivity(gotoMainIntent);
                                    }
                                }
                            });
                } else {
                    /**
                     * Depending on which field was left empty by the user, a reminder to fill out the appropriate field is given
                     */
                    if (emailInput.getText().length() > 0) {
                        Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    } else if (passwordInput.getText().length() > 0) {
                        Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Enter email and password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
