package com.smileyface.tastr;

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
    private static String tastrID;
    private static String name;
    private static String description;
    private static String restaurant;
    private static String rating;
    private static String imagePath;
    private static String imageID;
    private static String address;
    private static String phone;
    private static String categories;

    // Getters and Setters for parameters
    private static String getTastrID() {
        return tastrID;
    }

    public static void setTastrID(String tastrID) {
        TastrItem.tastrID = tastrID;
    }

    private static String getName() {
        return name;
    }

    public static void setName(String name) {
        TastrItem.name = name;
    }

    private static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        TastrItem.description = description;
    }

    private static String getRestaurant() {
        return restaurant;
    }

    private static String getRating() {
        return rating;
    }

    public static void setRating(String rating) {
        TastrItem.rating = rating;
    }

    private String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    private String getCategories() {
        return categories;
    }

    private String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        TastrItem.imagePath = imagePath;
    }

    private static String getImageID() {
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
        rating = "Unknown";
        imagePath = "Unknown";
        imageID = "Unknown";
        categories = "Unknown";
        phone = "unknown";
        address = "unknown";
    }

    //convenience constructor
    public TastrItem(String mTastrID, String mName, String mDescription, String mRestaurant, String mYelpID, String mRating, String mImagePath, String mImageID) {
        tastrID = mTastrID;
        name = mName;
        description = mDescription;
        restaurant = mRestaurant;
        rating = mRating;
        imagePath = mImagePath;
        imageID = mImageID;
    }

    //converts Tastr Item into a hash-map for database storage
    public Map<String, Object> getRestaurauntMap(TastrItem newItem) {

        Map<String, Object> tastrMap = new HashMap<>();
        tastrMap.put(" Rating", getRating());
        tastrMap.put(" Category", newItem.getCategories());

        return tastrMap;
    }

    public Map<String, Object> getlocationMap(TastrItem newItem) {

        Map<String, Object> tastrMap = new HashMap<>();
        tastrMap.put("", getTastrID());
        return tastrMap;
    }

    public Map<String, Object> getMenuMap(TastrItem newItem) {

        Map<String, Object> tastrMap = new HashMap<>();
        tastrMap.put("Name", "Unknown");
        tastrMap.put("Ingredients: ", "Unknown");
        tastrMap.put("Tastr ID", "Unknown");
        tastrMap.put("Image Path", getImagePath());
        tastrMap.put("Image ID", getImageID());


        return tastrMap;
    }

    void setAddress(String address) {
        TastrItem.address = address;
    }

    public void setPhone(String phone) {
        TastrItem.phone = phone;
    }

    public void setCategories(String categories) {
        TastrItem.categories = categories;
    }
}
