package com.hhsfbla.launch;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.FragmentManager;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * The base Activity of the application. At first, hosts browsing fundraisers, and navigation is made
 * possible by a navigation drawer. The drawer can slide out through the clicking of a button, and
 * on the drawer are selections for different screens.
 */

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String userID; //stores the Firebase ID for the current user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Checks if there is an Intent, and if there is one, extracts the user ID from it
         */
        if (getIntent() != null) {
            Intent temp = getIntent();
            if (temp.hasExtra("id")) {
                userID = temp.getStringExtra("id");
            }
        }

        /**
         * Hosts a FragmentManager because from the drawer, different fragments can be selected, meaning
         * that the FragmentManager would have to be used to navigate back and forth across the fragments
         */
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new BrowseFundraisersFragment()).commit(); //begins by displaying the browsing fundraisers Fragment
        setContentView(R.layout.activity_main);

        /**
         * Establishes the toolbar at the top of the screen
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Instantiates the drawer by using a DrawerLayout
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close); //allows the drawer to be opened and closed
        drawer.setDrawerListener(toggle);
        toggle.syncState(); //syncs the closing and opening of the toolbar with the retrievable status

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * A method used to close the navigation drawer when the back button on the drawer is clicked
     */
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) { //closes the drawer if it is currently opened
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * A method used to add the different selections to the navigation drawer when opened
     * @param menu the Menu that is to be inflated
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //retrieves the current user from the Firebase database
        if (user != null) { //if the user currently exists, then data that is to be displayed on the drawer is retrieved
            String name = user.getDisplayName();
            String email = user.getEmail();
            ((TextView) findViewById(R.id.nav_header_displayname)).setText(name);
            ((TextView) findViewById(R.id.nav_header_email)).setText(email);
        }
        getMenuInflater().inflate(R.menu.main, menu); //the drawer is inflated with the Menu that is passed in
        return true;
    }

    /**
     * Establishes the functionality of the toolbar at the top of the screen. Here, the toolbar has an option
     * to share the page on various social media platforms
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId(); //the id of the item selected on the toolbar
        if(id == R.id.menu_item_share){ //if the id matches the one associated with the social media sharing option
            takeScreenshot(); //calls the helper method that takes and saves a screenshot of the current page

            //stores the screenshot onto the user's device, and allows future retrieval using the image Uri
            Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/" +  "lastScreen.jpg"));

            /**
             * Creates an Intent that will allow the user to share the app on social media
             */
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "I'm contributing to causes I believe in using Launch; join me in my efforts! #Launch"); //adds a text message to the social media share
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri); //adds the image Uri path to allow future retrieval of the image
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share..."));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    /**
     * A Method that handles the selection of options on the navigation drawer
     */
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId(); //gets the ID of the option selected

        /**
         * Displays a new Fragment or sends the user to a new Activity depending on which option was selected
         */
        if (id == R.id.nav_account_settings) { //if the item selected goes to the Account Settings page
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new AccountViewPageFragment()).commit();
        }
        else if (id == R.id.nav_fundraisers) { //if the item selected goes to the page that displays all your fundraisers
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new YourFundraisersPageFragment()).commit();
        }
        else if (id == R.id.nav_browse) { //if the item selected goes to the page that allows you to browse all the fundraisers
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new BrowseFundraisersFragment()).commit();
        }
        else if (id == R.id.nav_launch_fundraiser) { //if the item selected goes to the fundraiser creation page
            Intent launchFundraiserIntent = new Intent(NavDrawerActivity.this, CreateFundraiserActivity.class);
            NavDrawerActivity.this.startActivity(launchFundraiserIntent);
            finish();
        }

        /**
         * Closes the drawer once the option has been selected
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Switches the Fragment currently displayed
     * @param id the ID of the currently displayed fragment
     * @param fragment the Fragment to be switched to
     */
    public void switchContent(int id, Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction(); //initializes a Fragment exchange to the FragmentManager hosted in the base Activity
        ft.replace(id, fragment, fragment.toString()); //replaces the currently displayed Fragment with the wanted one
        ft.addToBackStack(null);
        ft.commit(); //finalizes the exchange
    }

    /**
     * A helper method that takes a screenshot of the current screen
     */
    private void takeScreenshot() {
        /**
         * Retrieves and formats the current time and date
         */
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" +  "lastScreen.jpg"; //creates the path to where the image is stored on the SD card

            /**
             * Creates a Bitmap from the screenshot taken
             */
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            /**
             * Creates a new File out of the created path
             */
            File imageFile = new File(mPath);

            /**
             * Pushes the File containing the Bitmap image into the saved slot
             */
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Throwable e) {
            // Several errors may come out with file handling or OOM
            e.printStackTrace();
        }
    }
}
