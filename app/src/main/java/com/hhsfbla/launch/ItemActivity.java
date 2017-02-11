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
import android.view.Gravity;
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
 * A page that displays an Item that has been posted for sale. On the page is a brief summary of the
 * item available for purchase, including an image of the Item, the name of the Item, an asking price
 * for the Item, the condition of the Item, the seller of the Item, a description of the Item, and options
 * to buy or comment on the Item
 */

public class ItemActivity extends AppCompatActivity{

    private String fid, uid, id; //backend Firebase IDs for the fundraiser, the seller, and the item respectively

    private DatabaseReference databaseReference; //a DatabaseReference that connects with Firebase to retrieve required data

    private Fundraiser fundraiser; //the Fundraiser that the item is contained in

    private String sellerName; //the name of the seller
    private Bitmap bitmap; //the image of the item, stored as a Bitmap
    private int numComments = 0; //the number of comments on the item

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        /**
         * Establishes functionality for the toolbar, including allowing the user to navigate back to the previous page
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.item_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent(); //retrieves the Intent that sent the user to the Item page

        /**
         * Initializes the backend IDs
         */
        id = intent.getStringExtra("id");
        fid = intent.getStringExtra("fid");
        uid = intent.getStringExtra("uid");

        ImageView itemImage = (ImageView)findViewById(R.id.item_picture); //retrieves the View that will host the Item image
        final Uri imageUri = (Uri)intent.getExtras().get(Intent.EXTRA_STREAM); //obtains the Uri of the image from the passed in data
        try {
            /**
             * Uses the supplied Uri to retrieve the image stored on the user's device, and sets the image displayed by the ImageView equal to the retrieved picture
             */
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            itemImage.setImageBitmap(bitmap);

            /**
             * Sets the ImageView background to the average color of all the image pixels
             * */
            int color = Bitmap.createScaledBitmap(bitmap, 1, 1, false).getPixel(0, 0);
            itemImage.setBackgroundColor(color);
        } catch (IOException io) { //if the Uri passed in was invalid, an error is caught
            io.printStackTrace();
        }

        /**
         * Retrieves data about the selected Item from the Intent
         */
        final String itemName = intent.getStringExtra("name");
        final String price = intent.getStringExtra("price");
        final String description = intent.getStringExtra("description");
        /**
         * Inserts the retrieved data into their associated TextViews
         */
        ((TextView)findViewById(R.id.item_name)).setText(itemName);
        ((TextView)findViewById(R.id.item_price)).setText(price);
        ((TextView)findViewById(R.id.item_description)).setText(description);

        /**
         * Retrieves the button associated with the condition of the Item
         * Adjusts the color background of the button according to the condition of the Item
         */
        Button condition = (Button) findViewById(R.id.item_condition);
        String conditionText = intent.getStringExtra("condition");
        if (conditionText.equals("Poor")) { //if the Item is in poor condition, the button background is set to red
            condition.setBackgroundColor(getResources().getColor(R.color.pb_red_dark));
        } else if (conditionText.equals("New") || conditionText.contains("Good") ||
                conditionText.contains("Like New")) { //if the Item is in a good or a new condition, the button background is set to green
            condition.setBackgroundColor(getResources().getColor(R.color.pb_green));
        } else if (conditionText.equals("Acceptable")) { //if the Item is in an acceptable condition, the button background is set to orange
            condition.setBackgroundColor(getResources().getColor(R.color.pb_orange));
        }
        condition.setText(conditionText);

        databaseReference = FirebaseDatabase.getInstance().getReference("fundraisers/" + fid); //initializes the databaseReference to the Fundraiser with which the Item is associated
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() { //retrieves data from the appropriate Fundraiser once
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fundraiser = dataSnapshot.getValue(Fundraiser.class); //gets the wanted Fundraiser object
                /**
                 * The fundraiser name and purpose are displayed on the Item page
                 */
                ((TextView)findViewById(R.id.item_fundraiser_name)).setText(fundraiser.organizationName);
                ((TextView)findViewById(R.id.item_fundraiser_name))
                        .setText(fundraiser.purpose + " fundraiser hosted by "
                                + fundraiser.organizationName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("items").child(id).child("uid"); //initializes the databaseReference to the current Item
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() { //retrieves data from the appropriate Fundraiser once
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String owner_uid = dataSnapshot.getValue(String.class); //gets the wanted Item object
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                        .child(owner_uid).child("full_name"); //a new DatabaseReference is connected to the seller's name
                ref.addListenerForSingleValueEvent(new ValueEventListener() { //a single data retrieval is used to obtain the seller's name
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sellerName = dataSnapshot.getValue(String.class);
                        ((TextView)findViewById(R.id.item_seller_name)).setText(sellerName); //the seller's name is displayed on the Item page
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        /**
         * Adds functionality to the "Buy" button
         */
        Button buy = (Button) findViewById(R.id.item_buy);
        final Bitmap imageBitmap = bitmap;
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent buyIntent = new Intent(ItemActivity.this, BuyItemActivity.class); //sends the user to the page where he or she can purchase the item
                /**
                 * Passes the relevant data to the next page
                 */
                buyIntent.putExtra("fid", fid);
                buyIntent.putExtra("amount", price);
                buyIntent.putExtra("itemName", itemName);
                buyIntent.putExtra("image", imageBitmap);
                ItemActivity.this.startActivity(buyIntent);
            }
        });

        final Button submitComment = (Button) findViewById(R.id.item_submitComment); //retrieves the Button that submits the entered comment into the database

        final EditText addComment = (EditText) findViewById(R.id.item_addComment); //allows the user to enter in his or her comment

        /**
         * Adds click functionality to submitting a comment
         */
        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((EditText)(findViewById(R.id.item_addComment))).getText().length() == 0) { //does not allow the user to submit his or her comment if there is no comment

                } else {
                    final DatabaseReference itemReference = FirebaseDatabase.getInstance().getReference("items").child(id); //retrieves a reference to the current Item
                    itemReference.addListenerForSingleValueEvent(new ValueEventListener() { //retrieves data from the current Item once
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Item i = dataSnapshot.getValue(Item.class); //gets the current Item

                            /**
                             * Helps keep the comments in the order of which they were entered
                             */
                            numComments = i.numOfComments; //sets the current number of comments to the associated counter in the Item object
                            numComments += 1; //increments the number of comments, as a new comment has been added to the Item
                            itemReference.child("numOfComments").setValue(numComments); //pushes the new number of comments to Firebase

                            //retrieves a reference to the current user's name
                            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(NavDrawerActivity.userID).child("full_name");
                            userReference.addListenerForSingleValueEvent(new ValueEventListener() { //retrieves data from the current user once
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String currentName = (String) dataSnapshot.getValue(); //obtains the current user's name

                                    databaseReference = FirebaseDatabase.getInstance().getReference("comments"); //retrieves a reference to the comments folder of the Firebase database

                                    //creates a new ItemComment that takes in the data of the user that made the comment and the comment itself
                                    ItemComment comment = new ItemComment(NavDrawerActivity.userID, currentName, id, ((EditText)findViewById(R.id.item_addComment)).getText().toString(), numComments);

                                    DatabaseReference newReference = databaseReference.push(); //creates a new slot in which the new ItemComment can be placed
                                    newReference.setValue(comment); //sets the new slot to the newly created ItemComment

                                    hideSoftKeyboard(ItemActivity.this, addComment); //calls the helper function to close the keyboard once the user has submitted his or her comment

                                    /**
                                     * Hides the submit comment button until the user again decides to add a comment
                                     */
                                    submitComment.setClickable(false);
                                    submitComment.setVisibility(View.INVISIBLE);

                                    addComment.setText(""); //resets the comment editor to an empty String
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        /**
         * Adds click functionality to the comment text editor
         */
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * Once the editor has been selected, it is a given a new background
                 */
                GradientDrawable editD = (GradientDrawable) addComment.getBackground();
                editD.setColor(Color.WHITE);

                /**
                 * Makes the submit comment button visible and clickable; this way, the user can submit his or her comment
                 */
                submitComment.setVisibility(View.VISIBLE);
                submitComment.setClickable(true);
            }
        });

        //before readding all the stored comments, the existing Views that host all the comments are removed
        ((LinearLayout)findViewById(R.id.item_comments)).removeAllViews();

        databaseReference = FirebaseDatabase.getInstance().getReference("comments"); // a reference to all the comments in the database
        databaseReference.addValueEventListener(new ValueEventListener() { //whenever a new comment is supplied, the reference updates
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((LinearLayout)findViewById(R.id.item_comments)).removeAllViews(); //whenever new comments are added, the old ones must be cleared away

                /**
                 * Stores all the comments that are associated with the current Item
                 */
                ArrayList<ItemComment> comments = new ArrayList<ItemComment>();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    final ItemComment comment = data.getValue(ItemComment.class);
                    if (comment.itemID.equals(id)) { //if the comment that is currently being checked has a itemID that is the same as the current Item, the comment is added to the list
                        comments.add(comment);
                    }
                }

                /**
                 * The comments list is sorted so that the most recent ones appear last
                 */
                Collections.sort(comments, new Comparator<ItemComment>() {
                    @Override
                    public int compare(ItemComment itemComment, ItemComment t1) {
                        return Integer.compare(itemComment.order, t1.order);
                    }
                });

                /**
                 * Draws each comment onto the screen
                 */
                for (int i = 0; i < comments.size(); i++) {
                    ItemComment comment = comments.get(i);

                    /**
                     * A different comment look is displayed if the comment was written by the current user
                     */
                    if (!comment.uid.equals(NavDrawerActivity.userID)) { //if the comment was not written by the current user
                        /**
                         * Creates a layout that will hold the comment
                         */
                        LinearLayout newComment = new LinearLayout(ItemActivity.this);
                        newComment.setOrientation(LinearLayout.VERTICAL);
                        /**
                         * Creates layout parameters that will determine the width and the height of the comment, along with the margins around it
                         */
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(700, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, 25, 0, 0);
                        newComment.setLayoutParams(layoutParams);

                        /**
                         * Creates a TextView that displays which user made the comment
                         */
                        TextView name = new TextView(ItemActivity.this);
                        name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        name.setText(comment.uName + " said:");
                        newComment.addView(name);

                        /**
                         * Creates a TextView that displays the comment
                         */
                        TextView text = new TextView(ItemActivity.this);
                        text.setBackgroundResource(R.drawable.comment); //the TextView is set to a specific background
                        /**
                         * Sets the layout parameters such that the comment stretches horizontally to fill the comment layout
                         */
                        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        textParams.setMargins(0, 10, 0, 0);
                        text.setPadding(20, 20, 20, 20);
                        text.setLayoutParams(textParams);
                        text.setText(comment.text);
                        newComment.addView(text);

                        /**
                         * Adds the comment to the layout that holds all the comments
                         */
                        LinearLayout view_comments = (LinearLayout) findViewById(R.id.item_comments);
                        view_comments.addView(newComment);
                    } else {
                        //Uses much of the same logic to display comments made by the current user

                        LinearLayout newComment = new LinearLayout(ItemActivity.this);
                        newComment.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(700, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.gravity = Gravity.RIGHT; //comments made by the current user are displayed on the right side of the screen to differentiate between the two
                        layoutParams.setMargins(0, 25, 0, 0);
                        newComment.setLayoutParams(layoutParams);

                        TextView text = new TextView(ItemActivity.this);
                        text.setBackgroundResource(R.drawable.own_item_comments); //sets the background of the comment to a different one, allowing the user to further differentiate between the two
                        text.setTextColor(Color.WHITE);
                        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        textParams.setMargins(0, 10, 0, 0);
                        textParams.gravity = Gravity.RIGHT;
                        text.setPadding(20, 20, 20, 20);
                        text.setLayoutParams(textParams);
                        text.setText(comment.text);

                        newComment.addView(text);

                        LinearLayout view_comments = (LinearLayout) findViewById(R.id.item_comments);
                        view_comments.addView(newComment);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Establishes functionality for the Toolbar, such as allowing the user to navigate to the previously displayed screen
     * @param item the option that is selected
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //if the back button is selected
                this.finish();      //the user is sent back to the previous screen
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A helper class that hides the keyboard after the user is done entering his or her comment
     * @param activity the Activity that holds the current screen
     * @param view
     */
    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
}
