package com.hhsfbla.launch;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * An object that holds the data for a comment made on an Item
 */

public class ItemComment {

    protected String uid, uName; //the Firebase ID and the name of the user who made the comment
    protected String itemID; //the Firebase ID of the item on which the comment was made
    protected String text; //the comment itself

    protected int order; //a helper field that establishes the order of which the comment must be displayed

    /**
     * A default constructor
     */
    public ItemComment() {

    }

    /**
     * A constructor that initializes all fields
     * @param uid the Firebase ID of the user who made the comment
     * @param uName the name of the user who made the comment
     * @param itemID the Firebase ID of the item on which the comment was made
     * @param text
     * @param order the number of comments made on the same Item before this comment was made
     */
    public ItemComment(String uid, String uName, String itemID, String text, int order) {
        this.uName = uName;
        this.uid = uid;
        this.itemID = itemID;
        this.text = text;
        this.order = order;
    }
}
