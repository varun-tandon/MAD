package com.hhsfbla.launch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.List;


public class FundraiserRecyclerViewAdapter extends RecyclerView.Adapter<FundraiserRecyclerViewAdapter.ViewHolder>  {
    private List<Fundraiser> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
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

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FundraiserActivity.class);
//                    intent.putExtra("fid");
//                    uid = intent.getStringExtra("uid");
//                    organizationName = intent.getStringExtra("organizationName");
//                    purpose = intent.getStringExtra("purpose");
//                    deadline = intent.getStringExtra("deadline");
//                    description = intent.getStringExtra("description");
//
//                    goal = intent.getIntExtra("goal", 0);
//                    amountRaised = intent.getIntExtra("amountRaised", 0);
//
//                    hasImage = intent.getBooleanExtra("hasImage", false);
                }
            });
        }
    }

    public FundraiserRecyclerViewAdapter(List<Fundraiser> items) {
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
        holder.image.setImageBitmap(mDataset.get(position).imageBitmap);
        holder.name.setText(mDataset.get(position).purpose);
        holder.orgname.setText(mDataset.get(position).organizationName);
        holder.progressText.setText(mDataset.get(position).makeProgressString());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}