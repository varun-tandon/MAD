package com.hhsfbla.launch;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
 * A page that hosts all the fundraisers that the user has created. Allows the user to click into
 * each individual fundraiser
 */

public class YourFundraisersPageFragment extends Fragment {
    private View yourFundraiserView;
    private RecyclerView mRecyclerView; //the container that holds all the individual fundraisers
    private RecyclerView.Adapter mAdapter; //the adapter that creates the individual fundraiser views

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        yourFundraiserView = inflater.inflate(R.layout.fragment_your_fundraisers, container, false);

        /**
         * Initializes the container
         */
        mRecyclerView = (RecyclerView) yourFundraiserView.findViewById(R.id.your_fundraisers_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final ArrayList<Fundraiser> fundraisers = new ArrayList<Fundraiser>(); //stores all of the users fundraisers

        final StorageReference storageRef = FirebaseStorage.getInstance().getReference("/fundraisers"); //retrieves a reference to the fundraiser images
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/fundraisers"); //retrieves a reference to the fundraiser objects
        ref.addValueEventListener(new ValueEventListener() { //updates whenever a change is made to a fundraiser
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fundraisers.clear();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    final Fundraiser f = child.getValue(Fundraiser.class); //retrieves the Fundraiser
                    if (f.uid.equals(NavDrawerActivity.userID)) { //if the current user is the creator of the Fundraiser
                        f.setId(child.getKey());
                        /**
                         * Retrieves the image of the fundraiser from FirebaseStorage and passes it into the fundraiser object
                         */
                        storageRef.child(child.getKey() + ".jpg").getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 2;
                                f.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options));
                                fundraisers.add(f);

                                /**
                                 * Creates the container for the new fundraiser to be displayed in
                                 */
                                mAdapter = new FundraiserRecyclerViewAdapter(getActivity(), fundraisers);
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

        mAdapter = new FundraiserRecyclerViewAdapter(getActivity(), fundraisers);
        mRecyclerView.setAdapter(mAdapter);
        return yourFundraiserView;
    }
}
