package com.hhsfbla.launch;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Varun on 1/25/2017.
 */

public class FundraiserFragment extends Fragment {

    private View fundraiserView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fundraiserView = inflater.inflate(R.layout.fragment_fundraiser, container, false);

        // populate page with data
        final Bundle data = getArguments();
        ImageView fundraiserImg = (ImageView) fundraiserView.findViewById(R.id.fundraiser_image);
        fundraiserImg.setImageBitmap((Bitmap) data.getParcelable("bitmap"));
        setTextForTextView(R.id.campaign_title_textview, data.getString("purpose"));
        setTextForTextView(R.id.nonprofit_org_name_textview, data.getString("orgname"));
        setTextForTextView(R.id.homepage_campaign_description, data.getString("description"));
        setTextForTextView(R.id.progress_text, data.getString("progressText"));
        final RoundCornerProgressBar progressBar = (RoundCornerProgressBar)
                fundraiserView.findViewById(R.id.fundraiser_progressbar);

        final String fid = data.getString("fid");
        final String uid = data.getString("uid");
        final int amountRaised = data.getInt("amountRaised");

        // update progress bar
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("fundraisers/" + fid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int amountRaised = dataSnapshot.child("amountRaised").getValue(Integer.class);
                int goal = dataSnapshot.child("goal").getValue(Integer.class);
                progressBar.setProgress(amountRaised * 100f / goal);
                setTextForTextView(R.id.progress_text, "$" + amountRaised + " raised of $" + goal);
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });


        LinearLayout buy = (LinearLayout) fundraiserView.findViewById(R.id.buy_button);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent buyIntent = new Intent(getActivity(), BrowseItemsActivity.class);
                buyIntent.putExtra("uid", uid);
                buyIntent.putExtra("fid", fid);
                getActivity().startActivity(buyIntent);
            }
        });

        LinearLayout donateButton = (LinearLayout) fundraiserView.findViewById(R.id.donate_button);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent donateIntent = new Intent(getActivity(), DonateActivity.class);
                donateIntent.putExtra("uid", uid);
                donateIntent.putExtra("fid", fid);
                donateIntent.putExtra("amountRaised", amountRaised);
                getActivity().startActivity(donateIntent);
            }
        });

        LinearLayout sellButton = (LinearLayout) fundraiserView.findViewById(R.id.sell_button);
        sellButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sellIntent = new Intent(getActivity(), CreateItemActivity.class);
                sellIntent.putExtra("uid", uid);
                sellIntent.putExtra("fid", fid);
                getActivity().startActivity(sellIntent);
            }
        });

        data.clear();
        return fundraiserView;
    }

    public void setTextForTextView(int id, String text) {
        View v = fundraiserView.findViewById(id);
        if (v instanceof TextView) {
            ((TextView) v).setText(text);
        }
    }
}