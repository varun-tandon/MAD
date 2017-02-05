package com.hhsfbla.launch;

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

    public Item(String uid, String fundraiserID, String name, double price, String condition, String description) {
        this.uid = uid;
        this.fundraiserID = fundraiserID;
        this.name = name;
        this.price = price;
        this.condition = condition;
        this.description = description;
    }
}
