package com.hhsfbla.launch;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * An object that stores the data for an item that has been added by a user of the app
 */

public class Item {

    protected String id; //the ID of the Item as assigned by Firebase
    protected String uid; //the Firebase ID of the user that posted the Item for sale
    protected String fundraiserID; //the Firebase id of the fundraiser to which the Item has been posted
    protected String name; //the name of the Item
    protected double price; //the price of the Item
    protected String condition; //the condition of the Item, as limited to Bad, Acceptable, Used - Good, Used - Like New, and New
    protected String description; //a brief description of the Item provided by the seller

    protected Bitmap imageBitmap; //an Bitmap image that displays the Item

    protected boolean hasBitmap; //stores whether the Item has a Bitmap

    protected int numOfComments = 0; //a counter that stores the number of comments that the Item has

    /**
     * A default constructor
     */
    public Item(){

    }

    /**
     * A constructor that initalizes all the fields
     * @param uid the Firebase ID of the seller
     * @param fundraiserID the Firebase ID of the fundraiser
     * @param name
     * @param price
     * @param condition
     * @param description
     * @param hasBitmap
     */
    public Item(String uid, String fundraiserID, String name, double price, String condition, String description, boolean hasBitmap) {
        id = "";
        this.uid = uid;
        this.fundraiserID = fundraiserID;
        this.name = name;
        this.price = price;
        this.condition = condition;
        this.description = description;
        this.hasBitmap = hasBitmap;
    }

    /**
     * A setter method
     * @param imageBitmap the Bitmap that will be associated with the Item
     */
    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    /**
     * A setter method
     * @param id the unique Firebase ID that will be associated with the Item
     */
    public void setId(String id) {
        this.id = id;
    }
}
