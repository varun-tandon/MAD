package com.hhsfbla.launch;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Heidi on 2/5/2017.
 */

public class ItemComment {

    protected String uid, uName;
    protected String itemID;
    protected String text;

    protected int order;

    public ItemComment() {

    }

    public ItemComment(String uid, String uName, String itemID, String text, int order) {
        this.uName = uName;
        this.uid = uid;
        this.itemID = itemID;
        this.text = text;
        this.order = order;
    }
}
