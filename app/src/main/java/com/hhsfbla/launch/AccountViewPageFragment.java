package com.hhsfbla.launch;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

import static android.content.ContentValues.TAG;

/**
 * Account Settings page to view and edit account info
 * @author Varun
 */
public class AccountViewPageFragment extends Fragment {
    private View launchAccountPage;
    private FirebaseAuth mAuth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        launchAccountPage = inflater.inflate(R.layout.user_account_view_page, container, false);

        // Get user data with uid
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(user.getUid());
            myRef.child("full_name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // ...
                        ((TextView)launchAccountPage.findViewById(R.id.account_page_user_name)).setText(dataSnapshot.getValue().toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // ...
                }
            });
        // Get email
        myRef.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ...
                ((TextView)launchAccountPage.findViewById(R.id.account_page_user_email)).setText(dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
        // Edit password button
        launchAccountPage.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                DialogFragment dialog = new PasswordChangeDialog();
                dialog.show(getChildFragmentManager(), "FragmentTest");
            }
        });
        // Delete serialized user data
        launchAccountPage.findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                File file = new File("/sdcard/saveUserData.bin");
                boolean deleted = file.delete();
                Intent intent = new Intent(getActivity(), TitlescreenActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
//        ((TextView)launchAccountPage.findViewById(R.id.account_page_user_name)).setText(myRef.toString());
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.child("full_name").getValue().toString();
//                ((TextView)launchAccountPage.findViewById(R.id.account_page_user_name)).setText(value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
//        String s = database.getReference("users").child(user.getUid()).child("full_name").toString();
//        user.getUid();
        return launchAccountPage;

    }
}