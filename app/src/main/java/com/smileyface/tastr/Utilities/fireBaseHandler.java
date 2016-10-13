package com.smileyface.tastr.Utilities;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by cbrant on 10/13/2016.
 */

public class firebaseHandler {
    FirebaseDatabase database;
    DatabaseReference reference;

    //constructor requires database reference string. IE "message" or "TastrItem" Basically it names the category in which to search for in the database.
    public firebaseHandler(String ref){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(ref);
    }

    public void writeToDatabase(String entry){
        reference.setValue(entry);
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


