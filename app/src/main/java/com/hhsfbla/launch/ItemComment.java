package com.hhsfbla.launch;

/**
 * Created by Heidi on 2/5/2017.
 */

public class ItemComment {

    protected String uid;
    protected String itemID;
    protected String text;

    public ItemComment(String uid, String itemID, String text) {
        this.uid = uid;
        this.itemID = itemID;
        this.text = text;
    }
}
