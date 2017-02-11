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
 * An adapter that displays the items hosted by a specific fundraiser. Allows the user to scroll through all available
 * items and select ones that they are interested in. Once selected, the user is then taken to the item's
 * specific page through an Intent.
 */

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>  {
    public List<Item> mDataset; //a list of all the Items within a specific Fundraiser
    private Context context; //the Context of the Activity that hosts the adapter

    /**
     * A container that holds all the information of a specific Item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String id; //the Firebase ID of the item
        public String uid; //the Firebase ID of the seller
        public String fid; //the Firebase ID of the fundraiser that the item is hosted on
        public String condition; //the condition of the fundraiser, limited to Bad, Acceptable, Used - Good, Used - Like New, and New
        public String description;
        public CardView card; //the View that is displayed for the specific Item when scrolling through
        public MaterialRippleLayout cardRipple; //creates a ripple effect when clicking on the Item
        public ImageView image; //the holder for the image of the Item
        public TextView name;
        public TextView price;

        private final Context context;

        /**
         * A constructor that creates a container for the Item based on the parameter View
         * @param v
         */
        public ViewHolder(View v) {
            super(v);
            context = v.getContext();

            /**
             * Retrieves all necessary data from the View, and initializes them to the fields
             */
            card = (CardView) v.findViewById(R.id.item_card);
            cardRipple = (MaterialRippleLayout) v.findViewById(R.id.card_item_ripple);
            image = (ImageView) v.findViewById(R.id.item_card_image);
            name = (TextView) v.findViewById(R.id.item_card_name);
            price = (TextView) v.findViewById(R.id.item_card_price);

            //code influenced by http://stackoverflow.com/questions/28767413/how-to-open-a-different-activity-on-recyclerview-item-onclick

            /**
             * Adds the click functionality to the container of the Item, and sends the user to the Item page
             */
            cardRipple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ItemActivity.class);
                    /**
                     * Adds all the necessary information to the Intent
                     */
                    intent.putExtra("id", id);
                    intent.putExtra("uid", uid);
                    intent.putExtra("fid", fid);
                    intent.putExtra("condition", condition);
                    intent.putExtra("description", description);
                    intent.putExtra("name", name.getText());
                    intent.putExtra("price", price.getText());
                    /**
                     * Stores the Bitmap image onto the user's device, allowing future retrieval using the Uri path
                     */
                    String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), ((BitmapDrawable)image.getDrawable()).getBitmap(), "", "");
                    Uri uri = Uri.parse(path);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     * A constructor for the adapter that encapsulates all the items
     * @param c the Context of the Fragment that includes the adapter
     * @param items a List of all the items to be included in the adapter
     */
    public ItemRecyclerViewAdapter(Context c, List<Item> items) {
        context = c;
        mDataset = items;
    }

    /**
     * Creates a frame for the container of each individual item
     * @param parent the container that contains all the individual items
     * @param viewType the type of container
     * @return the created item frame
     */
    public ItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /**
     * Inserts the data from a item into its container
     * @param holder the container for the item
     * @param position gives information on which container is being altered
     */
    public void onBindViewHolder(ItemRecyclerViewAdapter.ViewHolder holder, final int position) {
        /**
         * Inserts the item information into the container user interface
         */
        holder.id = mDataset.get(position).id;
        holder.uid = mDataset.get(position).uid;
        holder.fid = mDataset.get(position).fundraiserID;
        holder.condition = mDataset.get(position).condition;
        holder.description = mDataset.get(position).description;
        holder.image.setImageBitmap(mDataset.get(position).imageBitmap);

        /**
         * Sets the background of the Item to the average color of the provided picture
         */
        Bitmap bitmap = ((BitmapDrawable)holder.image.getDrawable()).getBitmap();
        int color = Bitmap.createScaledBitmap(bitmap, 1, 1, false).getPixel(0, 0);
        holder.image.setBackgroundColor(color);

        holder.name.setText(mDataset.get(position).name);
        holder.price.setText("$" + String.format("%.2f", mDataset.get(position).price)); //uses String parsing to guarantee two decimal places in the monetary amount
    }

    /**
     * A helper method that switches from browsing items to a specific item page
     * @param id The ID of the screen that is being switched out of
     * @param fragment The fragment that is being switched to
     */
    public void switchContent(int id, Fragment fragment) {
        NavDrawerActivity mainActivity = (NavDrawerActivity) context; //uses the fact that browsing items is a fragment, and is thus overlayed onto the main Activity
        mainActivity.switchContent(id, fragment); //switches the overlayed fragment to the individual item fragment
    }

    /**
     * A getter method
     * @return the number of Items available
     */
    public int getItemCount() {
        return mDataset.size();
    }
}
