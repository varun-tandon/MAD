package com.hhsfbla.launch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by zhenfangchen on 1/26/17.
 */

public class YourFundraisersActivity extends AppCompatActivity {

    private String userID;
    private DatabaseReference databaseReference;

    private ArrayList<String> fundraiserIDs;

    protected void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);

        setContentView(R.layout.activity_your_fundraisers);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        userID = NavDrawerActivity.userID;

        fundraiserIDs = new ArrayList<String>();

        databaseReference.child("users").child(userID).child("user_fundraisers").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> changed = (ArrayList<String>) dataSnapshot.getValue();
                fundraiserIDs = new ArrayList<String>();
                for (String s: changed) {
                    fundraiserIDs.add(s);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        for (int i = 0; i < fundraiserIDs.size(); i++) {
            //String fundraiserName = databaseReference.child("fundraisers").child(fundraiserIDs.get(i));
        }

    }



}
