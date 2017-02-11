package com.hhsfbla.launch;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class FundraiserCreatedSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fundraiser_created_success);
        ((ImageButton) findViewById(R.id.man_in_party)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String message = intent.getStringExtra("extra");
               // gs://launch-aae4e.appspot.com/fundreaisers/-Kcar8MB4CI3CBU6fr3d.jpg
                File localFile = null;
                localFile = new File(Environment.getExternalStorageDirectory().toString() + "/" +  "lastScreen.jpg");
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//                    DatabaseReference newRef = database.child("fundraisers").child(message);
                StorageReference imageRef =  FirebaseStorage.getInstance().getReference().child("fundraisers/" + message + ".jpg");

                final File finalLocalFile = localFile;
                imageRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Uri imageUri = Uri.fromFile(finalLocalFile);
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "I just created my fundraiser using Launch; join me in my efforts! #Launch");
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.setType("image/jpeg");
                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                startActivity(Intent.createChooser(shareIntent, "send"));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        System.exit(0);
                    }
                });


            }
        });
        ((ImageButton) findViewById(R.id.man_in_party)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String message = intent.getStringExtra("extra");
                // gs://launch-aae4e.appspot.com/fundreaisers/-Kcar8MB4CI3CBU6fr3d.jpg
                File localFile = null;
                localFile = new File(Environment.getExternalStorageDirectory().toString() + "/" +  "lastScreen.jpg");
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//                    DatabaseReference newRef = database.child("fundraisers").child(message);
                StorageReference imageRef =  FirebaseStorage.getInstance().getReference().child("fundraisers/" + message + ".jpg");

                final File finalLocalFile = localFile;
                imageRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Uri imageUri = Uri.fromFile(finalLocalFile);
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "I just created my fundraiser using Launch; join me in my efforts! #Launch");
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.setType("image/jpeg");
                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                startActivity(Intent.createChooser(shareIntent, "Share..."));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        System.exit(0);
                    }
                });


            }
        });
        ((ImageButton) findViewById(R.id.sell_imagebutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchSellPage = new Intent(FundraiserCreatedSuccessActivity.this, CreateItemActivity.class);
                launchSellPage.putExtra("fid", getIntent().getStringExtra("extra"));
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                launchSellPage.putExtra("uid", mAuth.getCurrentUser().getUid());
                startActivity(launchSellPage);
                finish();

            }
        });

    }
}
