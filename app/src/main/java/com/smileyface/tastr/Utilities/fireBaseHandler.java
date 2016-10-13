package com.smileyface.tastr.Utilities;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smileyface.tastr.TastrItem;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cbrant on 10/13/2016.
 */

public class firebaseHandler {
    private FirebaseDatabase database;
    private DatabaseReference reference;

    // These strings represent "children" of the firebase, which is how things will be sorted. Example Under the data type TastrItems - > State - > City -> Restaurant -> Here you will find a list of food items under that particular restaurant.
    private String dataType = "Unknown";
    private String state ="Unknown";
    private String city ="Unknown";
    private String YelpID= "Unknown";

    // Getters and Setters for vars

    public String getYelpID() {
        return YelpID;
    }

    public void setYelpID(String yelpID) {
        YelpID = yelpID;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    //constructor requires database reference string. IE "message" or "TastrItem" Basically it names the category in which to search for in the database.
    public firebaseHandler(String ref){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(ref);
    }


    // Method Specifically for writing a new TastrItem to the database.
    public void writeTastrToDatabase(TastrItem newItem){
        // Define new parameters, this prevents errors when trying to write data to a state/city/ID we haven't added a restaurant to yet.
        reference.setValue(getState());
        reference.child(getState()).setValue(getCity());
        reference.child(getState()).child(getCity()).setValue(getYelpID());

        // Pick correct place in the database to save new data.
        DatabaseReference tempReference = reference.child(getState()).child(getCity()).child(getYelpID());
        // Write new data to the database
        tempReference.updateChildren(newItem.getMap(newItem));

    }

    public void readFromDatabase(){

        // Read from the database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                System.out.println("Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.err.println("Error Reading from Database");

            }
        });

    }


}


