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

import java.util.List;


public class FundraiserRecyclerViewAdapter extends RecyclerView.Adapter<FundraiserRecyclerViewAdapter.ViewHolder>  {
    private List<Fundraiser> mDataset;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String fid;
        public CardView card;
        public ImageView image;
        public TextView name;
        public TextView orgname;
        public TextView progressText;
        public RoundCornerProgressBar progressBar;
        public TextView daysLeftText;

        private final Context context;

        public ViewHolder(View v) {
            super(v);
            context = v.getContext();
            card = (CardView) v.findViewById(R.id.card);
            image = (ImageView) v.findViewById(R.id.card_image);
            name = (TextView) v.findViewById(R.id.card_name);
            orgname = (TextView) v.findViewById(R.id.card_nonprofit_name);
            progressText = (TextView) v.findViewById(R.id.card_progress_text);
            progressBar = (RoundCornerProgressBar) v.findViewById(R.id.card_progress);
            daysLeftText = (TextView) v.findViewById(R.id.card_days_text);
        }
    }

    public FundraiserRecyclerViewAdapter(Context c, List<Fundraiser> items) {
        context = c;
        mDataset = items;
    }

    @Override
    public FundraiserRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FundraiserRecyclerViewAdapter.ViewHolder holder, final int position) {
        final Fundraiser f = mDataset.get(position);
        holder.fid = f.id;
        holder.image.setImageBitmap(f.imageBitmap);
        holder.name.setText(f.purpose);
        holder.orgname.setText(f.organizationName);
        holder.progressText.setText(f.makeProgressString());
        holder.progressBar.setProgress(f.amountRaised * 100f / f.goal);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentJump(f);
            }
            // from http://stackoverflow.com/questions/28984879/how-to-open-a-different-fragment-on-recyclerview-onclick
            private void fragmentJump(Fundraiser mItemSelected) {
                FundraiserFragment mFragment = new FundraiserFragment();
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
                mBundle.putParcelable("bitmap", mItemSelected.imageBitmap);
                mFragment.setArguments(mBundle);
                switchContent(R.id.content_frame, mFragment);
            }
        });
    }

    public void switchContent(int id, Fragment fragment) {
        NavDrawerActivity mainActivity = (NavDrawerActivity) context;
        mainActivity.switchContent(id, fragment);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}