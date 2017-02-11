package com.hhsfbla.launch;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents and holds data for a fundraiser
 * @author Heidi
 */
public class Fundraiser implements Serializable {

    protected String id;
    protected String uid;

    protected String organizationName;
    protected String purpose;
    protected int goal;
    protected String deadline = "";
    protected String description;
    protected boolean hasImage;
    protected int amountRaised;

    protected Bitmap imageBitmap;

    /**
     * no args constructor
     */
    public Fundraiser() {}

    /**
     * basic constructor
     * @param uid
     * @param organizationName
     * @param purpose
     * @param goal
     * @param deadline
     * @param description
     * @param hasImage
     */
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
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    /**
     * for convenience when displaying a fundraiser
     * @return String that combines amountRaised and goal
     */
    public String makeProgressString() {
        return "$" + amountRaised + " raised of " + "$" + goal + " goal";
    }

    /**
     * calculates number of days remaining in the fundraiser
     * @return String to display
     */
    public String makeDaysRemainingString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date deadline = dateFormat.parse(this.deadline);
            Date today = new Date();
            int daysUntil = (int) ((deadline.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
            if (daysUntil < 0)
                return daysUntil + " days since fundraiser ended";
            else
                return daysUntil + " days left";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * check if fundraiser already ended
     * @return boolean - true if fundraiser already ended, false if still ongoing
     */
    public boolean isEnded() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date deadline = dateFormat.parse(this.deadline);
            Date today = new Date();
            int daysUntil = (int) ((deadline.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
            if (daysUntil < 0)
                return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
