package com.hhsfbla.launch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zhenfangchen on 2/7/17.
 */

public class ItemActivity extends AppCompatActivity{

    private String fid, uid;

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
        itemImage.setImageBitmap((Bitmap) intent.getParcelableExtra("image"));

        ((TextView)findViewById(R.id.item_name)).setText(intent.getStringExtra("name"));
        ((TextView)findViewById(R.id.item_price)).setText(intent.getStringExtra("price"));
        ((TextView)findViewById(R.id.item_description)).setText(intent.getStringExtra("description"));

        Button condition = (Button) findViewById(R.id.item_condition);
        if (intent.getStringExtra("condition").equals("Poor")) {
            condition.setText("Poor");
            condition.setTextColor(Color.RED);
        } else if (intent.getStringExtra("condition").equals("Good")) {
            condition.setText("Good");
            condition.setTextColor(Color.YELLOW);
        } else if (intent.getStringExtra("condition").equals("New")) {
            condition.setText("New");
            condition.setTextColor(Color.GREEN);
        }

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
