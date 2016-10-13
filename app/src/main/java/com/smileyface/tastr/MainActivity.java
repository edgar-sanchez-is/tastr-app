package com.smileyface.tastr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.smileyface.tastr.Utilities.firebaseHandler;

public class MainActivity extends AppCompatActivity {
    firebaseHandler firebase = new firebaseHandler("message");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Setup button listeners for the main menu
        Button tActivityButton = (Button) findViewById(R.id.touchViewButton);
        Button yActivityButton = (Button) findViewById(R.id.yelpViewButton);
        Button sActivityButton = (Button) findViewById(R.id.settingsViewButton);
        Button databaseButton = (Button) findViewById(R.id.database);

        tActivityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TouchActivity.class));
            }
        });

        yActivityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, YelpActivity.class));
            }
        });

        sActivityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        databaseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firebase.writeToDatabase("Testing");
            }
        });
    }


}
