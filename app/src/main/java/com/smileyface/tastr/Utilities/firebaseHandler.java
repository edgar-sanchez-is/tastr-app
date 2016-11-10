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
    // This includes a default menu item, since we are currently doing manual entry for menu items. 
    public void writeTastrToDatabase(TastrItem newItem) {

        final Map<String, Object> ID = new HashMap<>();
        ID.put("", restaurantName);
        reference.updateChildren(ID);

        // Create a new reference under the Restaurant hierarchy to write information such as Address and phone number.
        DatabaseReference topLevelRef = reference.child(getRestaurantName());
        Map<String, Object> menuMap = new HashMap<>();
        menuMap.put("Menu",null);
        topLevelRef.updateChildren(menuMap);
        DatabaseReference topMenuRef = topLevelRef.child("Menu");

        Map<String, Object> midMenuMap = new HashMap<>();
        midMenuMap.put("**Default Menu Item**",null);
        topMenuRef.updateChildren(midMenuMap);
        DatabaseReference midMenuRef = topMenuRef.child("**Default Menu Item**");
        Map<String, Object> lowMenuMap = new HashMap<>();
        lowMenuMap.put("Image Path","https://TestURL.com/image.jpg");
        lowMenuMap.put("Ingredients","Example Ingredient");
        lowMenuMap.put("Name","Example Item");
        lowMenuMap.put("Tastr ID","Example Tastr ID");
        midMenuRef.updateChildren(lowMenuMap);


        DatabaseReference locationRef = topLevelRef.child("Locations").child(getState());

        // Write new data to the database in the correct position.
        topLevelRef.updateChildren(newItem.getRestaurauntMap(newItem));
        Map<String, Object> locationMap = new HashMap<>();
        locationMap.put(getCity(),newItem.getAddress());
        locationRef.updateChildren(locationMap);


    }// writeToTastrDatabase
    
    // getters and setters for reading data from the database.
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

    //Specify a reference for the database to search with this method.
    //Temporarily fills a single array list with the child contents of search reference. 
    //Eventually this will be turned into a JSON object containing a list of tastr items.
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
    }//read from database

    // will use the default reference as the search parameter for reading from the database.
    public void readFromDatabase() {

        reference.addChildEventListener(new ChildEventListener(){
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<?,?> tempMap;
                tempMap = (Map<?, ?>) dataSnapshot.getValue();
                Iterator it = tempMap.entrySet().iterator();
                while(it.hasNext()){
                    System.err.println("Building Map From Firebase");
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
}//read from database


