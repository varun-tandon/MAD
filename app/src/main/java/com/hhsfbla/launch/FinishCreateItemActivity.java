package com.hhsfbla.launch;

import android.content.ActivityNotFoundException;
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
import android.util.Log;
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
    final static int PIC_CROP = 3;
    private ImageView imagePreview;
    private Bitmap imageBitmap;
    private StorageReference storageRef;
    private Uri imageUri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finish_create_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.item_finish_toolbar);
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

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String uid = intent.getStringExtra("uid");
        final String fid = intent.getStringExtra("fid");
        final String price = intent.getStringExtra("price");
        final String condition = intent.getStringExtra("condition");
        final String description = intent.getStringExtra("description");

        Log.d("TEST2", name + "  " + price + " "  +condition);

        Button launchButton = (Button) findViewById(R.id.launchItemButton);
        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Item item = new Item(uid, fid, name,
                            Double.parseDouble(price), condition, description,
                            imageBitmap != null ? true : false);
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference newRef = database.child("items").push();
                    String tempId = newRef.getKey();
                    item.setId(tempId);

                    newRef.setValue(item);

                    StorageReference imageRef = storageRef.child("item/" + newRef.getKey() + ".jpg");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap = ((BitmapDrawable)imagePreview.getDrawable()).getBitmap();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
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

                    Toast.makeText(FinishCreateItemActivity.this, "Item created", Toast.LENGTH_SHORT).show();

                    Intent gotoItemIntent = new Intent(FinishCreateItemActivity.this, ItemActivity.class);
                    gotoItemIntent.putExtra("id", tempId);
                    gotoItemIntent.putExtra("uid", uid);
                    gotoItemIntent.putExtra("fid", fid);
                    gotoItemIntent.putExtra("name", name);
                    gotoItemIntent.putExtra("price", price);
                    gotoItemIntent.putExtra("condition", condition);
                    gotoItemIntent.putExtra("description", description);

                    FinishCreateItemActivity.this.startActivity(gotoItemIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK) {
                    imageUri = imageReturnedIntent.getData();
                    crop(imageUri);
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    imagePreview.setImageBitmap(imageBitmap);
                    Toast.makeText(FinishCreateItemActivity.this, "Image taken", Toast.LENGTH_SHORT).show();
                }
                break;
            case PIC_CROP:
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imagePreview.setImageBitmap(selectedImage);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    Toast.makeText(FinishCreateItemActivity.this, "Image selected", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

        }
    }

    // from https://code.tutsplus.com/tutorials/capture-and-crop-an-image-with-the-device-camera--mobile-11458
    private void crop(Uri picUri) {
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 800);
            cropIntent.putExtra("outputY", 800);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
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
