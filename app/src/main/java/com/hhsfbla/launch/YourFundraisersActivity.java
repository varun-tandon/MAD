package com.hhsfbla.launch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
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
    protected DatabaseReference databaseReference;

    protected ArrayList<Fundraiser> fundraisers;

    protected void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);

        setContentView(R.layout.activity_your_fundraisers);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        userID = NavDrawerActivity.userID;

        fundraisers = new ArrayList<Fundraiser>();

        databaseReference.child("users").child(userID).child("user_fundraisers").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<String> changed = (ArrayList<String>) dataSnapshot.getValue(); //new user fundraisers

                fundraisers = new ArrayList<Fundraiser>();

                databaseReference.child("fundraisers").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Fundraiser current = (Fundraiser) dataSnapshot.getValue();
                        if (changed.contains(current.uid)) {
                            fundraisers.add(current);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LinearLayout ll = (LinearLayout) findViewById(R.id.fundraisers_layout);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < fundraisers.size(); i++) {
            final Button temp = new Button(this);
            temp.setText(fundraisers.get(i).uid);

            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(YourFundraisersActivity.this, FundraiserActivity.class);
                    intent.putExtra("fund_id", temp.getText().toString());
                    YourFundraisersActivity.this.startActivity(intent);
                }
            });

            ll.addView(temp, lp);
        }

    }



}
