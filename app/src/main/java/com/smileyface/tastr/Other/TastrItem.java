package com.smileyface.tastr.Other;

import android.util.Log;

import java.util.ArrayList;
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
    private String tastrID;
    private String name;
    private String description;


    private String restaurant;
    private String rating;
    private String imageID;
    private String address;
    private String phone;
    private String categories;
    private ArrayList<String> menuItems = new ArrayList<String>();
    private ArrayList<String> imagePath = new ArrayList<String>();

    // Getters and Setters for parameters
    private String getTastrID() {
        return tastrID;
    }

    public void setTastrID(String tastrID) {
        this.tastrID = tastrID;
    }

    private String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    private String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    private String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCategories() {
        return categories;
    }

    public ArrayList<String> getImagePath() {
        return imagePath;
    }

    public void setImagePath(ArrayList<String> imageP) {
        for (int i = 0; i < imageP.size(); i++) {
            this.imagePath.add(imageP.get(i));
            Log.i("Tastr Item ", "Added Image Path");
        }

    }

    public void setMenu(ArrayList<String> menu) {
        for (int i = 0; i < menu.size(); i++) {
            this.menuItems.add(menu.get(i));
            Log.i("Class: TastrItem ", "Added Menu Item ---> " + menu.get(i));
        }

    }

    public ArrayList<String> getMenu() {
        return menuItems;
    }

    private String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    //constructors
    public TastrItem() {
        tastrID = "Unknown";
        name = "Unknown";
        description = "Unknown";
        rating = "Unknown";
        imageID = "Unknown";
        categories = "Unknown";
        phone = "unknown";
        address = "unknown";
    }

    //converts Tastr Item into a hash-map for database storage
    public Map<String, Object> getRestaurantMap(TastrItem newItem) {

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

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
