package com.hhsfbla.launch;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

/**
 * A splash screen after the user has finished creating a fundraiser. On it, it gives the user several options, including
 * being able to share the new fundraiser on social media, add items for sale immediately into the fundraiser, or continue
 * fundraising at a later time.
 */

public class FundraiserCreatedSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Sets the user interface to the wanted layout
        setContentView(R.layout.activity_fundraiser_created_success);

        /**
         * Adds the clicking functionality to the social media button. After clicking, the user is allowed to selected several
         * social media platforms on which they want to share the fundraiser, including Facebook, Gmail, and Google Plus.
         */
        ((ImageButton) findViewById(R.id.man_in_party)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String message = intent.getStringExtra("extra");

                /**
                 * Creates a default message for the user to publish on their various social media platforms
                 */
                File localFile = null;
                localFile = new File(Environment.getExternalStorageDirectory().toString() + "/" +  "lastScreen.jpg");
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                StorageReference imageRef =  FirebaseStorage.getInstance().getReference().child("fundraisers/" + message + ".jpg");

                final File finalLocalFile = localFile;
                imageRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Uri imageUri = Uri.fromFile(finalLocalFile); //obtains the Uri value of the front page image

                                //adds the contents of the default message to the Intent that will send the user to the selected social media
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "I just created my fundraiser using Launch; join me in my efforts! #Launch");
                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                                shareIntent.setType("image/jpeg");
                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                startActivity(Intent.createChooser(shareIntent, "send")); //sends the user to the social media platform
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });


            }
        });

        /**
         * Adds the clicking functionality to the sell item button. Allows the user to begin selling items for
         * his or her fundraiser right away, as it leads to the creation of an item
         */
        ((ImageButton) findViewById(R.id.sell_imagebutton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchSellPage = new Intent(FundraiserCreatedSuccessActivity.this, CreateItemActivity.class);
                launchSellPage.putExtra("fid", getIntent().getStringExtra("extra")); //adds the newly created fundraiser's unique Firebase ID to the intent

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                launchSellPage.putExtra("uid", mAuth.getCurrentUser().getUid()); //retrieves the user's unique Firebase ID from the database and adds it to the intent

                startActivity(launchSellPage); //sends the user to the creation of an item
                finish();
            }
        });
        /**
         * Adds functionalty to go home if the user decides they do not want to act after creating a fundraiser
         */
        ((Button) findViewById(R.id.do_action_later_postsuccess)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goHome = new Intent(FundraiserCreatedSuccessActivity.this, NavDrawerActivity.class);
                startActivity(goHome);
            }
        });

    }
}
