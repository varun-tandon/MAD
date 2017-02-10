package com.hhsfbla.launch;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Heidi on 1/30/2017.
 */
public class Fundraiser implements Serializable{

    protected String id;

    protected String uid;

    protected String organizationName;
    protected String purpose;
    protected int goal;
    protected String deadline = "";
    protected String description;
    protected boolean hasImage;
    protected int amountRaised;

    protected ArrayList<String> itemIDs;

    protected Bitmap imageBitmap;

    public Fundraiser() {}

    public Fundraiser(String uid, String organizationName, String purpose, int goal, String deadline,
                      String description, boolean hasImage) {
        this.uid = uid;
        this.organizationName = organizationName;
        this.purpose = purpose;
        this.goal = goal;
        this.deadline = deadline;
        this.description = description;
        this.hasImage = hasImage;
        amountRaised = 0;

        itemIDs = new ArrayList<String>();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String makeProgressString() {
        return "$" + amountRaised + " raised of " + "$" + goal + " goal";
    }

    public String makeDaysRemainingString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date deadline = dateFormat.parse(this.deadline);
            Date today = new Date();
            int daysUntil = (int) ((deadline.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
            return daysUntil + " days left";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addItem(String itemID) {
        itemIDs.add(itemID);
    }

    public ArrayList<String> getItemIDs() {
        return itemIDs;
    }

}
