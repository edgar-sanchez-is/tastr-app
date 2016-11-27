package com.smileyface.tastr.Utilities;

import android.os.AsyncTask;
import android.util.Log;

import com.smileyface.tastr.Other.TastrItem;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Created by Remixt on 11/26/2016.
 */

public class ItemLoader extends AsyncTask<String, Void, String> {
    private int counter = 0;
    private firebaseHandler firebase;
    private yelpDataExecutor yelp;
    private ArrayList<TastrItem> itemList = new ArrayList<>();
    private boolean ready = false;

    public ItemLoader(yelpDataExecutor yelpContext) {
        yelp = yelpContext;
        Log.i("Item Loader ","Starting Database Operations...");
    }

    public TastrItem getNextItem() {
        if (counter > itemList.size() && itemList.size() > 0) {
            counter = 0;
            return itemList.get(0);
        } else if (itemList.size() > 0) {
            counter++;
            return itemList.get(counter - 1);
        }
        Log.i("Item Loader ","Item List is null");
        return null; // no items in the list yet
    }

    public boolean checkIfReady() {
        if (ready)
            return true;
        else
            return false;
    }

    // Everything you want to happen OUTSIDE of the GUI thread. This is a background process.
    protected String doInBackground(String... params) {
        // Add all the Tastr Items to the Array list using the list of restaurants found by yelp.
        for (int i = 0; i < yelp.getRestaurants().size(); i++) {
            TastrItem temp = new TastrItem();
            temp.setRestaurant(yelp.getRestaurants().get(i));

            // loop through each restaurant and add all the menu items from each one. I think we need to randomize this list later on to provide variety in the app.
                firebase = new firebaseHandler("Tastr Items/" + temp.getRestaurant() + "/Menu"); //Change where in the database we want to search for information.
                firebase.readKeyFromDatabase(); // Search the database for any Menu items available at the specified restaurant and put them into a list.

                // Wait for firebase to finish adding new data
                while (!firebase.isReaderDone()) {
                    try {
                        sleep(10); // wait 10 ms before checking again, saves cpu
                    } catch (InterruptedException e) {
                        e.printStackTrace(); // if there is a problem while sleeping, print out the errors encountered.
                    }
                }
                temp.setMenu(firebase.getReaderList());

                for (int k = 0; k < temp.getMenu().size(); k++) {

                    firebase = new firebaseHandler("Tastr Items/" + temp.getRestaurant() + "/Menu/" + temp.getMenu().get(k) + "/Image Path");

                    firebase.readValueFromDatabase();
                    // Wait for firebase to finish adding new data
                    while (!firebase.isReaderDone()) {
                        try {
                            sleep(10); // wait 50 ms before checking again, saves cpu
                        } catch (InterruptedException e) {
                            e.printStackTrace(); // if there is a problem while sleeping, print out the errors encountered.
                        }
                    }
                    temp.setImagePath(firebase.getReaderList());
                    Log.i("Class: ItemLoader ", "Image path added:" + firebase.getReaderList());

                }

            itemList.add(temp);
            ready = true;
        }
        return null;
    }//doInBackground

    // Everything you want to happen AFTER the doInBackground function is executed. Use this method to make changes to the GUI.
    @Override
    protected void onPostExecute(String results) {
        Log.i("Item Loader ","Finished Database Operations, closing firebase connection...");
        firebase.close();
        ready = true;
    }//On Post Execute
}//loader class
