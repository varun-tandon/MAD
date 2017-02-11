package com.hhsfbla.launch;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.balysv.materialripple.MaterialRippleLayout;

import java.util.List;

/**
 * An adapter that displays the fundraisers hosted on the app. Allows the user to scroll through all available
 * fundraisers and select ones that they are interested in. Once selected, the user is then taken to the fundraiser's
 * specific page through an Intent.
 */

public class FundraiserRecyclerViewAdapter extends RecyclerView.Adapter<FundraiserRecyclerViewAdapter.ViewHolder>  {
    private List<Fundraiser> mDataset; //a list of all the fundraisers existing in the app
    private Context context; //the context of the Fragment that the adapter is hosted in

    /**
     * A container that holds all the information of a specific fundraiser
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String fid; //the fundraiser ID assigned to it by Firebase
        public CardView card; //the View that is displayed for the specific fundraiser when browsing through all the fundraisers
        public MaterialRippleLayout cardRipple; //used to create a ripple effect when selecting the fundraiser
        public ImageView image; //the image that is used for the fundraiser
        public TextView name;
        public TextView orgname;
        public TextView progressText; //specifies the amount that has been donated so far and the goal amount
        public RoundCornerProgressBar progressBar;
        public TextView daysLeftText;

        /**
         * A constructor that initializes all the fields of the container
         * @param v a View that is associated with the current container
         */
        public ViewHolder(View v) {
            super(v);
            /**
             * Retrieves all the necessary data from the relevant Views contained in the parameter View
             */
            card = (CardView) v.findViewById(R.id.card);
            cardRipple = (MaterialRippleLayout) v.findViewById(R.id.card_ripple);
            image = (ImageView) v.findViewById(R.id.card_image);
            name = (TextView) v.findViewById(R.id.card_name);
            orgname = (TextView) v.findViewById(R.id.card_nonprofit_name);
            progressText = (TextView) v.findViewById(R.id.card_progress_text);
            progressBar = (RoundCornerProgressBar) v.findViewById(R.id.card_progress);
            daysLeftText = (TextView) v.findViewById(R.id.card_days_text);
        }
    }

    /**
     * A constructor for the adapter that encapsulates all the fundraisers
     * @param c the Context of the Fragment that includes the adapter
     * @param items a List of all the fundraisers to be included in the adapter
     */
    public FundraiserRecyclerViewAdapter(Context c, List<Fundraiser> items) {
        context = c;
        mDataset = items;
    }

    /**
     * Creates a frame for the container of each individual fundraiser
     * @param parent the container that contains all the individual fundraisers
     * @param viewType the type of container
     * @return the created fundraiser frame
     */
    public FundraiserRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        ViewHolder vh = new ViewHolder(v); //creates a container for the View
        return vh;
    }

    /**
     * Inserts the data from a fundraiser into its container
     * @param holder the container for the fundraiser
     * @param position gives information on which container is being altered
     */
    public void onBindViewHolder(FundraiserRecyclerViewAdapter.ViewHolder holder, final int position) {
        final Fundraiser f = mDataset.get(position);
        /**
         * Inserts the fundraiser information from the Fundraiser object onto the user interface
         */
        holder.fid = f.id;
        holder.image.setImageBitmap(f.imageBitmap);
        holder.name.setText(f.purpose);
        holder.orgname.setText(f.organizationName);
        holder.progressText.setText(f.makeProgressString());
        holder.progressBar.setProgress(f.amountRaised * 100f / f.goal);
        holder.daysLeftText.setText(f.makeDaysRemainingString());

        /**
         * Sets the click functionality for the container. When clicked, the user will be led to the fundraiser's individual page
         */
        holder.cardRipple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentJump(f);
            }
            // from http://stackoverflow.com/questions/28984879/how-to-open-a-different-fragment-on-recyclerview-onclick
            private void fragmentJump(Fundraiser mItemSelected) {
                FundraiserFragment mFragment = new FundraiserFragment();

                /**
                 * Creates and stores all the information needed for the fundraiser page
                 */
                Bundle mBundle = new Bundle();
                mBundle.putString("fid", mItemSelected.id);
                mBundle.putString("uid", mItemSelected.uid);
                mBundle.putString("purpose", mItemSelected.purpose);
                mBundle.putString("orgname", mItemSelected.organizationName);
                mBundle.putString("description", mItemSelected.description);
                mBundle.putString("progressText", mItemSelected.makeProgressString());
                mBundle.putString("deadline", mItemSelected.deadline);
                mBundle.putInt("amountRaised", mItemSelected.amountRaised);
                mBundle.putInt("goal", mItemSelected.goal);
                mBundle.putParcelable("bitmap", mItemSelected.imageBitmap); //utilizes the fact that Bitmaps are Parcelable
                mFragment.setArguments(mBundle);
                switchContent(R.id.content_frame, mFragment);
            }
        });
    }

    /**
     * A helper method that switches from browsing fundraisers to a specific fundraiser page
     * @param id The ID of the screen that is being switched out of
     * @param fragment The fragment that is being switched to
     */
    public void switchContent(int id, Fragment fragment) {
        NavDrawerActivity mainActivity = (NavDrawerActivity) context; //uses the fact that browsing fundraisers is a fragment, and is thus overlayed onto the main Activity
        mainActivity.switchContent(id, fragment); //switches the overlayed fragment to the individual fundraiser fragment
    }

    /**
     * A getter method
     * @return the number of Fundraisers available
     */
    public int getItemCount() {
        return mDataset.size();
    }
}