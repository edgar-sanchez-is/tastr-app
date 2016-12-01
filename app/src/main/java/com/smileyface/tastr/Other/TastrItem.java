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
    public String tastrID;
    public String name;
    public String description;


    public String restaurant;
    public String rating;
    public String imageID;
    public String address;
    public String phone;
    public String categories;
    public ArrayList<MenuItem> menu = new ArrayList<MenuItem>();
    public ArrayList<String> imagePath = new ArrayList<String>();

    // Getters and Setters for parameters
    public String getTastrID() {
        return tastrID;
    }

    public void setTastrID(String tastrID) {
        this.tastrID = tastrID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPhone() {
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

    public void setMenu(ArrayList<MenuItem> menu) {
        this.menu = menu;
    }

    public ArrayList<MenuItem> getMenu() {
        return menu;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    //constructors
    public TastrItem() {
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

    public void addImagePath(String s) {
        this.imagePath.add(s);
    }
}
