package com.hhsfbla.launch;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Part 1 of the form for launching a fundraiser, which includes basic info like nonprofit name,
 * fundraiser purpose, monetary goal, and deadline for raising funds.
 */
public class CreateFundraiserActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_fundraiser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.create_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button nextButton = (Button) findViewById(R.id.create_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (getTextFromField(R.id.organizationNameField).length() == 0 || getTextFromField(R.id.fundraiserPurposeField).length() == 0
                    || getIntFromField(R.id.goalField) <= 0 || getTextFromField(R.id.fundraiser_dateField).length() == 0) {
                Toast.makeText(CreateFundraiserActivity.this, "Enter all fields first!", Toast.LENGTH_SHORT).show();
            }
            else {
                // send data to next page
                Intent nextPageIntent = new Intent(CreateFundraiserActivity.this, FinishCreateFundraiserActivity.class);
                nextPageIntent.putExtra("organizationName", getTextFromField(R.id.organizationNameField));
                nextPageIntent.putExtra("purpose", getTextFromField(R.id.fundraiserPurposeField));
                nextPageIntent.putExtra("goal", getIntFromField(R.id.goalField));
                nextPageIntent.putExtra("deadline", getTextFromField(R.id.fundraiser_dateField));
                CreateFundraiserActivity.this.startActivity(nextPageIntent);
                finish();
            }
            }
        });

        EditText dateField = (EditText) findViewById(R.id.fundraiser_dateField);
        dateField.setInputType(0);
        dateField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showDatePickerDialog(v);
            }
        });
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
    }

    /**
     * display Android datepicker which has a nice calendar ui
     * @param v
     */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(this.getFragmentManager(), "datePicker");
    }

    @Override
    /**
     * set text of date input field to selected date
     */
    public void onDateSet(DatePicker view, int year, int month, int day) {
        ((EditText) findViewById(R.id.fundraiser_dateField)).setText((month + 1) + "/" + day + "/" + year);
    }

    /**
     * wrapper method for easily retrieving text from TextView
     * @param id of view
     * @return String of text in TextView
     */
    public String getTextFromField(int id) {
        View v = findViewById(id);
        if (v instanceof EditText) {
            return ((EditText) v).getText().toString();
        }
        return null;
    }

    /**
     * wrapper method for easily retrieving a single number from a view
     * @param id of view
     * @return integer parsed from TextView text
     */
    public int getIntFromField(int id) {
        View v = findViewById(id);
        if (v instanceof EditText) {
            if (!((EditText) v).getText().toString().equals("")) {
                return Integer.parseInt(((EditText) v).getText().toString());
            }
        }
        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent nextPageIntent = new Intent(CreateFundraiserActivity.this, NavDrawerActivity.class);
                startActivity(nextPageIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onBackPressed() {
        Intent nextPageIntent = new Intent(CreateFundraiserActivity.this, NavDrawerActivity.class);
        startActivity(nextPageIntent);
        finish();
    }
}
