package com.hhsfbla.launch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by zhenfangchen on 2/7/17.
 */

public class ItemActivity extends AppCompatActivity{

    private String fid, uid, id;

    private DatabaseReference databaseReference;

    private Fundraiser fundraiser;

    private String sellerName;
    private int numComments = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.item_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
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
        String conditionText = intent.getStringExtra("condition");
        if (conditionText.equals("Poor")) {
            condition.setBackgroundColor(getResources().getColor(R.color.pb_red_dark));
        } else if (conditionText.equals("New") || conditionText.contains("Good") ||
                conditionText.contains("Like New")) {
            condition.setBackgroundColor(getResources().getColor(R.color.pb_green));
        } else if (conditionText.equals("Acceptable")) {
            condition.setBackgroundColor(getResources().getColor(R.color.pb_orange));
        }
        condition.setText(conditionText);

<<<<<<< HEAD
        databaseReference = FirebaseDatabase.getInstance().getReference("fundraisers").child(fid);
=======
        databaseReference = FirebaseDatabase.getInstance().getReference("fundraisers/" + fid);
>>>>>>> origin/master
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fundraiser = dataSnapshot.getValue(Fundraiser.class);
<<<<<<< HEAD

                ((TextView)findViewById(R.id.item_fundraiser_name)).setText(fundraiser.organizationName);
                ((ImageView)findViewById(R.id.item_seller_picture)).setImageBitmap(fundraiser.imageBitmap);
=======
                ((TextView)findViewById(R.id.item_fundraiser_name))
                        .setText(fundraiser.purpose + " fundraiser hosted by "
                                + fundraiser.organizationName);
>>>>>>> origin/master
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("full_name");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
<<<<<<< HEAD
                sellerName = (String) dataSnapshot.getValue();
=======
                sellerName = dataSnapshot.getValue(String.class);
>>>>>>> origin/master
                ((TextView)findViewById(R.id.item_seller_name)).setText(sellerName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

<<<<<<< HEAD

=======
>>>>>>> origin/master
        Button buy = (Button) findViewById(R.id.item_buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        final Button submitComment = (Button) findViewById(R.id.item_submitComment);

        final EditText addComment = (EditText) findViewById(R.id.item_addComment);

        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((EditText)(findViewById(R.id.item_addComment))).getText().length() == 0) {

                } else {
                    DatabaseReference itemReference = FirebaseDatabase.getInstance().getReference("items").child(id);
                    itemReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Item i = dataSnapshot.getValue(Item.class);
                            numComments = i.numOfComments;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    numComments += 1;
                    itemReference.child("numOfComments").setValue(numComments);

                    databaseReference = FirebaseDatabase.getInstance().getReference("comments");
                    ItemComment comment = new ItemComment(NavDrawerActivity.userID, sellerName, id, ((EditText)findViewById(R.id.item_addComment)).getText().toString(), numComments);
                    DatabaseReference newReference = databaseReference.push();
                    newReference.setValue(comment);
                    hideSoftKeyboard(ItemActivity.this, addComment);
                    submitComment.setClickable(false);
                    submitComment.setVisibility(View.INVISIBLE);
                    addComment.setText("");
                }
            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GradientDrawable editD = (GradientDrawable) addComment.getBackground();
                editD.setColor(Color.WHITE);

                submitComment.setVisibility(View.VISIBLE);
                submitComment.setClickable(true);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("comments");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((LinearLayout)findViewById(R.id.item_comments)).removeAllViews();

                ArrayList<ItemComment> comments = new ArrayList<ItemComment>();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    final ItemComment comment = data.getValue(ItemComment.class);
                    if (comment.itemID.equals(id)) {
                        comments.add(comment);
                    }
                }

                Collections.sort(comments, new Comparator<ItemComment>() {
                    @Override
                    public int compare(ItemComment itemComment, ItemComment t1) {
                        return Integer.compare(itemComment.order, t1.order);
                    }
                });

                for (int i = 0; i < comments.size(); i++) {
                    ItemComment comment = comments.get(i);

                    LinearLayout newComment = new LinearLayout(ItemActivity.this);
                    newComment.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1100, LinearLayout.LayoutParams.WRAP_CONTENT);
                    newComment.setLayoutParams(layoutParams);

                    TextView name = new TextView(ItemActivity.this);
                    name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    name.setText(comment.uName + " said:");

                    newComment.addView(name);

                    TextView text = new TextView(ItemActivity.this);
                    text.setBackgroundResource(R.drawable.comment);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    textParams.setMargins(0, 10, 0, 0);
                    text.setPadding(20, 20, 20, 20);
                    text.setLayoutParams(textParams);
                    text.setText(comment.text);

                    newComment.addView(text);

                    LinearLayout view_comments = (LinearLayout) findViewById(R.id.item_comments);
                    view_comments.addView(newComment);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
}
