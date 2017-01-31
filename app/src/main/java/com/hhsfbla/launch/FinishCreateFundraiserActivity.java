package com.hhsfbla.launch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.R.id.message;
import static com.hhsfbla.launch.R.id.imageView;

public class FinishCreateFundraiserActivity extends AppCompatActivity {

    final static int SELECT_PHOTO = 1;
    private ImageView imagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_create_fundraiser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imagePreview = (ImageView) findViewById(R.id.imagePreview);

        Button chooseImageButton = (Button) findViewById(R.id.chooseImageButton);
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        Button launchButton = (Button) findViewById(R.id.launchFundraiserButton);
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = ((TextView) findViewById(R.id.descriptionField)).getText().toString();
                if (description.equals("")) {
                    Toast.makeText(FinishCreateFundraiserActivity.this, "Description cannot be blank", Toast.LENGTH_SHORT).show();
                }
                else {
                    Bundle data = FinishCreateFundraiserActivity.this.getIntent().getExtras();
                    Fundraiser fundraiser = new Fundraiser(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            data.getString("organizationName"), data.getString("purpose"),
                            data.getInt("goal"), data.getString("deadline"), description);
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    database.child("fundraisers").push().setValue(fundraiser);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imagePreview.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

}
