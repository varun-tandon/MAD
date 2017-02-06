package com.hhsfbla.launch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.R.attr.bitmap;
import static android.R.id.message;
import static com.hhsfbla.launch.R.id.imageView;

public class FinishCreateFundraiserActivity extends AppCompatActivity {

    final static int SELECT_PHOTO = 1;
    private ImageView imagePreview;
    private Bitmap imageBitmap;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_create_fundraiser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        storageRef = FirebaseStorage.getInstance().getReference();
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
                            data.getInt("goal"), data.getString("deadline"), description, imageBitmap != null ? true : false);
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference newRef = database.child("fundraisers").push();
                    newRef.setValue(fundraiser);

                    StorageReference imageRef = storageRef.child("fundraisers/" + newRef.getKey() + ".jpg");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap = ((BitmapDrawable)imagePreview.getDrawable()).getBitmap();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                    byte[] imgData = baos.toByteArray();
                    UploadTask uploadTask = imageRef.putBytes(imgData);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(FinishCreateFundraiserActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Toast.makeText(FinishCreateFundraiserActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Toast.makeText(FinishCreateFundraiserActivity.this, "Fundraiser created", Toast.LENGTH_SHORT).show();

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
                        imageBitmap = BitmapFactory.decodeStream(imageStream);
                        Toast.makeText(FinishCreateFundraiserActivity.this, "Image selected", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
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
