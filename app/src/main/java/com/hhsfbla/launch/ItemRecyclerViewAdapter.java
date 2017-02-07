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

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.List;

/**
 * Created by zhenfangchen on 2/6/17.
 */

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>  {
    private List<Item> mDataset;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String uid;
        public String fid;
        public String condition;
        public String description;
        public CardView card;

        public ImageView image;
        public TextView name;
        public TextView price;

        private final Context context;

        public ViewHolder(View v) {
            super(v);
            context = v.getContext();
            card = (CardView) v.findViewById(R.id.item_card);
            image = (ImageView) v.findViewById(R.id.item_card_image);
            name = (TextView) v.findViewById(R.id.item_card_name);
            price = (TextView) v.findViewById(R.id.item_card_price);
        }
    }

    public ItemRecyclerViewAdapter(Context c, List<Item> items) {
        context = c;
        mDataset = items;
    }

    @Override
    public ItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ItemRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.uid = mDataset.get(position).uid;
        holder.fid = mDataset.get(position).fundraiserID;
        holder.condition = mDataset.get(position).condition;
        holder.description = mDataset.get(position).description;

        holder.image.setImageBitmap(mDataset.get(position).imageBitmap);
        holder.name.setText(mDataset.get(position).name);
        holder.price.setText(mDataset.get(position).price+"");
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentJump(mDataset.get(position));
            }
            // from http://stackoverflow.com/questions/28984879/how-to-open-a-different-fragment-on-recyclerview-onclick
            private void fragmentJump(Item mItemSelected) {
                FundraiserFragment mFragment = new FundraiserFragment();
                Bundle mBundle = new Bundle();
                mBundle.putString("fid", mItemSelected.fundraiserID);
                mBundle.putString("uid", mItemSelected.uid);
                mBundle.putString("condition", mItemSelected.condition);
                mBundle.putString("description", mItemSelected.description);
                mBundle.putString("name", mItemSelected.name);
                mBundle.putString("price", mItemSelected.price+"");
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
