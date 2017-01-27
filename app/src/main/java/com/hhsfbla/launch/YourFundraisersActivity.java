package com.hhsfbla.launch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by zhenfangchen on 1/26/17.
 */

public class YourFundraisersActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);

        setContentView(R.layout.activity_your_fundraisers);

        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(YourFundraisersActivity.this, FundraiserActivity.class);
                YourFundraisersActivity.this.startActivity(intent);
            }
        });

    }

}
