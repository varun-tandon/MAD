package com.hhsfbla.launch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

/**
 * Created by zhenfangchen on 2/7/17.
 */

public class ItemActivity extends AppCompatActivity{

    private String fid, uid;

    private DatabaseReference databaseReference;

    private Fundraiser fundraiser;

    private String sellerName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.item_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        fid = intent.getStringExtra("fid");
        uid = intent.getStringExtra("uid");

        ImageView itemImage = (ImageView)findViewById(R.id.item_picture);
        Uri imageUri = (Uri)intent.getExtras().get(Intent.EXTRA_STREAM);
        Bitmap bitmap;
        try {
           bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            itemImage.setImageBitmap(bitmap);
        } catch (IOException io) {
            io.printStackTrace();
        }

        ((TextView)findViewById(R.id.item_name)).setText(intent.getStringExtra("name"));
        ((TextView)findViewById(R.id.item_price)).setText(intent.getStringExtra("price"));
        ((TextView)findViewById(R.id.item_description)).setText(intent.getStringExtra("description"));

        Button condition = (Button) findViewById(R.id.item_condition);
        Log.d("TEST4", intent.getStringExtra("condition") + "fasdf");
        if (intent.getStringExtra("condition").equals("Bad")) {
            condition.setText("Poor");
            condition.setTextColor(Color.RED);
        } else if (intent.getStringExtra("condition").equals("Good")) {
            condition.setText("Good");
            condition.setTextColor(Color.YELLOW);
        } else if (intent.getStringExtra("condition").equals("New")) {
            condition.setText("New");
            condition.setTextColor(Color.GREEN);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("fundraisers").child(fid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fundraiser = dataSnapshot.getValue(Fundraiser.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("full_name");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sellerName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button buy = (Button) findViewById(R.id.item_buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
