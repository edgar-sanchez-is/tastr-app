package com.smileyface.tastr.Utilities;

import android.os.AsyncTask;
import android.os.Process;

import com.smileyface.tastr.Other.TastrItem;
import com.smileyface.tastr.Yelp.*;

import java.util.ArrayList;

/**
 * Created by Remixt on 10/23/2016.
 */

public class yelpDataExecutor extends AsyncTask<String, Void, String> {

    boolean hasNewData = false;

    public boolean checkHasNewData() {
        return hasNewData;
    }

    int numberOfBusinesses = 0;
    YelpAPI API = new YelpAPI();

    // important to use an Array list for the business ID values so its easier to iterate through them and retrieve them based on index in order of closest to furthest.
    ArrayList<String> businessIDs = new ArrayList<>();
    ArrayList<String> ratings = new ArrayList<>();
    ArrayList<String> cities = new ArrayList<>();
    ArrayList<String> states = new ArrayList<>();
    ArrayList<String> addresses = new ArrayList<>();
    ArrayList<String> categories = new ArrayList<>();
    ArrayList<String> phones = new ArrayList<>();
    ArrayList<String> restaurants = new ArrayList<>();

    /**
    *Everything you want to happen OUTSIDE of the GUI thread.
    *params in order of use:
    *params[0] = latitude
    *params[1] = longitude
    */


    public ArrayList<String> getRestaurants() {

        return restaurants;
    }
    protected String doInBackground(String... params) {
        hasNewData = false;
        System.err.println("Waiting for GPS signal");
        System.err.println("GPS signal found, contacting Yelp");
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        // Double check for non null values to prevent app crash. Then set search location.
        if (params[0] != null && params[1] != null) {
            API.setLocation(params[0], params[1]);

        }//if

        // else{put something in here to grab zip code and convert it to a gps location later.}

        // Starts the API, which will automatically contact yelp and populate the business information.
        API.run();

        // Checks how many Businesses yelp found
        numberOfBusinesses = API.getNumberOfBusinessIDS();

        // Makes a copy of the array of Business ID's found in the most recent Yelp API query. Resets the array list to avoid duplicate data.
        // Also clears the previous results if this is the second time hitting the button.
        businessIDs.clear();

        businessIDs = API.getBusinessIDList();
        ratings = YelpAPI.getRatingList();
        cities = YelpAPI.getCityList();
        states = YelpAPI.getStateList();
        addresses = YelpAPI.getAddressList();
        categories = YelpAPI.getCategoryList();
        phones = YelpAPI.getPhoneList();
        restaurants = YelpAPI.getNameList();
/*
        firebaseHandler firebase = new firebaseHandler("Tastr Items");
        for (int i = 0; i < numberOfBusinesses; i++) {

            TastrItem newItem = new TastrItem();
            firebase.setRestaurantName(restaurants.get(i).replaceAll("[.#$\\]\\\\\\[]", ""));
            TastrItem.setRating(ratings.get(i).replaceAll("[.#$\\]\\\\\\[]", ""));
            firebase.setCity(cities.get(i).replaceAll("[.#$\\]\\\\\\[]", ""));
            firebase.setState(states.get(i).replaceAll("[.#$\\]\\\\\\[]", ""));
            newItem.setCategories(categories.get(i).replaceAll("[.#$\\]\\\\\\[]", ""));
            newItem.setPhone(phones.get(i).replaceAll("[.#$\\]\\\\\\[]", ""));
            newItem.setAddress(addresses.get(i).replaceAll("[.#$\\]\\\\\\[]", ""));
            firebase.writeTastrToDatabase(newItem);

        }//for
*/
        return null;
    }//doInBackground

    // Everything you want to happen AFTER the doInBackground function is executed. Use this method to make changes to the GUI.
    @Override
    protected void onPostExecute(String result) {

        if (numberOfBusinesses < 1) {
            System.err.println("No businesses found");
        }//if
        hasNewData = true;


    }//On Post Execute

}//Yelp Loader Class
