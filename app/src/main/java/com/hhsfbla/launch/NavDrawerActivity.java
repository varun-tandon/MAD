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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public static String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            Intent temp = getIntent();
            if (temp.hasExtra("id")) {
                userID = temp.getStringExtra("id");
            }
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new BrowseFundraisersFragment()).commit();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        LinearLayout navhome = (LinearLayout) findViewById(R.id.nav_head);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final String[] name = {""};
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("users").child(user.getUid());

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ((TextView) findViewById(R.id.nav_header_displayname)).setText(dataSnapshot.child("full_name").getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // ...
                }
            });
            System.out.println(name[0]);
            String email = user.getEmail();
            ((TextView) findViewById(R.id.nav_header_email)).setText(email);
        }
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.menu_item_share){
//            takeScreenshot();
//            String externalStorageDirectory = Environment.getExternalStorageDirectory().toString();
//            Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/" +  "lastScreen.jpg"));
//            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//            shareIntent.setType("text/html");
//            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test Mail");
//            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Launcher");
//            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//            startActivity(Intent.createChooser(shareIntent, "Share Deal"));

            takeScreenshot();
            Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/" +  "lastScreen.jpg"));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "I'm contributing to causes I believe in using Launch; join me in my efforts! #Launch");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share..."));




//            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//            sharingIntent.setType("image/jpeg");
//            String shareBody = "I'm contributing to causes I believe in using Launch; join me in my efforts! #Launch";
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Launch");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//            takeScreenshot();
//            sharingIntent.putExtra(Intent.EXTRA_STREAM, )));
//            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account_settings) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new AccountViewPageFragment()).commit();

        }
        else if (id == R.id.nav_fundraisers) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new YourFundraisersPageFragment()).commit();
        }
        else if (id == R.id.nav_browse) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new BrowseFundraisersFragment()).commit();
        }
        else if (id == R.id.nav_launch_fundraiser) {
            Intent launchFundraiserIntent = new Intent(NavDrawerActivity.this, CreateFundraiserActivity.class);
            NavDrawerActivity.this.startActivity(launchFundraiserIntent);
            finish();

        }
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchContent(int id, Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(id, fragment, fragment.toString());
        ft.addToBackStack(null);
        ft.commit();
    }
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" +  "lastScreen.jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }
}
