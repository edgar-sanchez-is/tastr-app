package com.smileyface.tastr.Activities;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.smileyface.tastr.R;
import com.smileyface.tastr.Utilities.locationHandler;
import com.smileyface.tastr.Utilities.yelpDataExecutor;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup buttons for the main menu
        Button tActivityButton = (Button) findViewById(R.id.touchViewButton);
        Button sActivityButton = (Button) findViewById(R.id.settingsViewButton);

        tActivityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TouchActivity.class));
            }
        });

        sActivityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        listenForLocation location = new listenForLocation();
        location.execute(this);
    }//On Create

    private class listenForLocation extends AsyncTask<Activity, Void, locationHandler> {

        protected locationHandler doInBackground(Activity... params) {
            locationHandler curLoc = new locationHandler(params[0]);
            curLoc.askForlocation();
            while (curLoc.getCurrentLong() == null){
                // wait for an actual location update
            }
            return curLoc;
        }//doInBackground

        @Override
        protected void onPostExecute(locationHandler result) {
            yelpDataExecutor load = new yelpDataExecutor();
            load.execute(result.getCurrentLat(),result.getCurrentLong());

        }//On Post Execute
    }//listen for location Class
}//MainActivity Class
