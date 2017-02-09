package com.hhsfbla.launch;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by zhenfangchen on 1/30/17.
 */

public class Item {

    protected String id;
    protected String uid;
    protected String fundraiserID;
    protected String name;
    protected double price;
    protected String condition;
    protected String description;

    protected Bitmap imageBitmap;

    protected boolean hasBitmap;

    public Item(){

    }

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

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public void setId(String id) {
        this.id = id;
    }
}
