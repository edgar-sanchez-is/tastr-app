package com.smileyface.tastr.Utilities;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smileyface.tastr.Other.TastrItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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


    // getters and setters for retreiving data from the database.
    ArrayList<String> readerList = new ArrayList<>();
    void setReaderList(String input){
        readerList.add(input);
    }
    public ArrayList<String> getReaderList(){
        return readerList;
    }

    boolean readerDone = false;
    public boolean isReaderDone(){
        return readerDone;
    }

    public void setReaderDone(boolean input){
        readerDone = input;
    }

    // You can either specify a reference for the database to search with this method, or use the one below to use the default search which is created when instantiating a new firebaseHandler.
    public void readFromDatabase(DatabaseReference temporaryRef) {

        ArrayList<String> tmpList = new ArrayList<>();
        temporaryRef.addChildEventListener(new ChildEventListener(){

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<?,?> tempMap;
                tempMap = (Map<?, ?>) dataSnapshot.getValue();

                Iterator it = tempMap.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry pair = (Map.Entry) it.next();
                    setReaderList(pair.getValue().toString());
                    System.out.println(pair.getValue().toString() +  " This is from the child loop ");
                    it.remove();
                }
                setReaderDone(true);
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void readFromDatabase() {
        //readerList.clear(); // clears all the data from the list to avoid duplicate data.
        reference.addChildEventListener(new ChildEventListener(){
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<?,?> tempMap;
                tempMap = (Map<?, ?>) dataSnapshot.getValue();
                Iterator it = tempMap.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry pair = (Map.Entry) it.next();
                    setReaderList(pair.getValue().toString());
                    System.out.println(pair.getValue().toString());
                    it.remove();
                }
                setReaderDone(true);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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


