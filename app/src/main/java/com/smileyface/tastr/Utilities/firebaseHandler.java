package com.smileyface.tastr.Utilities;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smileyface.tastr.TastrItem;

import java.util.HashMap;
import java.util.Map;



//TODO: move yelp api to the firebase handler becuase it should only be used when new items are requested
public class firebaseHandler {
    private final DatabaseReference reference;

    // These strings represent "children" of the firebase, which is how things will be sorted. Example Under the data type TastrItems - > State - > City -> Restaurant -> Here you will find a list of food items under that particular restaurant.
    private String dataType = "Unknown";
    private String state = "Unknown";
    private String city = "Unknown";
    private String restaurantName = "Unknown";
    private boolean foundID = false;
    private String search = "";

    // Getters and Setters for vars

    private String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    private String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    //constructor requires database reference string. IE "message" or "TastrItem" Basically it names the category in which to search for in the database.
    public firebaseHandler(String ref) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference(ref);
    }


    // Method Specifically for writing a new TastrItem to the database.

    public void writeTastrToDatabase(TastrItem newItem) {

        final Map<String, Object> ID = new HashMap<>();
        ID.put("", restaurantName);
        reference.updateChildren(ID);

        // Create a new reference under the Restaurant hierarchy to write information such as Address and phone number.
        DatabaseReference topLevelRef = reference.child(getRestaurantName());
        topLevelRef.setValue("Menu");
        DatabaseReference topMenuRef = topLevelRef.child("Menu");
        DatabaseReference lowMenuRef = topMenuRef.child("Example Menu Item:");
        DatabaseReference locationRef = topLevelRef.child("Locations").child(getState());

        // Write new data to the database in the correct position.
        topLevelRef.updateChildren(newItem.getRestaurauntMap(newItem));
        lowMenuRef.updateChildren(newItem.getMenuMap(newItem));
        Map<String, Object> locationMap = new HashMap<>();
        locationMap.put(getCity(),newItem.getAddress());
        locationRef.updateChildren(locationMap);


    }

    private String getSearch() {
        return search;
    }

    private void setSearch(String input) {
        search = input;

    }

    public boolean searchForYelpID(String newSearch) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild(getSearch())) {
                    setFoundID(true);
                } else {
                    setFoundID(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };

        setSearch(newSearch);
        reference.child(getState()).child(getCity()).addListenerForSingleValueEvent(postListener);
        return foundID;
    }

    private void setFoundID(boolean input) {
        foundID = input;
    }

    public void readFromDatabase() {

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

    //TODO:in firebaseHandler: create func requestNewItems(int requestSize)
    //List<TastrItem> requestNewItems(int requestSize)
    //{
    //  get yelp stuff
    //  get firebase items
    //  filter list
    //  return list
    //}
}


