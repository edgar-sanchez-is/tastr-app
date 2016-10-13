package com.smileyface.tastr;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * class: public class TastrItem
 * Created by Josh on 9/25/2016.
 * desc: class to hold data for the items that the user will see and select from
 * notes:
 **/
public class TastrItem {
    //parameters
    public static String tastrID;
    public static String name;
    public static String description;
    public static String restaurant;
    public static String yelpRestaurantID;
    public static String rating;
    public String imagePath;
    public static String imageID;

    // Getters and Setters for parameters
    public static String getTastrID() {
        return tastrID;
    }

    public static void setTastrID(String tastrID) {
        TastrItem.tastrID = tastrID;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        TastrItem.name = name;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        TastrItem.description = description;
    }

    public static String getRestaurant() {
        return restaurant;
    }

    public static void setRestaurant(String restaurant) {
        TastrItem.restaurant = restaurant;
    }

    public static String getYelpRestaurantID() {
        return yelpRestaurantID;
    }

    public static void setYelpRestaurantID(String yelpRestaurantID) {
        TastrItem.yelpRestaurantID = yelpRestaurantID;
    }

    public static String getRating() {
        return rating;
    }

    public static void setRating(String rating) {
        TastrItem.rating = rating;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public static String getImageID() {
        return imageID;
    }

    public static void setImageID(String imageID) {
        TastrItem.imageID = imageID;
    }


    //constructors
    public TastrItem() {
        tastrID = "Unknown";
        name = "Unknown";
        description = "Unknown";
        restaurant = "Unknown";
        yelpRestaurantID = "Unknown";
        rating = "Unknown";
        imagePath = "Unknown";
        imageID = "Unknown";
    }

    //convenience constructor
    public TastrItem(String mTastrID, String mName, String mDescription, String mRestraunt, String mYelpID, String mRating, String mImagePath, String mImageID) {
        tastrID = mTastrID;
        name = mName;
        description = mDescription;
        restaurant = mRestraunt;
        yelpRestaurantID = mYelpID;
        rating = mRating;
        imagePath = mImagePath;
        imageID = mImageID;
    }

    //converts Tastr Item into a hasmap for database storage
    public Map<String, Object> getMap(TastrItem newItem){

        Map<String, Object> tastrMap = new HashMap<String, Object>();

        tastrMap.put("1- Restraunt", newItem.getRestaurant());
        tastrMap.put("2- Tastr ID", newItem.getTastrID());
        tastrMap.put("3- Menu Item", newItem.getName());
        tastrMap.put("4- Description", newItem.getDescription());
        tastrMap.put("5- Rating", newItem.getRating());
        tastrMap.put("6- Image ID", newItem.getImageID());
        tastrMap.put("7- Image Path", newItem.getImagePath());


        return tastrMap;
    }

}
