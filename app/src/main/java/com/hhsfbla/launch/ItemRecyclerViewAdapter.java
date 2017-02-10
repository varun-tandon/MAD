package com.hhsfbla.launch;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.balysv.materialripple.MaterialRippleLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by zhenfangchen on 2/6/17.
 */

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>  {
    public List<Item> mDataset;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String id;
        public String uid;
        public String fid;
        public String condition;
        public String description;
        public CardView card;
        public MaterialRippleLayout cardRipple;
        public ImageView image;
        public TextView name;
        public TextView price;

        private final Context context;

        public ViewHolder(View v) {
            super(v);
            context = v.getContext();
            card = (CardView) v.findViewById(R.id.item_card);
            cardRipple = (MaterialRippleLayout) v.findViewById(R.id.card_item_ripple);
            image = (ImageView) v.findViewById(R.id.item_card_image);
            name = (TextView) v.findViewById(R.id.item_card_name);
            price = (TextView) v.findViewById(R.id.item_card_price);

            //code influenced by http://stackoverflow.com/questions/28767413/how-to-open-a-different-activity-on-recyclerview-item-onclick
            cardRipple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ItemActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("uid", uid);
                    intent.putExtra("fid", fid);
                    intent.putExtra("condition", condition);
                    intent.putExtra("description", description);
                    intent.putExtra("name", name.getText());
                    intent.putExtra("price", price.getText());
                    String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), ((BitmapDrawable)image.getDrawable()).getBitmap(), "", "");
                    Uri uri = Uri.parse(path);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    context.startActivity(intent);
                }
            });
        }
    }

    public ItemRecyclerViewAdapter(Context c, List<Item> items) {
        context = c;
        mDataset = items;
    }

    @Override
    public ItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ItemRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.id = mDataset.get(position).id;
        holder.uid = mDataset.get(position).uid;
        holder.fid = mDataset.get(position).fundraiserID;
        holder.condition = mDataset.get(position).condition;
        holder.description = mDataset.get(position).description;
        holder.image.setImageBitmap(mDataset.get(position).imageBitmap);

        // set background color to average color of bitmap
        Bitmap bitmap = ((BitmapDrawable)holder.image.getDrawable()).getBitmap();
        int color = Bitmap.createScaledBitmap(bitmap, 1, 1, false).getPixel(0, 0);
        holder.image.setBackgroundColor(color);

        holder.name.setText(mDataset.get(position).name);
        holder.price.setText("$" + String.format("%.2f", mDataset.get(position).price));
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
