package com.hhsfbla.launch;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by zhenfangchen on 1/30/17.
 */

public class Item {

    protected String uid;
    protected String fundraiserID;
    protected String name;
    protected double price;
    protected String condition;
    protected String description;

    protected Bitmap imageBitmap;

    public Item(String uid, String fundraiserID, String name, double price, String condition, String description) {
        this.uid = uid;
        this.fundraiserID = fundraiserID;
        this.name = name;
        this.price = price;
        this.condition = condition;
        this.description = description;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
