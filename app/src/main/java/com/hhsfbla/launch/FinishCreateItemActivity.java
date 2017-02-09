package com.hhsfbla.launch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

/**
 * Created by zhenfangchen on 2/7/17.
 */

public class FinishCreateItemActivity extends AppCompatActivity {

    final static int SELECT_PHOTO = 1;
    final static int REQUEST_IMAGE_CAPTURE = 2;
    private ImageView imagePreview;
    private Bitmap imageBitmap;
    private StorageReference storageRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finish_create_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        storageRef = FirebaseStorage.getInstance().getReference();
        imagePreview = (ImageView) findViewById(R.id.item_imagePreview);

        imagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code sourced from https://developer.android.com/training/camera/photobasics.html
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE );
                }
            }
        });

        Button choosePicture = (Button) findViewById(R.id.chooseItemImageButton);
        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        Button launchButton = (Button) findViewById(R.id.launchItemButton);
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Bundle data = FinishCreateItemActivity.this.getIntent().getExtras();
                    Item item = new Item(data.getString("uid"),
                            data.getString("fid"), data.getString("name"),
                            Double.parseDouble(data.getString("price")), data.getString("condition"), data.getString("description"),
                            imageBitmap != null ? true : false);
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference newRef = database.child("items").push();
                    item.setId(newRef.getKey());

                    newRef.setValue(item);

                    StorageReference imageRef = storageRef.child("item/" + newRef.getKey() + ".jpg");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap = ((BitmapDrawable)imagePreview.getDrawable()).getBitmap();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                    byte[] imgData = baos.toByteArray();
                    UploadTask uploadTask = imageRef.putBytes(imgData);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(FinishCreateItemActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            Toast.makeText(FinishCreateItemActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Toast.makeText(FinishCreateItemActivity.this, "Fundraiser created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imagePreview.setImageBitmap(selectedImage);
                        imageBitmap = BitmapFactory.decodeStream(imageStream);
                        Toast.makeText(FinishCreateItemActivity.this, "Image selected", Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    imagePreview.setImageBitmap(imageBitmap);
                    Toast.makeText(FinishCreateItemActivity.this, "Image taken", Toast.LENGTH_SHORT).show();
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
