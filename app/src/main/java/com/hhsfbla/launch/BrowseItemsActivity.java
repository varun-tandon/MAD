package com.hhsfbla.launch;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.R.attr.colorBackground;
import static android.R.attr.width;

/**
 * Created by zhenfangchen on 2/6/17.
 */

public class BrowseItemsActivity extends AppCompatActivity{

    private View browseItemsView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private boolean empty = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.browse_items_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        browseItemsView = findViewById(R.id.browse_items_view);

        mRecyclerView = (RecyclerView) browseItemsView.findViewById(R.id.browse_items_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(BrowseItemsActivity.this));
        final ArrayList<Item> items = new ArrayList<Item>();

        final String fid = getIntent().getStringExtra("fid");

        final StorageReference storageRef = FirebaseStorage.getInstance().getReference("item");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("items");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    final Item f = child.getValue(Item.class);
                    if (f.fundraiserID.equals(fid)) {
                        storageRef.child(child.getKey() + ".jpg").getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 2;
                                f.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options));
                                items.add(f);
                                mAdapter = new ItemRecyclerViewAdapter(BrowseItemsActivity.this, items);
                                mRecyclerView.setAdapter(mAdapter);
                                if (mAdapter.getItemCount() > 0) {
                                    ((LinearLayout) findViewById(R.id.browse_items_empty)).removeAllViews();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
        addEmptyItemsMessage();
        mAdapter = new ItemRecyclerViewAdapter(BrowseItemsActivity.this, items);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void addEmptyItemsMessage() {
        if (empty) {
            TextView message = new TextView(BrowseItemsActivity.this);
            message.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            message.setText("Darn, seems like there is nothing for sale.");
            TextView message2 = new TextView(BrowseItemsActivity.this);
            message2.setText("");
            Button sellButton = new Button(BrowseItemsActivity.this);
            sellButton.setText("Sell an item!");
            sellButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent sellIntent = new Intent(BrowseItemsActivity.this, CreateItemActivity.class);
                    sellIntent.putExtra("uid", getIntent().getStringExtra("uid"));
                    sellIntent.putExtra("fid", getIntent().getStringExtra("fid"));
                    BrowseItemsActivity.this.startActivity(sellIntent);
                }
            });
//            mRecyclerView.setBackground(null);
            LinearLayout ll = (LinearLayout) findViewById(R.id.browse_items_empty);
            ll.setBackgroundColor(getResources().getColor(R.color.pb_white));
            ll.addView(message);
            ll.addView(message2);
            ll.addView(sellButton);

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
