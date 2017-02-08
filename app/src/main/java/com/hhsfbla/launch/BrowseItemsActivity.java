package com.hhsfbla.launch;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by zhenfangchen on 2/6/17.
 */

public class BrowseItemsActivity extends AppCompatActivity{

    private View browseItemsView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

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

        final StorageReference storageRef = FirebaseStorage.getInstance().getReference("items");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("items");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    final Item f = child.getValue(Item.class);
                    if (f.uid.equals(NavDrawerActivity.userID)) {
                        storageRef.child(child.getKey() + ".jpg").getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 2;
                                f.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options));
                                items.add(f);
                                mAdapter = new ItemRecyclerViewAdapter(BrowseItemsActivity.this, items);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

        mAdapter = new ItemRecyclerViewAdapter(BrowseItemsActivity.this, items);
        mRecyclerView.setAdapter(mAdapter);
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
