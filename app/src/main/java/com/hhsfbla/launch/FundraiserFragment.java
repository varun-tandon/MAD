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
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A page that displays the fundraiser that the user has selected. On the page is a brief summary of the
 * fundraiser, including its name, the organization that created it, a brief description of why other
 * users should donate to it, how much money that has been currently raised, the target money goal, and
 * the number of days left in the fundraiser. In addition, there are several buttons that leads the users
 * to a variety of tasks they can perform to support the fundraiser, such as purchasing items from the fundraiser,
 * donating directly to the fundraiser, and selling items for the fundraiser.
 */

public class FundraiserFragment extends Fragment {

    private View fundraiserView; //the container that hosts the user interface of the page

    /**
     * A default method that establishes the default behavior of the page
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fundraiserView = inflater.inflate(R.layout.fragment_fundraiser, container, false);

        final Bundle data = getArguments(); //contains all the fundraiser data

        /**
         * Retrieves a image of the fundraiser and displays it
         */
        Bitmap bitmap = (Bitmap)data.getParcelable("bitmap");
        ImageView fundraiserImg = (ImageView) fundraiserView.findViewById(R.id.fundraiser_image);
        fundraiserImg.setImageBitmap(bitmap);

        /**
         * Colors the background of the fundraiser image with the average RGB value of the pixels within the image
         */
        int color = Bitmap.createScaledBitmap(bitmap, 1, 1, false).getPixel(0, 0);
        fundraiserImg.setBackgroundColor(color);

        /**
         * Displays all the relevant information onto the page
         */
        setTextForTextView(R.id.campaign_title_textview, data.getString("purpose"));
        setTextForTextView(R.id.nonprofit_org_name_textview, data.getString("orgname"));
        setTextForTextView(R.id.homepage_campaign_description, data.getString("description"));
        setTextForTextView(R.id.progress_text, data.getString("progressText"));

        final RoundCornerProgressBar progressBar = (RoundCornerProgressBar)
                fundraiserView.findViewById(R.id.fundraiser_progressbar); //retrieves the progress bar that displays the percent of the monetary goal that has been already donated

        /**
         * Retrieves backend information about the current user and the fundraiser that has been clicked into
         */
        final String fid = data.getString("fid");
        final String uid = data.getString("uid");
        final int amountRaised = data.getInt("amountRaised");

        /**
         * Updates the progress bar to the most recent donation values
         */
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("fundraisers/" + fid); //retrieves the current fundraiser from the Firebase database
        myRef.addValueEventListener(new ValueEventListener() { //listens for any changes to the current fundraiser. If someone donates money, for example, this EventListener will activate and update the progress bar
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int amountRaised = dataSnapshot.child("amountRaised").getValue(Integer.class); //gets the total amount that has been raised for the current fundraiser
                int goal = dataSnapshot.child("goal").getValue(Integer.class); //gets the target amount that the fundraiser wants to raise
                progressBar.setProgress(amountRaised * 100f / goal); //uses the RoundCornerProgressBar's method setProgress in order to display an accurate percentage on the progress bar
                setTextForTextView(R.id.progress_text, "$" + amountRaised + " raised of $" + goal);
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });

        /**
         * The use of MaterialRippleLayout for the "Buy", "Donate", and "Sell" options creates a unique and pleasing ripple effect when the option is selected.
         */
        MaterialRippleLayout buy = (MaterialRippleLayout) fundraiserView.findViewById(R.id.buy_button);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //adds the corresponding clicking functionality to the Buy option
                Intent buyIntent = new Intent(getActivity(), BrowseItemsActivity.class); //creates an Intent that will send the user to the page that allows item browsing
                buyIntent.putExtra("uid", uid);
                buyIntent.putExtra("fid", fid);
                getActivity().startActivity(buyIntent);
            }
        });

        MaterialRippleLayout donateButton = (MaterialRippleLayout) fundraiserView.findViewById(R.id.donate_button);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //adds the corresponding clicking functionality to the Donate option
                Intent donateIntent = new Intent(getActivity(), DonateActivity.class); //creates an Intent that will send the user to the page that allows direct monetary donation
                donateIntent.putExtra("uid", uid);
                donateIntent.putExtra("fid", fid);
                donateIntent.putExtra("amountRaised", amountRaised);
                getActivity().startActivity(donateIntent);
            }
        });

        MaterialRippleLayout sellButton = (MaterialRippleLayout) fundraiserView.findViewById(R.id.sell_button);
        sellButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { //adds the corresponding clicking functionality to the Sell option
                Intent sellIntent = new Intent(getActivity(), CreateItemActivity.class); //creates an Intent that will send the user to the page that allows the user to sell items for the fundraiser
                sellIntent.putExtra("uid", uid);
                sellIntent.putExtra("fid", fid);
                getActivity().startActivity(sellIntent);
            }
        });

        data.clear();
        return fundraiserView;
    }

    /**
     * A helper method that sets the text of a View
     * @param id The id of the View
     * @param text The desired text to be displayed on the View
     */
    public void setTextForTextView(int id, String text) {
        View v = fundraiserView.findViewById(id);
        if (v instanceof TextView) {
            ((TextView) v).setText(text);
        }
    }
}