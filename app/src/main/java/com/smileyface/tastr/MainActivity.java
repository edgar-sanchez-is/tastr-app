package com.smileyface.tastr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.smileyface.tastr.Utilities.firebaseHandler;

public class MainActivity extends AppCompatActivity {

    // Test variables
    firebaseHandler firebase = new firebaseHandler("Tastr Items");
    TastrItem item1 = new TastrItem();
    // end test variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Setup button listeners for the main menu
        Button tActivityButton = (Button) findViewById(R.id.touchViewButton);
        Button yActivityButton = (Button) findViewById(R.id.yelpViewButton);
        Button sActivityButton = (Button) findViewById(R.id.settingsViewButton);

        // test buttons
        Button databaseButton = (Button) findViewById(R.id.database);
        // end test buttons

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


        // test setting parameters
        firebase.setCity("Denton");
        firebase.setState("Texas");
        firebase.setYelpID("Chuys-of-Denton1");
        item1.setTastrID("123456");
        item1.setDescription("Delicious");
        item1.setRestaurant("Chuys");
        item1.setName("Cheese Enchiladas");
        item1.setRating("4.55");
        item1.setImageID("ench1.jpg");
        item1.setYelpRestaurantID("Chuys-of-Denton1");
        item1.setImagePath("Http://www.thisisamazing.com");
        // end test setting parameters


        databaseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firebase.writeTastrToDatabase(item1);
            }
        });
    }


}
