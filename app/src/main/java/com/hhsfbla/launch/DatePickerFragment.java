package com.hhsfbla.launch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), (CreateFundraiserActivity)getActivity(), year, month, day);
        dialog.getDatePicker().setMinDate(new Date().getTime());

        // Create a new instance of DatePickerDialog and return it
        return dialog;
    }

}